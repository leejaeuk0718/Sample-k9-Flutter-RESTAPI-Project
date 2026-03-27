import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

// 위젯을 분리 해보기.
// 1) 스테이트리스 위젯, -> 정적화면 예시) 나무 간판.
// 2) 상태를 관리하는 , 스테이트 풀 위젯. -> 동적 화면, 상태에 따라서 화면표기.
// 예시) 점수가 표시되는 간판.

class MyApp extends StatelessWidget {

  // SnackBar를 보여주는 함수
  void _showSnackBar(BuildContext context) {
    final snackBar = SnackBar(
      content: Text('스낵바가 표시되었습니다!'), // 스낵바 안에 표시할 텍스트

      duration: Duration(seconds: 3), // 스낵바가 화면에 표시되는 시간

      backgroundColor: Colors.indigo, // 배경색 지정

      behavior: SnackBarBehavior.fixed, // fixed 또는 floating 설정 가능

      elevation: 6.0, // 그림자 깊이 (부유 느낌)

      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(8.0), // 모서리 둥글게
      ),

      action: SnackBarAction(
        label: '클릭', // 버튼 텍스트
        textColor: Colors.yellow, // 텍스트 색상

        onPressed: () {
          // 클릭 이벤트 처리: 예를 들어 로그 출력
          print('SnackBar의 클릭 액션 실행됨');
        },
      ),
    );

