import 'package:flutter/material.dart';

void main() {
  runApp( MyApp() ); // runApp
} // main()

// 위젯을 분리 해보기.
// 1) 스테이트리스 위젯, -> 정적화면 예시) 나무 간판.
// 2) 상태를 관리하는 , 스테이트 풀 위젯. -> 동적 화면, 상태에 따라서 화면표기.
// 예시) 점수가 표시되는 간판.
class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
   return MaterialApp(
     home: Scaffold(
         body : SizedBox(
             width: double.infinity,
             child: Column ( // Flex 세로방향으로 나란히 배치 비슷
               mainAxisAlignment: MainAxisAlignment.center,
               children: [
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
   );
  }

}
