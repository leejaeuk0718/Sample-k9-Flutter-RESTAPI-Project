import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../controller/auth/signup_controller.dart';

class SignupScreen extends StatelessWidget {
  const SignupScreen({super.key});

  OutlineInputBorder _passwordBorder(bool hasText, bool isMatch) {
    Color color = !hasText ? Colors.grey : (isMatch ? Colors.green : Colors.red);
    return OutlineInputBorder(borderSide: BorderSide(color: color, width: 2.0));
  }

  @override
  Widget build(BuildContext context) {
    final ctrl = context.watch<SignupController>();

    return Scaffold(
      appBar: AppBar(title: const Text('회원 가입')),
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: ListView(
            children: [
              // ── 프로필 이미지 선택 ──
              Center(
                child: Stack(
                  alignment: Alignment.bottomRight,
                  children: [
                    GestureDetector(
                      onTap: () => ctrl.showImagePickerDialog(context),
                      child: CircleAvatar(
                        radius: 55,
                        backgroundColor: Colors.grey[200],
                        backgroundImage: ctrl.profileImageFile != null
                            ? FileImage(ctrl.profileImageFile!)
                            : null,
                        child: ctrl.profileImageFile == null
                            ? const Icon(Icons.person,
                                size: 55, color: Colors.grey)
                            : null,
                      ),
                    ),
                    GestureDetector(
                      onTap: () => ctrl.showImagePickerDialog(context),
                      child: Container(
                        padding: const EdgeInsets.all(6),
                        decoration: BoxDecoration(
                          color: Colors.indigo,
                          shape: BoxShape.circle,
                          border: Border.all(color: Colors.white, width: 2),
                        ),
                        child: const Icon(Icons.camera_alt,
                            size: 18, color: Colors.white),
                      ),
                    ),
                  ],
                ),
              ),
              const SizedBox(height: 6),
              const Center(
                child: Text('프로필 이미지 선택 (선택, 5MB 이하)',
                    style: TextStyle(fontSize: 12, color: Colors.grey)),
              ),
              const SizedBox(height: 20),

              // ── 아이디 + 중복 체크 ──
              Row(
                children: [
                  Expanded(
                    child: TextField(
                      controller: ctrl.idController,
                      decoration: const InputDecoration(
                        labelText: '아이디 *',
                        border: OutlineInputBorder(),
                      ),
                    ),
                  ),
                  const SizedBox(width: 8),
                  ElevatedButton(
                    onPressed: () => ctrl.checkDuplicateId(context),
                    child: const Text('중복 체크'),
                  ),
                ],
              ),
              const SizedBox(height: 16),

              // ── 이름 ──
              TextField(
                controller: ctrl.mnameController,
                decoration: const InputDecoration(
                  labelText: '이름 *',
                  border: OutlineInputBorder(),
                ),
              ),
              const SizedBox(height: 16),

              // ── 이메일 ──
              TextField(
                controller: ctrl.emailController,
                keyboardType: TextInputType.emailAddress,
                decoration: const InputDecoration(
                  labelText: '이메일 *',
                  border: OutlineInputBorder(),
                ),
              ),
              const SizedBox(height: 16),

              // ── 지역 선택 ──
              DropdownButtonFormField<String>(
                value: ctrl.selectedRegion,
                decoration: const InputDecoration(
                  labelText: '지역 선택 *',
                  border: OutlineInputBorder(),
                  prefixIcon: Icon(Icons.map_outlined),
                ),
                hint: const Text('거주 지역을 선택하세요'),
                items: ctrl.regions.map((String region) {
                  return DropdownMenuItem<String>(
                    value: region,
                    child: Text(region),
                  );
                }).toList(),
                onChanged: (String? newValue) {
                  ctrl.setRegion(newValue);
                },
                validator: (value) => value == null ? '지역을 선택해주세요' : null,
              ),
              const SizedBox(height: 16),

              // ── 비밀번호 ──
              TextField(
                controller: ctrl.passwordController,
                obscureText: true,
                onChanged: (_) => ctrl.validatePassword(),
                decoration: InputDecoration(
                  labelText: '비밀번호 *',
                  enabledBorder: _passwordBorder(
                    ctrl.passwordController.text.isNotEmpty,
                    ctrl.isPasswordMatch,
                  ),
                  focusedBorder: _passwordBorder(
                    ctrl.passwordController.text.isNotEmpty,
                    ctrl.isPasswordMatch,
                  ),
                ),
              ),
              const SizedBox(height: 16),

              // ── 비밀번호 확인 ──
              TextField(
                controller: ctrl.passwordConfirmController,
                obscureText: true,
                onChanged: (_) => ctrl.validatePassword(),
                decoration: InputDecoration(
                  labelText: '비밀번호 확인 *',
                  enabledBorder: _passwordBorder(
                    ctrl.passwordConfirmController.text.isNotEmpty,
                    ctrl.isPasswordMatch,
                  ),
                  focusedBorder: _passwordBorder(
                    ctrl.passwordConfirmController.text.isNotEmpty,
                    ctrl.isPasswordMatch,
                  ),
                ),
              ),
              const SizedBox(height: 8),

              if (ctrl.passwordConfirmController.text.isNotEmpty)
                Text(
                  ctrl.isPasswordMatch ? '비밀번호가 일치합니다.' : '비밀번호가 일치하지 않습니다.',
                  style: TextStyle(
                    color: ctrl.isPasswordMatch ? Colors.green : Colors.red,
                    fontSize: 13,
                  ),
                ),
              const SizedBox(height: 24),

              // ── 가입 버튼 ──
              SizedBox(
                height: 50,
                child: ElevatedButton(
                  onPressed:
                      ctrl.isLoading ? null : () => ctrl.signup(context),
                  child: ctrl.isLoading
                      ? const SizedBox(
                          height: 24,
                          width: 24,
                          child: CircularProgressIndicator(strokeWidth: 2),
                        )
                      : const Text('회원 가입', style: TextStyle(fontSize: 16)),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
