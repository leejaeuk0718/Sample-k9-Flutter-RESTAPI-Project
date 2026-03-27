import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../../controller/pd_data/food_controller.dart';

class MyPdTestScreen extends StatelessWidget {
  const MyPdTestScreen({super.key});

  @override
  Widget build(BuildContext context) {
    // Provider 패턴을 이용해서, 데이터를 받아오면, 화면에 연동하는 작업,
    return ChangeNotifierProvider(
      create: (_) => FoodController()..fetchFoodData(),
      child: Scaffold(
        appBar: AppBar(title: const Text('부산 맛집 정보 서비스')),
        body: Consumer<FoodController>( // 컨트롤러로 부터 전달 받은 데이터를 , 화면에 반영하기.
          builder: (context, controller, _) {
            if (controller.isLoading) {
              return const Center(child: CircularProgressIndicator());
            }
            if (controller.items.isEmpty) {
              return const Center(child: Text('데이터가 없습니다.'));
            }
            return ListView.builder(
              itemCount: controller.items.length, // 전달 받은 데이터의 총 갯수
              itemBuilder: (context, index) { // 전달 받은 데이터를 화면에 반영하기.
                final item = controller.items[index]; //
                return Card(
                  margin: const EdgeInsets.all(10),
                  child: ListTile(
                    leading:
                    item.image != null
                        ? Image.network(
                      item.image!,
                      width: 80,
                      fit: BoxFit.cover,
                    )
                        : const Icon(Icons.image_not_supported),
                    title: Text(item.mainTitle ?? ""),
                    subtitle: Text(item.title ?? ""),
                  ),
                );
              },
            );
          },
        ),
      ),
    );
  }
}