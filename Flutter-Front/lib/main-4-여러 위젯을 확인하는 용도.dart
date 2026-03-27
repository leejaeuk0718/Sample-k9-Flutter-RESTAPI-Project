import 'package:flutter/material.dart';

void main() {
  runApp(
    MaterialApp(
      home: Scaffold(
        body : SizedBox( // 크기가 정해진 상자, 1) 빈공간 만들기 2) 자식 위젯의 크기 강제할 때
          width: 200.0,
          child: Column ( // Flex 세로방향으로 나란히 여러개 배치 가능. 비슷
            mainAxisAlignment: MainAxisAlignment.center, // 세로 방향 주축.
            children: [
              Container(
                width: 150,
                height: 100,
                color: Colors.blue,
              ),
              Text('오늘 점심 뭐 먹지'),
              Text(
                // "헬로우 busanit 501, 플러터 앱 화면 작업 시작."
                // 작성하고 싶은 글
                  ' 헬로우 busanit 501, 오늘점심뭐 먹지', // 글자에 스타일 적용
                  style: TextStyle(
// 글자 크기
                    fontSize: 16.0,
// 글자 굵기
                    fontWeight: FontWeight.w700,
// 글자 색상
                    color: Colors.blue,
                  )
              ),
            ],
          )
        )
      ),
    )
  );
}
