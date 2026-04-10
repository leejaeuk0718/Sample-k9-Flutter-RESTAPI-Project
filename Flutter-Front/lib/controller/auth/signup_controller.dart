import 'dart:convert';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:image_picker/image_picker.dart';
import 'package:path/path.dart' as p;
import 'package:path_provider/path_provider.dart';

import '../../const/api_constants.dart';

/// 회원가입 컨트롤러 (프로필 이미지 포함 통합 가입 버전)
class SignupController extends ChangeNotifier {
  // 입력 필드 컨트롤러
  final TextEditingController idController = TextEditingController();
  final TextEditingController emailController = TextEditingController();
  final TextEditingController passwordController = TextEditingController();
  final TextEditingController passwordConfirmController =
      TextEditingController();
  final TextEditingController mnameController = TextEditingController();
  final TextEditingController regionController = TextEditingController();

  // 상태 변수
  bool _isPasswordMatch = false;
  bool get isPasswordMatch => _isPasswordMatch;

  bool _isLoading = false;
  bool get isLoading => _isLoading;

  // 프로필 이미지
  File? _profileImageFile;
  File? get profileImageFile => _profileImageFile;

  // 지역 선택 드롭다운용 리스트
  final List<String> regions = [
    '서울특별시', '부산광역시', '대구광역시', '인천광역시',
    '광주광역시', '대전광역시', '울산광역시', '세종특별자치시',
    '경기도', '강원특별자치도', '충청북도', '충청남도',
    '전북특별자치도', '전라남도', '경상북도', '경상남도', '제주특별자치도'
  ];

  String? _selectedRegion;
  String? get selectedRegion => _selectedRegion;

  /// 지역 선택 시 호출
  void setRegion(String? value) {
    _selectedRegion = value;
    regionController.text = value ?? '';
    notifyListeners();
  }

  /// 비밀번호 일치 확인
  void validatePassword() {
    _isPasswordMatch = (passwordController.text.isNotEmpty &&
        passwordController.text == passwordConfirmController.text);
    notifyListeners();
  }

  // ── 프로필 이미지 선택 ────────────────────────────────────────────

  /// 갤러리 또는 카메라로 프로필 이미지 선택
  Future<void> pickProfileImage(BuildContext context, ImageSource source) async {
    final picker = ImagePicker();
    final XFile? picked = await picker.pickImage(
      source: source,
      maxWidth: 1024,
      maxHeight: 1024,
      imageQuality: 85,
    );

    if (picked == null) return;

    final File file = File(picked.path);
    final int fileSize = await file.length();

    // 5MB 크기 제한 검사
    if (fileSize > 5 * 1024 * 1024) {
      if (!context.mounted) return;
      _showDialog(context, '이미지 크기 초과', '이미지 파일 크기는 5MB 이하여야 합니다.\n현재 크기: ${(fileSize / 1024 / 1024).toStringAsFixed(1)}MB');
      return;
    }

    // 앱 내부 저장소에 복사 저장
    await _saveToAppStorage(file);

    _profileImageFile = file;
    notifyListeners();
  }

  /// 선택한 이미지를 앱 내부 Documents 폴더에 복사 저장
  Future<void> _saveToAppStorage(File sourceFile) async {
    try {
      final Directory appDir = await getApplicationDocumentsDirectory();
      final String profileDir = '${appDir.path}/profile_images';
      await Directory(profileDir).create(recursive: true);

      final String fileName = p.basename(sourceFile.path);
      final String destPath = '$profileDir/$fileName';
      await sourceFile.copy(destPath);
    } catch (_) {
      // 내부 저장 실패는 무시 (서버 업로드는 계속 진행)
    }
  }

  /// 선택한 이미지를 base64로 인코딩
  Future<String?> _encodeImageToBase64(File file) async {
    try {
      final List<int> bytes = await file.readAsBytes();
      final String ext = p.extension(file.path).toLowerCase().replaceAll('.', '');
      final String mimeType = ext == 'png' ? 'png' : 'jpeg';
      final String base64Str = base64Encode(bytes);
      return 'data:image/$mimeType;base64,$base64Str';
    } catch (_) {
      return null;
    }
  }

