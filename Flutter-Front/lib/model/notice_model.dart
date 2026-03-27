class NoticeModel {
  final int? id;
  final String? title;
  final String? content;
  final String? writer;
  final String? regDate;
  final int? viewCount;

  NoticeModel({this.id, this.title, this.content, this.writer, this.regDate, this.viewCount});

  factory NoticeModel.fromJson(Map<String, dynamic> json) {
    return NoticeModel(
      id: json['id'],
      title: json['title'],
      content: json['content'],
      writer: json['writer'],
      regDate: json['regDate'],
      viewCount: json['viewCount'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'title': title,
      'content': content,
      'writer': writer,
      'regDate': regDate,
      'viewCount': viewCount,
    };
  }
}
