class BookModel {
  final int? id;
  final String? title;
  final String? author;
  final String? publisher;
  final String? isbn;
  final String? status; // AVAILABLE, RENTED, RESERVED, LOST

  BookModel({this.id, this.title, this.author, this.publisher, this.isbn, this.status});

  factory BookModel.fromJson(Map<String, dynamic> json) {
    return BookModel(
      id: json['id'],
      title: json['title'],
      author: json['author'],
      publisher: json['publisher'],
      isbn: json['isbn'],
      status: json['status'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'title': title,
      'author': author,
      'publisher': publisher,
      'isbn': isbn,
      'status': status,
    };
  }
}
