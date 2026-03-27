import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:http/http.dart' as http;

import '../../model/food_item.dart';

class FoodController with ChangeNotifier {
  final List<FoodItem> _items = [];
  bool _isLoading = false;

  List<FoodItem> get items => _items;
  bool get isLoading => _isLoading;

  Future<void> fetchFoodData() async {
    _isLoading = true;
    notifyListeners();

    //serviceKey	인증키
    // numOfRows	한 페이지 결과 수
    // pageNo	페이지 번호
    // resultType	JSON방식 호출
    final queryParams ={
      'serviceKey': 'ALRX9GpugtvHxcIO/iPg1vXIQKi0E6Kk1ns4imt8BLTgdvSlH/AKv+A1GcGUQgzuzqM3Uv1ZGgpG5erOTDcYRQ==',
      'pageNo': '1',
      'numOfRows': '100',
      'resultType': 'json',
    };
//서비스 URL	http://apis.data.go.kr/6260000/FoodService
    // 부산맛집정보 서비스	getFoodKr	부산맛집 국문 정보
    final uri = Uri.https(
      'apis.data.go.kr',
      '/6260000/FoodService/getFoodKr',
      queryParams,
    );
    try {

      final response = await http.get(uri);
      if (response.statusCode == 200) {
        //서버에서 받은 바이트 데이터를 UTF-8로 디코딩한 후, JSON 파싱.
        // response.body 대신 bodyBytes를 쓰는 이유: 한글이나 특수문자 인코딩 오류 방지
        final decoded = jsonDecode(utf8.decode(response.bodyBytes));
        final dynamic foodData = decoded['getFoodKr'];

        if (foodData is Map<String, dynamic> && foodData['item'] is List) {
          final List<dynamic> itemList = foodData['item'];
          _items.clear();
          _items.addAll(itemList.map((e) => FoodItem.fromJson(e)).toList());
        } else {
          debugPrint('데이터 구조가 예상과 다릅니다: ${jsonEncode(foodData)}');
        }
      } else {
        debugPrint('서버 오류: ${response.statusCode}');
      }
    } catch (e) {
      debugPrint('데이터 로딩 실패: $e');
    }

    _isLoading = false;
    //ChangeNotifier를 통해 UI에 변경을 알림.
    notifyListeners();
  }
}