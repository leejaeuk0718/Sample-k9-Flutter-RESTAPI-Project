class RentalModel {
  final int? id;
  final int? memberId;
  final int? bookId;
  final String? rentDate;
  final String? returnDate;
  final String? status; // RENTING, RETURNED, OVERDUE, EXTENDED

  RentalModel({this.id, this.memberId, this.bookId, this.rentDate, this.returnDate, this.status});

  factory RentalModel.fromJson(Map<String, dynamic> json) {
    return RentalModel(
      id: json['id'],
      memberId: json['memberId'],
      bookId: json['bookId'],
      rentDate: json['rentDate'],
      returnDate: json['returnDate'],
      status: json['status'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'memberId': memberId,
      'bookId': bookId,
      'rentDate': rentDate,
      'returnDate': returnDate,
      'status': status,
    };
  }
}
