import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class Sample3ListOfListView extends StatelessWidget {
  const Sample3ListOfListView({super.key});
  ////////////////////////////////////////////
  void _showSnackBar(BuildContext context) {
    final snackBar = SnackBar(
      content: Text('스낵바가 표시되었습니다!!!!!!'),

      // 스낵바 안에 표시할 텍스트
      duration: Duration(seconds: 3),

      // 스낵바가 화면에 표시되는 시간
      backgroundColor: Colors.indigo,

      // 배경색 지정
      behavior: SnackBarBehavior.fixed,

      // fixed 또는 floating 설정 가능
      elevation: 6.0,

      // 그림자 깊이 (부유 느낌)
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

  ////////////////////////////////////////////

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      //오른쪽 상단의 디버그 화면을 제거
      debugShowCheckedModeBanner: false,
      home: Scaffold(
        // 이 위치에 플로팅 액션 버튼을 위치 하기.
        floatingActionButton: FloatingActionButton(
          onPressed: () {
            // 버튼 클릭 시 실행할 코드
          },
          child: Icon(Icons.add),
          // 버튼 내부 아이콘
          backgroundColor: Colors.blue,
          // 배경색
          tooltip: '추가',
          // 툴팁 텍스트 (길게 누를 때 표시)
          elevation: 6.0, // 그림자 깊이
        ),
        // floatingActionButtonLocation: FloatingActionButtonLocation.centerTop,
        body: ListView(
          children: [
            Builder(
              builder: (context) {
                return Wrap(
                  alignment: WrapAlignment.center,
                  children: [
                    OutlinedButton(
                      // 클릭 시 실행할 함수
                      onPressed: () => _showSnackBar(context),
                      // 버튼 스타일 지정
                      style: OutlinedButton.styleFrom(
                        foregroundColor: Colors.red,
                      ),
                      // 버튼에 넣을 위젯
                      child: Text('아웃라인드 버튼'),
                    ),
                  ],
                );
              },
            ),
            Builder(
              builder: (context) {
                return Wrap(
                  alignment: WrapAlignment.center,
                  children: [
                    ElevatedButton(
                      // 클릭 시 실행할 함수
                      onPressed: () => _showSnackBar(context),
                      // 버튼 스타일 지정
                      style: ElevatedButton.styleFrom(
                        backgroundColor: Colors.red,
                      ),
                      // 버튼에 넣을 위젯
                      child: Text('엘리베이티드 버튼'),
                    ),
                  ],
                );
              },
            ),
            Wrap(
              alignment: WrapAlignment.center,
              children: [
                IconButton(
                  onPressed: () {},
                  icon: Icon(
                    // ① 플러터에서 기본으로 제공하는 아이콘입니다.
                    // 제공되는 아이콘 목록은 다음 링크에서 확인해볼 수 있습니다.
                    // https://fonts.google.com/icons
                    Icons.home,
                  ),
                ),
              ],
            ),
            Wrap(
              alignment: WrapAlignment.center,
              children: [
                GestureDetector(
                  // 한 번 탭했을 때 실행할 함수
                  // onTap: () {
                  //   // 출력 결과는 안드로이드 스튜디오의 [Run] 탭에서 확인 가능합니다.
                  //   print('on tap');
                  // },
                  // // 두 번 탭했을 때 실행할 함수
                  // onDoubleTap: () {
                  //   print('on double tap');
                  // },
                  // // 길게 눌렀을 때 실행할 함수
                  // onLongPress: () {
                  //   print('on long press');
                  // },
                  onPanStart: (details) {
                    print('on onPanStart start');
                  },
                  onPanUpdate: (details) {
                    print('onPanUpdate ');
                  },
                  onPanEnd: (details) {
                    print('onPanEnd ');
                  },
                  // 제스처를 적용할 위젯
                  child: Container(
                    decoration: BoxDecoration(color: Colors.red),
                    width: 100.0,
                    height: 100.0,
                  ),
                ),
              ],
            ),
            Wrap(
              alignment: WrapAlignment.center,
              children: [
                Container(
                  // 스타일 적용
                  decoration: BoxDecoration(
                    // 배경색 적용
                    color: Colors.red,
                    // 테두리 적용
                    border: Border.all(
                      // 테두리 굵기
                      width: 16.0,
                      // 테두리 색상
                      color: Colors.black,
                    ),
                    // 모서리 둥글게 만들기
                    borderRadius: BorderRadius.circular(16.0),
                  ),
                  // 높이
                  height: 200.0,
                  // 너비
                  width: 100.0,
                ),
              ],
            ),
            SizedBox(height: 16),

            Wrap(
              alignment: WrapAlignment.center,
              children: [
                SizedBox(
                  // 높이 지정
                  height: 200.0,
                  // 너비 지정
                  width: 200.0,
                  // SizedBox는 색상이 없으므로 크기를 확인하는
                  // 용도로 Container 추가
                  child: Container(color: Colors.red),
                ),
              ],
            ),
            SizedBox(height: 16),
            Wrap(
              alignment: WrapAlignment.center,
              children: [
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
            SizedBox(height: 16),
            Wrap(
              alignment: WrapAlignment.center,
              children: [
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
                ),
              ],
            ),
            SizedBox(height: 16),
            Row(
              // 주축 정렬 지정
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              // 반대축 정렬 지정
              crossAxisAlignment: CrossAxisAlignment.center,
              // 넣고 싶은 위젯 입력
              children: [
                Container(height: 50.0, width: 50.0, color: Colors.red),
                // SizedBox는 일반적으로 공백을 생성할 때 사용
                SizedBox(width: 12.0),
                Container(height: 50.0, width: 50.0, color: Colors.green),
                SizedBox(width: 12.0),
                Container(height: 50.0, width: 50.0, color: Colors.blue),
              ],
            ),
            SizedBox(height: 16),
            Column(
              // 주축 정렬 지정
              mainAxisAlignment: MainAxisAlignment.start,
              // 반대축 정렬 지정
              crossAxisAlignment: CrossAxisAlignment.stretch,
              // 넣고 싶은 위젯 입력
              children: [
                Container(height: 50.0, width: 50.0, color: Colors.red),
                // SizedBox는 일반적으로 공백을 생성할 때 사용
                SizedBox(height: 12.0), // 공백 추가 (Column이므로 height 사용)
                Container(height: 50.0, width: 50.0, color: Colors.green),
                SizedBox(height: 12.0), // 공백 추가
                Container(height: 50.0, width: 50.0, color: Colors.blue),
              ],
            ),
            SizedBox(height: 30),
            // 중첩 리스트 뷰
            // 가로 방향으로 스크롤이 되는 위젯
            SizedBox(
              height: 200,
              child: ListView.builder(
                scrollDirection: Axis.horizontal,
                itemCount: 5,
                itemBuilder: (context, hIndex) {
                  return Container(
                    width: 200,
                    margin: EdgeInsets.all(16),
                    color: Colors.blueAccent,
                    alignment: Alignment.center,
                    child: Text('가로 $hIndex'),
                  );
                },
              ),
            ),
            SizedBox(height: 30),
            // 중첩 리스트 뷰
            // 가로 방향으로 스크롤이 되는 위젯
            SizedBox(
              height: 200,
              child: ListView.builder(
                scrollDirection: Axis.horizontal,
                itemCount: 5,
                itemBuilder: (context, hIndex) {
                  return Container(
                    width: 200,
                    margin: EdgeInsets.all(16),
                    color: Colors.redAccent,
                    alignment: Alignment.center,
                    child: Text('가로 $hIndex'),
                  );
                },
              ),
            ),
          ],
        ),
      ),
    );
  }
}