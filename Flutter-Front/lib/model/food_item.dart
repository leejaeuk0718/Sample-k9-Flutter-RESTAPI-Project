class FoodItem {
  final String? mainTitle;
  final String? title;
  final String? image;

  FoodItem({this.mainTitle, this.title, this.image});

  factory FoodItem.fromJson(Map<String, dynamic> json) {
    return FoodItem(
      mainTitle: json['MAIN_TITLE'],
      title: json['TITLE'],
      image: json['MAIN_IMG_NORMAL'],
    );
  }
}