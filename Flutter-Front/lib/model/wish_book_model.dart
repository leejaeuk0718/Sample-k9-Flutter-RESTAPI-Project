class WishBookModel {
  final int? id;
  final String? title;
  final String? author;
  final String? reason;
  final String? status;
  final String? regDate;

  WishBookModel({this.id, this.title, this.author, this.reason, this.status, this.regDate});

  factory WishBookModel.fromJson(Map<String, dynamic> json) {
    return WishBookModel(
      id: json['id'],
      title: json['title'],
      author: json['author'],
      reason: json['reason'],
      status: json['status'],
      regDate: json['regDate'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'title': title,
      'author': author,
      'reason': reason,
      'status': status,
      'regDate': regDate,
    };
  }
}
