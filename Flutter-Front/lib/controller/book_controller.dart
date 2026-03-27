import 'package:flutter/material.dart';
import '../model/book_model.dart';
import '../const/api_constants.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

/// 도서 검색 및 상세 정보를 관리하는 상태 관리 컨트롤러 (Provider)
class BookController extends ChangeNotifier {
  // 상태 변수: 도서 목록 데이터 보관
  List<BookModel> _bookList = [];
  List<BookModel> get bookList => _bookList;

  // 상태 변수: 로딩 상태 표시에 사용
  bool _isLoading = false;
  bool get isLoading => _isLoading;

  /// 도서 목록을 서버에서 불러오는 람다형 비동기 함수
  Future<void> fetchBooks() async {
    _isLoading = true;
    notifyListeners(); // UI 렌더링(로딩바 표시 등)을 위해 리스너 알림

    try {
      final url = Uri.parse('${ApiConstants.springBaseUrl}/book/list');
      final response = await http.get(url);

      if (response.statusCode == 200) {
        // 서버 응답이 정상이면 JSON 디코딩 후 객체로 변환
        final List<dynamic> jsonList = jsonDecode(response.body);
        _bookList = jsonList.map((json) => BookModel.fromJson(json)).toList();
      } else {
        // 실패 시 빈 목록 처리
        _bookList = [];
      }
    } catch (e) {
      print('도서 불러오기 에러: $e');
    } finally {
      // 데이터 호출 완료 후 로딩 상태 해제
      _isLoading = false;
      notifyListeners(); // 화면에 데이터 반영을 위해 재알림
    }
  }
}
