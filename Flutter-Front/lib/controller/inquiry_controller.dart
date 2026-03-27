import 'package:flutter/material.dart';
import '../model/inquiry_model.dart';

/// 사용자 1:1 문의사항 데이터와 전송 기능을 관리하는 컨트롤러
class InquiryController extends ChangeNotifier {
  List<InquiryModel> _inquiryList = [];
  List<InquiryModel> get inquiryList => _inquiryList;

  bool _isLoading = false;
  bool get isLoading => _isLoading;

  /// 사용자가 작성한 문의를 서버로 전송
  Future<bool> postInquiry(InquiryModel inquiry) async {
    _isLoading = true;
    notifyListeners(); // 작성간 버튼 비활성화 상태 트리거
    
    bool isSuccess = false;
    try {
      // TODO: Spring boot로 Post Method 전송 로직 구성
      await Future.delayed(const Duration(seconds: 1));
      isSuccess = true;
    } catch (e) {
      print('문의 작성 에러: $e');
    } finally {
      _isLoading = false;
      notifyListeners();
    }
    return isSuccess;
  }
}
