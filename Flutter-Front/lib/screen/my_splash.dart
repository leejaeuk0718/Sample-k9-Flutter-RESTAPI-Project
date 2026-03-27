import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

// 코드 스니펫으로 : 단축 : stl -> stless 선택
class MySplash extends StatelessWidget {
  const MySplash({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      // 오른쪽 상단에 debug 문구를 제거.
      debugShowCheckedModeBanner: false,
      home: Scaffold(
        body: Container(
          decoration: BoxDecoration(
            color: Colors.amberAccent
          ),
          // 간단 구성 1) 문자열 2) 이미지 구성.
          child: Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Text(
                    style: TextStyle(
                      fontSize: 32,
                      fontWeight: FontWeight.w800
                    ),
                    '나의 첫 Splash 화면'
                  ),
                  Image.asset('assets/images/logo.jpg',width: 400,),
                  // 공간 여백 잡는 위젯을 사용.
                  SizedBox(height: 16,),
                  CircularProgressIndicator(
                    valueColor: AlwaysStoppedAnimation(
                      Colors.white
                    ),
                  )
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }
}
