import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:http_parser/http_parser.dart'; // MediaType을 위해 추가
import 'package:image_picker/image_picker.dart'; // ✅ 이미지 피커 추가
import 'package:path/path.dart' as p; // ✅ path 패키지 추가 (파일명 추출용)

class SignupController extends ChangeNotifier {
  // 입력 필드 컨트롤러
  final TextEditingController idController = TextEditingController();
  final TextEditingController emailController = TextEditingController();
  final TextEditingController passwordController = TextEditingController();
  final TextEditingController passwordConfirmController = TextEditingController();

  // --- 상태 변수 수정 ---
  bool _isPasswordMatch = false; // ✅ private으로 변경
  bool get isPasswordMatch => _isPasswordMatch; // ✅ getter 추가

  // ✅ --- 이미지 관련 속성 추가 ---
  XFile? _pickedImage;
  XFile? get pickedImage => _pickedImage;
  final ImagePicker _picker = ImagePicker();
  // -------------------------

  final String serverIp = "http://10.100.201.87:8080"; // 서버 주소 변경 필요
  // final String serverIp = "http://192.168.219.103:8080"; // 서버 주소 변경 필요

  // 패스워드 일치 여부 검사
  void validatePassword() {
    // ✅ _isPasswordMatch로 업데이트
    _isPasswordMatch = (passwordController.text.isNotEmpty &&
        passwordController.text == passwordConfirmController.text);
    notifyListeners();
  }

  // 다이얼로그 표시 (기존과 동일)
  void showDialogMessage(BuildContext context, String title, String message) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text(title),
          content: Text(message),
          actions: [
            TextButton(
              onPressed: () => Navigator.of(context).pop(),
              child: const Text("확인"),
            ),
          ],
        );
      },
    );
  }

  // 토스트 메시지 표시 (기존과 동일)
  void showToast(BuildContext context, String message) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text(message),
        duration: const Duration(seconds: 1),
      ),
    );
  }

  // 아이디 중복 체크 기능 (기존과 동일)
  Future<void> checkDuplicateId(BuildContext context) async {
    String inputId = idController.text.trim();
    if (inputId.isEmpty) {
      showDialogMessage(context, "오류", "아이디를 입력하세요.");
      return;
    }

    try {
      final response = await http.get(
        Uri.parse("$serverIp/member/check-mid?mid=$inputId"),
      );

      if (response.statusCode == 200) {
        showDialogMessage(context, "사용 가능", "이 아이디는 사용할 수 있습니다.");
      } else if (response.statusCode == 409) {
        showDialogMessage(context, "중복된 아이디", "이미 사용 중인 아이디입니다.");
      } else {
        showDialogMessage(context, "오류", "서버 응답 오류: ${response.statusCode}");
      }
    } catch (e) {
      showDialogMessage(context, "오류", "네트워크 오류 발생: $e");
    }
  }

  // ✅ --- 이미지 선택 함수 추가 ---
  Future<void> pickImage(ImageSource source) async {
    try {
      final XFile? image = await _picker.pickImage(source: source, imageQuality: 80);
      if (image != null) {
        _pickedImage = image;
        notifyListeners(); // 이미지 선택 UI 업데이트
      }
    } catch (e) {
      print("이미지 선택 오류: $e");
      // (필요 시 사용자에게 Toast/Dialog 표시)
    }
  }
  // ------------------------------

  // ✅ --- 회원 가입 요청 (수정됨) ---
  Future<void> signup(BuildContext context) async {
    if (!_isPasswordMatch) { // ✅ _isPasswordMatch 사용
      showDialogMessage(context, "오류", "패스워드가 일치해야 합니다.");
      return;
    }

    String inputId = idController.text.trim();
    String inputPw = passwordController.text.trim();
    String inputEmail = emailController.text.trim(); // ✅ 이메일 추가

    // ✅ 이메일 필드까지 검사
    if (inputId.isEmpty || inputPw.isEmpty || inputEmail.isEmpty) {
      showToast(context, "모든 필드를 입력하세요.");
      return;
    }

    // ✅ userData에 email 추가
    Map<String, String> userData = {
      "mid": inputId,
      "mpw": inputPw,
      "email": inputEmail,
    };

    try {
      var uri = Uri.parse("$serverIp/member/register");
      var request = http.MultipartRequest("POST", uri);

      // 1. JSON 데이터를 'user' 파트로 추가
      request.files.add(
        http.MultipartFile.fromString(
          'user', // Spring @RequestPart("user")
          jsonEncode(userData),
          contentType: MediaType('application', 'json'),
        ),
      );

      // ✅ 2. (수정) 이미지 파일 파트 ('profileImage')
      if (_pickedImage != null) {
        request.files.add(
          await http.MultipartFile.fromPath(
            'profileImage', // Spring @RequestPart("profileImage")
            _pickedImage!.path,
            filename: p.basename(_pickedImage!.path), // 파일명 추가
          ),
        );
      }

      // 3. 요청 전송
      var streamedResponse = await request.send();
      final response = await http.Response.fromStream(streamedResponse);

      if (response.statusCode == 200) {
        showToast(context, "회원 가입 성공!");
        Future.delayed(const Duration(seconds: 1), () {
          Navigator.pushReplacementNamed(context, "/main");
        });
      } else {
        final responseBody = utf8.decode(response.bodyBytes);
        showToast(context, "회원 가입 실패: $responseBody");
      }
    } catch (e) {
      showToast(context, "오류 발생: $e");
    }
  }

  // ✅ --- dispose 함수 추가 ---
  @override
  void dispose() {
    idController.dispose();
    emailController.dispose();
    passwordController.dispose();
    passwordConfirmController.dispose();
    super.dispose();
  }
}
