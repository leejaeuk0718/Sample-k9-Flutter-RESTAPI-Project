class MemberModel {
  final int? id;
  final String? email;
  final String? name;
  final String? role;

  MemberModel({this.id, this.email, this.name, this.role});

  factory MemberModel.fromJson(Map<String, dynamic> json) {
    return MemberModel(
      id: json['id'],
      email: json['email'],
      name: json['name'],
      role: json['role'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'email': email,
      'name': name,
      'role': role,
    };
  }
}
