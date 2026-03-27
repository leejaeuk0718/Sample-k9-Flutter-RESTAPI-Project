import 'package:flutter_test/flutter_test.dart';
import 'package:busanit501_flutter_workspace_251021/const/api_constants.dart';
import 'package:busanit501_flutter_workspace_251021/const/app_colors.dart';
import 'package:flutter/material.dart';

void main() {
  group('Constants Test', () {
    test('API Constants should not be empty', () {
      expect(ApiConstants.springBaseUrl.isNotEmpty, true);
      expect(ApiConstants.flaskBaseUrl.isNotEmpty, true);
    });

    test('App Colors should define basic theme colors', () {
      expect(AppColors.primary, isA<Color>());
      expect(AppColors.background, isA<Color>());
    });
  });
}
