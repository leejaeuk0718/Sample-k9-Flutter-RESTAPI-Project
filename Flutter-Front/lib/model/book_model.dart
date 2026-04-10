class BookModel {
  final int? id;
  final String? title;      // JSON의 'bookTitle'과 매핑
  final String? author;
  final String? publisher;
  final String? isbn;
  final String? status;     // AVAILABLE, RENTED, RESERVED, LOST
  final String? description;
  final String? coverImage;
  final String? publishDate;
  final String? regDate;

  BookModel({
    this.id,
    this.title,
    this.author,
    this.publisher,
    this.isbn,
    this.status,
    this.description,
    this.coverImage,
    this.publishDate,
    this.regDate,
  });

  factory BookModel.fromJson(Map<String, dynamic> json) {
    return BookModel(
      id: json['id'],
      title: json['bookTitle'], // 서버의 'bookTitle'을 title에 저장
      author: json['author'],
      publisher: json['publisher'],
      isbn: json['isbn'],
      status: json['status'],
      description: json['description'],
      coverImage: json['coverImage'],
      publishDate: json['publishDate'],
      regDate: json['regDate'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'bookTitle': title,      // 서버로 데이터를 보낼 때도 'bookTitle' 키를 사용하도록 수정
      'author': author,
      'publisher': publisher,
      'isbn': isbn,
      'status': status,
      'description': description,
      'coverImage': coverImage,
      'publishDate': publishDate,
      'regDate': regDate,
    };
  }
}