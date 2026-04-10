import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../controller/book_controller.dart';
import '../../controller/rental_controller.dart';

/// 도서 상세 정보 화면
class BookDetailScreen extends StatefulWidget {
  const BookDetailScreen({super.key});

  @override
  State<BookDetailScreen> createState() => _BookDetailScreenState();
}

class _BookDetailScreenState extends State<BookDetailScreen> {
  int? _bookId;

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    final args = ModalRoute.of(context)?.settings.arguments;
    if (args is int && args != _bookId) {
      _bookId = args;
      Future.microtask(() {
        if (context.mounted) {
          context.read<BookController>().fetchBookById(_bookId!);
        }
      });
    }
  }

  Future<void> _rentBook() async {
    if (_bookId == null) return;

    final confirmed = await showDialog<bool>(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text('대여 신청'),
        content: const Text('이 도서를 대여 신청하시겠습니까?'),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(ctx, false),
            child: const Text('취소'),
          ),
          TextButton(
            onPressed: () => Navigator.pop(ctx, true),
            child: const Text('신청', style: TextStyle(color: Colors.blue)),
          ),
        ],
      ),
    );

    if (confirmed != true || !context.mounted) return;

    final success = await context.read<RentalController>().rentBook(_bookId!);

    if (!context.mounted) return;

    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text(success ? '대여 신청이 완료되었습니다.' : '대여 신청에 실패했습니다. 다시 시도해주세요.'),
        backgroundColor: success ? Colors.green : Colors.red,
      ),
    );

    if (success) {
      context.read<BookController>().fetchBookById(_bookId!);
    }
  }

  Color _statusColor(String? status) {
    switch (status) {
      case 'AVAILABLE': return Colors.green;
      case 'RENTED': return Colors.red;
      case 'RESERVED': return Colors.orange;
      default: return Colors.grey;
    }
  }

  String _statusLabel(String? status) {
    switch (status) {
      case 'AVAILABLE': return '대여 가능';
      case 'RENTED': return '대여 중';
      case 'RESERVED': return '예약 중';
      case 'LOST': return '분실';
      default: return status ?? '알 수 없음';
    }
  }

  @override
  Widget build(BuildContext context) {
    final bookCtrl = context.watch<BookController>();
    final rentalCtrl = context.watch<RentalController>();
    final book = bookCtrl.selectedBook;
    final isAvailable = book?.status == 'AVAILABLE';

    return Scaffold(
      appBar: AppBar(title: const Text('도서 상세 정보')),
      body: bookCtrl.isLoading
          ? const Center(child: CircularProgressIndicator())
          : book == null
          ? const Center(child: Text('도서 정보를 불러올 수 없습니다.'))
          : SingleChildScrollView( // 설명이 길어질 수 있으므로 스크롤 추가
        padding: const EdgeInsets.all(20),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // 1. 도서 표지 이미지 (coverImage)
            Center(
              child: Container(
                height: 250,
                width: 170,
                decoration: BoxDecoration(
                  color: Colors.grey[200],
                  borderRadius: BorderRadius.circular(12),
                  boxShadow: [
                    BoxShadow(
                      color: Colors.black.withOpacity(0.1),
                      blurRadius: 10,
                      offset: const Offset(0, 5),
                    ),
                  ],
                ),
                child: ClipRRect(
                  borderRadius: BorderRadius.circular(12),
                  child: book.coverImage != null && book.coverImage!.isNotEmpty
                      ? Image.network(
                    book.coverImage!,
                    fit: BoxFit.cover,
                    errorBuilder: (context, error, stackTrace) =>
                    const Icon(Icons.broken_image, size: 64, color: Colors.grey),
                  )
                      : const Icon(Icons.menu_book, size: 64, color: Colors.grey),
                ),
              ),
            ),
            const SizedBox(height: 24),

            // 2. 상태 뱃지 및 제목
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Chip(
                  label: Text(
                    _statusLabel(book.status),
                    style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
                  ),
                  backgroundColor: _statusColor(book.status),
                ),
                Text('ID: ${book.id}', style: const TextStyle(color: Colors.grey)),
              ],
            ),
            const SizedBox(height: 12),
            Text(
              book.title ?? '제목 없음',
              style: const TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 20),
            const Divider(),

            // 3. 상세 정보 리스트
            _infoRow('저자', book.author),
            _infoRow('출판사', book.publisher),
            _infoRow('출판일', book.publishDate),
            _infoRow('ISBN', book.isbn),
            _infoRow('등록일', book.regDate),

            const SizedBox(height: 24),
            const Divider(),

            // 4. 도서 설명 (description)
            const Text(
              '도서 설명',
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 12),
            Text(
              book.description ?? '등록된 설명이 없습니다.',
              style: const TextStyle(fontSize: 16, height: 1.6, color: Colors.black87),
            ),

            const SizedBox(height: 100), // 버튼과 겹치지 않게 여백 추가
          ],
        ),
      ),
      // 5. 대여 신청 버튼 (하단 고정 느낌을 위해 bottomSheet 대신 마지막에 배치하거나 별도 처리 가능)
      bottomSheet: book != null ? Container(
        padding: const EdgeInsets.all(16),
        decoration: BoxDecoration(
            color: Colors.white,
            boxShadow: [BoxShadow(color: Colors.black12, blurRadius: 4, offset: Offset(0, -2))]
        ),
        child: SizedBox(
          width: double.infinity,
          height: 55,
          child: ElevatedButton.icon(
            onPressed: (isAvailable && !rentalCtrl.isLoading) ? _rentBook : null,
            style: ElevatedButton.styleFrom(
              backgroundColor: isAvailable ? Colors.blue : Colors.grey,
              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
            ),
            icon: rentalCtrl.isLoading
                ? const SizedBox(width: 20, height: 20, child: CircularProgressIndicator(strokeWidth: 2, color: Colors.white))
                : const Icon(Icons.library_add, color: Colors.white),
            label: Text(
              isAvailable ? '대여 신청하기' : _statusLabel(book.status),
              style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold, color: Colors.white),
            ),
          ),
        ),
      ) : null,
    );
  }

  Widget _infoRow(String label, String? value) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          SizedBox(
            width: 80,
            child: Text(label,
                style: const TextStyle(fontWeight: FontWeight.bold, color: Colors.blueGrey, fontSize: 15)),
          ),
          Expanded(
            child: Text(value ?? '-', style: const TextStyle(fontSize: 15)),
          ),
        ],
      ),
    );
  }
}