  /// 프로필 이미지 선택 다이얼로그 (갤러리 / 카메라 선택)
  Future<void> showImagePickerDialog(BuildContext context) async {
    await showModalBottomSheet(
      context: context,
      builder: (ctx) => SafeArea(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            ListTile(
              leading: const Icon(Icons.photo_library),
              title: const Text('갤러리에서 선택'),
              onTap: () {
                Navigator.of(ctx).pop();
                pickProfileImage(context, ImageSource.gallery);
              },
            ),
            ListTile(
              leading: const Icon(Icons.camera_alt),
              title: const Text('카메라로 촬영'),
              onTap: () {
                Navigator.of(ctx).pop();
                pickProfileImage(context, ImageSource.camera);
              },
            ),
            if (_profileImageFile != null)
              ListTile(
                leading: const Icon(Icons.delete, color: Colors.red),
                title: const Text('이미지 제거', style: TextStyle(color: Colors.red)),
                onTap: () {
                  Navigator.of(ctx).pop();
                  _profileImageFile = null;
                  notifyListeners();
                },
              ),
          ],
        ),
      ),
    );
  }

  // ── 아이디 중복 체크 ──────────────────────────────────────────────

  Future<void> checkDuplicateId(BuildContext context) async {
    final inputId = idController.text.trim();
    if (inputId.isEmpty) {
      _showDialog(context, '오류', '아이디를 입력하세요.');
      return;
    }
    try {
      final response = await http.get(
        Uri.parse('${ApiConstants.springBaseUrl}/member/check-mid?mid=$inputId'),
      );
      if (!context.mounted) return;

      if (response.statusCode == 200) {
        final Map<String, dynamic> data =
            jsonDecode(utf8.decode(response.bodyBytes));
        final bool isAvailable = data['available'] ?? false;
        if (isAvailable) {
          _showDialog(context, '사용 가능', '이 아이디는 사용할 수 있습니다.');
        } else {
          _showDialog(context, '중복된 아이디', '이미 사용 중인 아이디입니다.');
        }
      } else {
        _showDialog(context, '오류', '서버 응답 오류: ${response.statusCode}');
      }
    } catch (e) {
      if (!context.mounted) return;
      _showDialog(context, '오류', '네트워크 오류: $e');
    }
  }

  // ── 회원가입 ──────────────────────────────────────────────────────

  /// 단일 통합 회원가입 처리
  /// API: POST /api/member/signup
  Future<void> signup(BuildContext context) async {
    // 1. 유효성 검사
    if (!_isPasswordMatch) {
      _showDialog(context, '오류', '비밀번호가 일치하지 않습니다.');
      return;
    }

    final mid = idController.text.trim();
    final mpw = passwordController.text.trim();
    final mpwConfirm = passwordConfirmController.text.trim();
    final email = emailController.text.trim();
    final mname = mnameController.text.trim();
    final region = regionController.text.trim();

    if (mid.isEmpty || mpw.isEmpty || email.isEmpty || mname.isEmpty) {
      _showToast(context, '필수 항목(*)을 모두 입력하세요.');
      return;
    }

    _isLoading = true;
    notifyListeners();

    try {
      // 2. 프로필 이미지 base64 인코딩 (선택된 경우)
      String? profileImageBase64;
      if (_profileImageFile != null) {
        profileImageBase64 = await _encodeImageToBase64(_profileImageFile!);
      }

      // 3. 서버로 단일 요청 전송
      final response = await http.post(
        Uri.parse('${ApiConstants.springBaseUrl}/member/signup'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({
          'mid': mid,
          'mpw': mpw,
          'mpwConfirm': mpwConfirm,
          'mname': mname,
          'email': email,
          'region': region.isEmpty ? null : region,
          'profileImageBase64': profileImageBase64,
        }),
      );

      if (!context.mounted) return;

      // 4. 응답 결과 처리
      if (response.statusCode == 200 || response.statusCode == 201) {
        _showToast(context, '회원 가입 성공!');
        Future.delayed(const Duration(milliseconds: 800), () {
          if (context.mounted) Navigator.pushReplacementNamed(context, '/login');
        });
      } else {
        final Map<String, dynamic> responseData =
            jsonDecode(utf8.decode(response.bodyBytes));
        _showToast(
            context, '가입 실패: ${responseData['message'] ?? '오류가 발생했습니다.'}');
      }
    } catch (e) {
      if (!context.mounted) return;
      _showToast(context, '오류 발생: $e');
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  // ── 공통 UI 헬퍼 ──────────────────────────────────────────────────

  void _showDialog(BuildContext context, String title, String message) {
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: Text(title),
        content: Text(message),
        actions: [
          TextButton(
              onPressed: () => Navigator.of(ctx).pop(),
              child: const Text('확인')),
        ],
      ),
    );
  }

  void _showToast(BuildContext context, String message) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text(message), duration: const Duration(seconds: 2)),
    );
  }

  @override
  void dispose() {
    idController.dispose();
    emailController.dispose();
    passwordController.dispose();
    passwordConfirmController.dispose();
    mnameController.dispose();
    regionController.dispose();
    super.dispose();
  }
}
