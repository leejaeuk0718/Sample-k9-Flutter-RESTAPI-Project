import 'package:flutter/material.dart';
import '../model/event_model.dart';

/// 도서관 행사 목록 및 참가 신청을 관리하는 컨트롤러
class EventController extends ChangeNotifier {
  // 행사 리스트 상태
  List<LibraryEventModel> _eventList = [];
  List<LibraryEventModel> get eventList => _eventList;

  bool _isLoading = false;
  bool get isLoading => _isLoading;

  /// 이달의 행사 목록을 조회
  Future<void> fetchEvents() async {
    _isLoading = true;
    notifyListeners(); // 이벤트 리스트 구성 시 화면 로딩 상태 전환

    try {
      // TODO: Spring 행사 API 데이터 패치 로직
      await Future.delayed(const Duration(seconds: 1));
      _eventList = [];
    } catch (e) {
      print('행사 리스트 불러오기 에러: $e');
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }
}