    // ScaffoldMessenger를 통해 현재 context에 SnackBar 표시
    ScaffoldMessenger.of(context).showSnackBar(snackBar);
  }


  @override
  Widget build(BuildContext context) { // 그림을 그리는 도구.
    return MaterialApp( // 머터리얼 디자인 도구함.
      home: Scaffold( // 도화지, 빈 캔버스,
          body : ListView( // 여러 개의 위젯(화면, ui) 구성하는 목록.
            children: [ // 복수개 UI 구성 가능 <-> child 단수개의 UI 구성 가능.
                Column ( // Flex 세로방향으로 나란히 배치 비슷
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
                      Text('오늘 점심 뭐 먹지? 라면+밥'),
                      TextButton(
                        // 클릭 시 실행할 함수
                        onPressed: () {},
                        // 스타일 지정
                        style: TextButton.styleFrom(
                          // 주색상 지정
                          foregroundColor: Colors.red,
                        ),
                        // 버튼에 넣을 위젯
                        child: Text('텍스트 버튼'),
                      ),
                      OutlinedButton(
                        // 클릭 시 실행할 함수
                        onPressed: () {},
                        // 버튼 스타일 지정
                        style: OutlinedButton.styleFrom(
                          foregroundColor: Colors.red,
                        ),
                        // 버튼에 넣을 위젯
                        child: Text('아웃라인드 버튼'),
                      ),

// ElevatedButton 예제
                      ElevatedButton(
                        // 클릭 시 실행할 함수
                        onPressed: () {},
                        // 버튼 스타일 지정
                        style: ElevatedButton.styleFrom(
                          backgroundColor: Colors.red,
                        ),
                        // 버튼에 넣을 위젯
                        child: Text('엘리베이티드 버튼'),
                      ),
                      Center(
                        child: Builder(
                          builder: (context) => TextButton(
                            onPressed: () {
                              // 방법1
                              ScaffoldMessenger.of(context).showSnackBar(
                                const SnackBar(content: Text('스낵바가 표시되었습니다!')),
                              );
                              // 방법2
                              // _showSnackBar(context);
                            },
                            child: const Text('스낵바 보기 방법1'),
                          ),
                        ),
                      ),
                      Center(
                        child: Builder(
                          builder: (context) =>   ElevatedButton(
                            // 클릭 시 실행할 함수
                            onPressed: () {
                              // 방법2
                              _showSnackBar(context);
                            },
                            // 버튼 스타일 지정
                            style: ElevatedButton.styleFrom(
                              backgroundColor: Colors.red,
                            ),
                            // 버튼에 넣을 위젯
                            child: const Text('스낵바 보기 방법2'),
                          ),
                        ),
                      ),
                      Row(
                        children: [
                          IconButton(
                              onPressed: () {
                                print("아이콘 버튼 클릭 이벤트 확인");
                              },
                              icon: Icon(
                          // ① 플러터에서 기본으로 제공하는 아이콘입니다.
                          // 제공되는 아이콘 목록은 다음 링크에서 확인해볼 수 있습니다.
                          // https://fonts.google.com/icons
                                  Icons.home)),
                          IconButton(
                              onPressed: () {},
                              icon: Icon(
                                  Icons.favorite)),
                          IconButton(
                              onPressed: () {},
                              icon: Icon(
                                  Icons.dangerous)),
                          IconButton(
                              onPressed: () {},
                              icon: Icon(
                                  Icons.cabin))
                        ],
                      ),
                      IconButton(
                          onPressed: () {},
                          icon: Icon(
                              Icons.favorite))
                      ,
                      GestureDetector(
                        // 한 번 탭했을 때 실행할 함수
                        onTap: () {
                          // 출력 결과는 안드로이드 스튜디오의 [Run] 탭에서 확인 가능합니다.
                          print('on tap');
                        },
                        // 두 번 탭했을 때 실행할 함수
                        onDoubleTap: () {
                          print('on double tap');
                        },
                        // 길게 눌렀을 때 실행할 함수
                        onLongPress: () {
                          print('on long press');
                        },
                        onPanStart: (DragStartDetails details) {
                          print('#################onPanStart : ${details.globalPosition}');
                        },
                        onPanUpdate: (DragUpdateDetails details){
                          print('============================onPanUpdate : ${details.delta}');
                        },
                        // 제스처를 적용할 위젯
                        child: Container(
                          decoration: BoxDecoration(
                            color: Colors.blue,
                          ),
                          width: 100.0,
                          height: 100.0,
                        ),
                      ),
                      Container(
                        // 스타일 적용
                        decoration: BoxDecoration(
                          // 배경색 적용
                          color: Colors.red,
                          // 테두리 적용
                          border: Border.all(
                            // 테두리 굵기
                            width: 10.0,
                            // 테두리 색상
                            color: Colors.black,
                          ),
                          // 모서리 둥글게 만들기
                          borderRadius: BorderRadius.circular(16.0),
                        ),
                        // 높이
                        height: 100.0,
                        // 너비
                        width: 100.0,
                      ),
                      Container(
                        color: Colors.blue,
                        child: Padding(
                          // 상하, 좌우로 모두 16픽셀만큼 패딩 적용
                          padding: EdgeInsets.all(16.0),
                          child: Container(
                            color: Colors.red,
                            width: 50.0,
                            height: 50.0,
                          ),
                        ),
                      ),

                    ],
                  ),
              Container(
                color: Colors.blue,
                child: Padding(
                  // 상하, 좌우로 모두 16픽셀만큼 패딩 적용
                  padding: EdgeInsets.all(16.0),
                  child: Container(
                    color: Colors.red,
                    width: 50.0,
                    height: 50.0,
                  ),
                ),
              ),
              Wrap(
                alignment: WrapAlignment.center,
                children:[
                  Container(
                  color: Colors.blue,
                  child: Padding(
                    // 상하, 좌우로 모두 16픽셀만큼 패딩 적용
                    padding: EdgeInsets.all(16.0),
                    child: Container(
                      color: Colors.red,
                      width: 50.0,
                      height: 50.0,
                    ),
                  ),
                ),

        ]
              ),
              Wrap(
                  alignment: WrapAlignment.center,
                  children:[
                    Container(
                      color: Colors.black, // ③ 최상위 검정 컨테이너 (margin이 적용되는 대상)
                      child: Container(
                        color: Colors.blue, // ② 중간 파란 컨테이너

                        // 마진 적용 위치
                        margin: EdgeInsets.all(16.0),

                        // 패딩 적용
                        child: Padding(
                          padding: EdgeInsets.all(16.0),

                          // ① 패딩이 적용된 빨간 컨테이너
                          child: Container(
                            color: Colors.red,
                            width: 50.0,
                            height: 50.0,
                          ),
                        ),
                      ),
                    )
                  ]
              )

            ],
          ),


          floatingActionButton: FloatingActionButton(
          onPressed: () {
      // 버튼 클릭 시 실행할 코드
    },
      child: Icon(Icons.add), // 버튼 내부 아이콘
      backgroundColor: Colors.blue, // 배경색
      tooltip: '추가', // 툴팁 텍스트 (길게 누를 때 표시)
      elevation: 20.0, // 그림자 깊이
    ),
        floatingActionButtonLocation: FloatingActionButtonLocation.endFloat,
      ),
    );
  }
}