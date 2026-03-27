import 'dart:io'; // FileImage를 사용하기 위해 추가
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../controller/auth/signup_controller.dart';
import 'package:image_picker/image_picker.dart'; // ImageSource를 사용하기 위해 추가

class SignupScreen extends StatelessWidget {
  const SignupScreen({super.key});

  // 카메라/갤러리 선택 바텀 시트 표시
  void _showImageSourceActionSheet(BuildContext context) {
    // context.read는 이벤트 핸들러 내에서 상태를 변경할 때 사용
    final signupController = context.read<SignupController>();

    showModalBottomSheet(
      context: context,
      builder: (BuildContext sheetContext) {
        return SafeArea(
          child: Wrap(
            children: <Widget>[
              ListTile(
                leading: const Icon(Icons.camera_alt),
                title: const Text('카메라'),
                onTap: () {
                  Navigator.of(sheetContext).pop();
                  signupController.pickImage(ImageSource.camera);
                },
              ),
              ListTile(
                leading: const Icon(Icons.photo_library),
                title: const Text('갤러리'),
                onTap: () {
                  Navigator.of(sheetContext).pop();
                  signupController.pickImage(ImageSource.gallery);
                },
              ),
            ],
          ),
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    // context.watch는 UI 빌드 시 상태를 읽고, 변경 시 리빌드할 때 사용
    final signupController = context.watch<SignupController>();

    return Scaffold(
      appBar: AppBar(title: const Text('회원 가입')),
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: ListView(
            children: [
              // ----- 프로필 이미지 선택 UI 추가 -----
              Center(
                child: GestureDetector(
                  onTap: () => _showImageSourceActionSheet(context),
                  child: CircleAvatar(
                    radius: 60,
                    backgroundColor: Colors.grey[200],
                    // 선택된 이미지가 있으면 보여주고, 없으면 아이콘 표시
                    backgroundImage: signupController.pickedImage != null
                        ? FileImage(File(signupController.pickedImage!.path))
                        : null,
                    child: signupController.pickedImage == null
                        ? Icon(
                      Icons.camera_alt,
                      color: Colors.grey[800],
                      size: 40,
                    )
                        : null,
                  ),
                ),
              ),
              const SizedBox(height: 24),

              // 아이디 입력 필드 + 중복 체크 버튼
              Row(
                children: [
                  Expanded(
                    child: TextField(
                      controller: signupController.idController,
                      decoration: const InputDecoration(labelText: '아이디'),
                    ),
                  ),
                  const SizedBox(width: 10),
                  ElevatedButton(
                    onPressed: () => signupController.checkDuplicateId(context),
                    child: const Text('중복 체크'),
                  ),
                ],
              ),
              const SizedBox(height: 16),

              // 이메일 입력 필드
              TextField(
                controller: signupController.emailController,
                decoration: const InputDecoration(labelText: '이메일'),
                keyboardType: TextInputType.emailAddress, // 이메일 키보드 타입
              ),
              const SizedBox(height: 16),

              // 패스워드 입력 필드 (기존 코드)
              TextField(
                controller: signupController.passwordController,
                obscureText: true,
                decoration: InputDecoration(
                  labelText: '패스워드',
                  enabledBorder: OutlineInputBorder(
                    borderSide: BorderSide(
                      color: signupController.passwordController.text.isNotEmpty
                          ? (signupController.isPasswordMatch
                          ? Colors.green
                          : Colors.red)
                          : Colors.grey,
                      width: 2.0,
                    ),
                  ),
                  focusedBorder: OutlineInputBorder(
                    borderSide: BorderSide(
                      color: signupController.passwordController.text.isNotEmpty
                          ? (signupController.isPasswordMatch
                          ? Colors.green
                          : Colors.red)
                          : Colors.grey,
                      width: 2.0,
                    ),
                  ),
                ),
                onChanged: (value) => signupController.validatePassword(),
              ),
              const SizedBox(height: 16),

              // 패스워드 확인 입력 필드 (기존 코드)
              TextField(
                controller: signupController.passwordConfirmController,
                obscureText: true,
                decoration: InputDecoration(
                  labelText: '패스워드 확인',
                  enabledBorder: OutlineInputBorder(
                    borderSide: BorderSide(
                      color: signupController
                          .passwordConfirmController.text.isNotEmpty
                          ? (signupController.isPasswordMatch
                          ? Colors.green
                          : Colors.red)
                          : Colors.grey,
                      width: 2.0,
                    ),
                  ),
                  focusedBorder: OutlineInputBorder(
                    borderSide: BorderSide(
                      color: signupController
                          .passwordConfirmController.text.isNotEmpty
                          ? (signupController.isPasswordMatch
                          ? Colors.green
                          : Colors.red)
                          : Colors.grey,
                      width: 2.0,
                    ),
                  ),
                ),
                onChanged: (value) => signupController.validatePassword(),
              ),

              const SizedBox(height: 16),

              // 회원 가입 버튼
              ElevatedButton(
                onPressed: () => signupController.signup(context),
                child: const Text('회원 가입'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}