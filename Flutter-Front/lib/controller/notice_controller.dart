import 'package:flutter/material.dart';
import '../model/notice_model.dart';

/// 공지사항 게시판의 데이터를 관리하는 컨트롤러
class NoticeController extends ChangeNotifier {
  // 공지사항 최근 목록 저장
  List<NoticeModel> _noticeList = [];
  List<NoticeModel> get noticeList => _noticeList;

  bool _isLoading = false;
  bool get isLoading => _isLoading;

  /// 공지사항 리스트 페칭 메서드
  Future<void> fetchNotices() async {
    _isLoading = true;
    notifyListeners(); // View에 State 변경을 전파

    try {
      // TODO: Spring Boot API 연동 (NoticeController에서 DTO 매핑)
      await Future.delayed(const Duration(seconds: 1)); 
      _noticeList = [];
    } catch (e) {
      print('공지사항 불러오기 에러: $e');
    } finally {
      _isLoading = false;
      notifyListeners(); // 로딩 종료 처리 후 UI 렌더링
    }
  }
}
