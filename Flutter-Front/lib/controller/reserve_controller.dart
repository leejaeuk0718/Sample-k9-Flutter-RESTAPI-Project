import 'package:flutter/material.dart';
import '../model/apply_model.dart';

/// 도서관 시설(스터디룸 등)의 예약 신청 데이터를 관리하는 컨트롤러
class ReserveController extends ChangeNotifier {
  List<ApplyModel> _reservationList = [];
  List<ApplyModel> get reservationList => _reservationList;

  bool _isLoading = false;
  bool get isLoading => _isLoading;

  /// 시설 예약 내역 조회
  Future<void> fetchReservations() async {
    _isLoading = true;
    notifyListeners(); // 달력이나 목록 로더 표시

    try {
      // TODO: 예약 리스트 패치 (Spring API - ApplyController 매핑)
      await Future.delayed(const Duration(seconds: 1));
      _reservationList = [];
    } catch (e) {
      print('예약 목록 불러오기 에러: $e');
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }
}
