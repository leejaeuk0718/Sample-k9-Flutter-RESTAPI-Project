import 'package:flutter_test/flutter_test.dart';
import 'package:busanit501_flutter_workspace_251021/model/member_model.dart';
import 'package:busanit501_flutter_workspace_251021/model/book_model.dart';

void main() {
  group('Model JSON Serialization Tests', () {
    test('MemberModel should serialize and deserialize correctly', () {
      final json = {
        'id': 1,
        'email': 'test@example.com',
        'name': 'Hong',
        'role': 'USER',
      };

      final member = MemberModel.fromJson(json);

      expect(member.id, 1);
      expect(member.name, 'Hong');

      final serialized = member.toJson();
      expect(serialized['email'], 'test@example.com');
      expect(serialized['role'], 'USER');
    });

    test('BookModel should serialize and deserialize correctly', () {
      final json = {
        'id': 100,
        'title': 'Flutter Basics',
        'author': 'Doe',
        'publisher': 'IT Pub',
        'isbn': '1234567890',
        'status': 'AVAILABLE',
      };

      final book = BookModel.fromJson(json);

      expect(book.id, 100);
      expect(book.title, 'Flutter Basics');
      expect(book.status, 'AVAILABLE');

      final serialized = book.toJson();
      expect(serialized['author'], 'Doe');
    });
  });
}
