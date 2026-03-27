import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:provider/provider.dart';

import '../controller/auth/login_controller.dart';

class MainScreen2 extends StatefulWidget {
  const MainScreen2({super.key});

  @override
  State<MainScreen2> createState() => _MainScreen2State();
}

class _MainScreen2State extends State<MainScreen2> {
  // 보안 저장소에 저장된, 로그인 된 유저 정보를 가져오기 준비 작업.
  final FlutterSecureStorage secureStorage = const FlutterSecureStorage();

  // ✅ 서버 IP (SignupController와 동일하게 설정해야 합니다)
  final String serverIp = "http://10.100.201.87:8080"; // ‼️ localhost 대신 실제 IP 사용

  String? userId;
  String? profileImgId; // ✅ 프로필 이미지 ID를 저장할 변수

  @override
  void initState() {
    super.initState();
    // ✅ 함수 이름 변경
    _loadUserData();
  }

  // ✅ 보안 저장소에서 로그인한 유저 ID 및 프로필 이미지 ID 불러오기
  Future<void> _loadUserData() async {
    // 저장된 키 이름이 "profileImgId"가 맞는지 확인 필요 (로그인/회원가입 시 저장한 키)
    String? mid = await secureStorage.read(key: "mid");
    String? imgId = await secureStorage.read(key: "profileImg"); // ✅ 이미지 ID 가져오기

    setState(() {
      userId = mid;
      profileImgId = imgId; // ✅ 상태 변수에 저장
    });
  }

  @override
  Widget build(BuildContext context) {
    final loginController = context.watch<LoginController>();

    return Scaffold(
      appBar: AppBar(
        title: const Text('메인화면'),
        actions: [
          // 로그인 상태 일때만, 로그아웃 버튼 표시
          if (loginController.isLoggedIn)
            IconButton(
                onPressed: () => loginController.showLogoutDialog(context),
                icon: const Icon(Icons.logout))
        ],
      ),
      body: SafeArea(
          child: ListView(
            padding: const EdgeInsets.all(16),
            children: [
              // ✅ --- 프로필 이미지 표시 ---
              Center(
                child: CircleAvatar(
                  radius: 60,
                  backgroundColor: Colors.grey[200],
                  // profileImgId가 있으면 NetworkImage 로드, 없으면 기본 아이콘
                  // 예시
                  //http://10.100.201.87:8080/member/view/6902c22d2f5a26d3ccb68d47
                  backgroundImage: (profileImgId != null && profileImgId!.isNotEmpty)
                      ? NetworkImage("$serverIp/member/view/$profileImgId")
                      : null,
                  onBackgroundImageError: (profileImgId != null && profileImgId!.isNotEmpty)
                      ? (exception, stackTrace) {
                    print("프로필 이미지 로드 오류: $exception");
                    // ‼️ 에러 발생 시 (예: 이미지가 삭제되었거나) 기본 아이콘을 보여주기 위해
                    // setState(() { profileImgId = null; }); // <- 무한 루프 위험
                    // 대신, 이미지가 없는 것처럼 처리 (아래 child가 보이도록)
                  }
                      : null,
                  child: (profileImgId == null || profileImgId!.isEmpty)
                      ? Icon(
                    Icons.person,
                    size: 60,
                    color: Colors.grey[600],
                  )
                      : null,
                ),
              ),
              const SizedBox(height: 24),
              // --------------------------

              // 로그인한 유저의 상태를 표시 하는 화면을 구성.
              Center(
                child: Text(
                  userId != null ? "환영합니다, $userId님!" : "로그인이 필요합니다.",
                  style: const TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
                  textAlign: TextAlign.center,
                ),
              ),
              const SizedBox(height: 16),
              // const Center(child: FlutterLogo(size: 100)), // 로고는 잠시 주석 처리 (선택)
              const SizedBox(height: 16),
              ElevatedButton(
                  onPressed: () => Navigator.pushNamed(context, '/login'),
                  child: const Text('로그인')),

              ElevatedButton(
                //라우팅 2번 째 준비물,
                onPressed: () => Navigator.pushNamed(context, '/signup'),
                child: const Text('회원 가입'),
              ),

              if (loginController.isLoggedIn)
                ElevatedButton(
                  //라우팅 2번 째 준비물,
                  onPressed: () => Navigator.pushNamed(context, '/pdtest'),
                  child: const Text('부산 맛집 공공 데이터'),
                ),

              if (loginController.isLoggedIn)
                ElevatedButton(
                  onPressed: () => Navigator.pushNamed(context, '/sample_design'),
                  child: const Text('샘플 디자인1-중첩리스트'),
                ),
              if (loginController.isLoggedIn)
                ElevatedButton(
                  onPressed: () => Navigator.pushNamed(context, '/sample_design2'),
                  child: const Text('샘플 디자인2-탭모드'),
                ),
              if (loginController.isLoggedIn)
                ElevatedButton(
                  onPressed: () => Navigator.pushNamed(context, '/sample_design3'),
                  child: const Text('샘플 디자인3-드로워-네비게이션'),
                ),
              if (loginController.isLoggedIn)
                ElevatedButton(
                  onPressed: () => Navigator.pushNamed(context, '/todos'),
                  child: const Text('todos 일정'),
                ),
              if (loginController.isLoggedIn)
                ElevatedButton(
                  onPressed: () => Navigator.pushNamed(context, '/ai-image'),
                  child: const Text('Ai 이미지 테스트'),
                ),
              if (loginController.isLoggedIn)
                ElevatedButton(
                  onPressed: () => Navigator.pushNamed(context, '/ai-stock'),
                  child: const Text('Ai 주가 테스트'),
                ),
            ],
          )),
    );
  }
}
