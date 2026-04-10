import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:http/http.dart' as http;
import 'package:provider/provider.dart';

import '../../const/api_constants.dart';
import '../../controller/auth/login_controller.dart';

/// 내 정보 수정 화면
/// - 이름, 이메일, 거주 지역 수정
/// - PUT /api/member/update 호출
class EditProfileScreen extends StatefulWidget {
  const EditProfileScreen({super.key});

  @override
  State<EditProfileScreen> createState() => _EditProfileScreenState();
}

class _EditProfileScreenState extends State<EditProfileScreen> {
  final _mnameCtrl = TextEditingController();
  final _emailCtrl = TextEditingController();
  String? _selectedRegion;
  bool _isLoading = false;

  final List<String> _regions = [
    '서울특별시', '부산광역시', '대구광역시', '인천광역시',
    '광주광역시', '대전광역시', '울산광역시', '세종특별자치시',
    '경기도', '강원특별자치도', '충청북도', '충청남도',
    '전북특별자치도', '전라남도', '경상북도', '경상남도', '제주특별자치도'
  ];

  @override
  void initState() {
    super.initState();
    // 현재 회원 정보로 초기값 설정
    final ctrl = context.read<LoginController>();
    _mnameCtrl.text = ctrl.memberName ?? '';
    _emailCtrl.text = ctrl.memberEmail ?? '';
    // 지역이 목록에 있으면 선택, 없으면 null
    final region = ctrl.memberRegion;
    if (region != null && _regions.contains(region)) {
      _selectedRegion = region;
    }
  }

  @override
  void dispose() {
    _mnameCtrl.dispose();
    _emailCtrl.dispose();
    super.dispose();
  }

  Future<void> _submit() async {
    final mname = _mnameCtrl.text.trim();
    final email = _emailCtrl.text.trim();

    if (mname.isEmpty || email.isEmpty) {
      _showSnack('이름과 이메일을 입력하세요.');
      return;
    }

    setState(() => _isLoading = true);

    try {
      final storage = const FlutterSecureStorage();
      final token = await storage.read(key: 'accessToken');
      final mid = await storage.read(key: 'mid');

      if (token == null || mid == null) {
        _showSnack('로그인 정보가 없습니다. 다시 로그인하세요.');
        return;
      }

      final response = await http.put(
        Uri.parse('${ApiConstants.springBaseUrl}/member/update'),
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer $token',
        },
        body: jsonEncode({
          'mid': mid,
          'mname': mname,
          'email': email,
          'region': _selectedRegion,
        }),
      );

      if (!mounted) return;

      if (response.statusCode == 200) {
        // LoginController 캐시 갱신
        await context.read<LoginController>().loadMemberInfo();
        _showSnack('회원 정보가 수정되었습니다.');
        Future.delayed(const Duration(milliseconds: 800), () {
          if (mounted) Navigator.pop(context);
        });
      } else {
        final data = jsonDecode(utf8.decode(response.bodyBytes));
        _showSnack('수정 실패: ${data['message'] ?? '오류가 발생했습니다.'}');
      }
    } catch (e) {
      if (!mounted) return;
      _showSnack('오류: $e');
    } finally {
      if (mounted) setState(() => _isLoading = false);
    }
  }

  void _showSnack(String msg) {
    ScaffoldMessenger.of(context)
        .showSnackBar(SnackBar(content: Text(msg)));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('내 정보 수정'), centerTitle: true),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(20),
        child: Column(
          children: [
            // 이름
            TextField(
              controller: _mnameCtrl,
              decoration: const InputDecoration(
                labelText: '이름 *',
                border: OutlineInputBorder(),
                prefixIcon: Icon(Icons.person_outline),
              ),
            ),
            const SizedBox(height: 16),

            // 이메일
            TextField(
              controller: _emailCtrl,
              keyboardType: TextInputType.emailAddress,
              decoration: const InputDecoration(
                labelText: '이메일 *',
                border: OutlineInputBorder(),
                prefixIcon: Icon(Icons.email_outlined),
              ),
            ),
            const SizedBox(height: 16),

            // 지역 선택
            DropdownButtonFormField<String>(
              value: _selectedRegion,
              decoration: const InputDecoration(
                labelText: '지역 선택',
                border: OutlineInputBorder(),
                prefixIcon: Icon(Icons.map_outlined),
              ),
              hint: const Text('거주 지역을 선택하세요'),
              items: _regions.map((r) {
                return DropdownMenuItem(value: r, child: Text(r));
              }).toList(),
              onChanged: (v) => setState(() => _selectedRegion = v),
            ),
            const SizedBox(height: 32),

            // 저장 버튼
            SizedBox(
              width: double.infinity,
              height: 50,
              child: ElevatedButton(
                onPressed: _isLoading ? null : _submit,
                child: _isLoading
                    ? const SizedBox(
                        height: 22,
                        width: 22,
                        child: CircularProgressIndicator(strokeWidth: 2))
                    : const Text('저장하기', style: TextStyle(fontSize: 16)),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
