import 'package:flutter/material.dart';
import '../model/rental_model.dart';

/// 회원의 도서 대여/반납 상태를 추적 및 관리하는 컨트롤러
class RentalController extends ChangeNotifier {
  // 회원의 대여 기록을 저장하는 리스트
  List<RentalModel> _rentalList = [];
  List<RentalModel> get rentalList => _rentalList;

  bool _isLoading = false;
  bool get isLoading => _isLoading;

  /// 특정 회원의 대여 목록을 불러오는 메서드 (샘플 로직)
  Future<void> fetchMemberRentals(int memberId) async {
    _isLoading = true;
    notifyListeners();

    try {
      // TODO: API 호출 로직 추가 (api_constants.dart 사용 예정)
      // fetch 데이터를 받아와 _rentalList에 매핑하는 과정
      await Future.delayed(const Duration(seconds: 1)); // 통신 시뮬레이션
      _rentalList = []; 
    } catch (e) {
      print('대여 목록 불러오기 에러: $e');
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }
}
