import 'package:flutter/material.dart';

void main() {
  runApp(
    MaterialApp(
      home: Scaffold( // 앱 화면을 구성하는 기본 뼈대
        body : // 화면을 그리는 작업.
          Center( // 배치 해주는 위젯,
            child: Text(
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
          )
      ),
    )
  );
}
