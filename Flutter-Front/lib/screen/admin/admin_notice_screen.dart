import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:http/http.dart' as http;

import '../../const/api_constants.dart';

/// 관리자 - 공지사항 관리 화면
/// GET /api/notice?page=0&size=100
class AdminNoticeScreen extends StatefulWidget {
  const AdminNoticeScreen({super.key});

  @override
  State<AdminNoticeScreen> createState() => _AdminNoticeScreenState();
}

class _AdminNoticeScreenState extends State<AdminNoticeScreen> {
  List<dynamic> _notices = [];
  bool _isLoading = true;

  @override
  void initState() {
    super.initState();
    _fetchNotices();
  }

  Future<void> _fetchNotices() async {
    setState(() => _isLoading = true);
    try {
      final token =
          await const FlutterSecureStorage().read(key: 'accessToken');
      final res = await http.get(
        Uri.parse('${ApiConstants.springBaseUrl}/notice?page=0&size=100'),
        headers: {'Authorization': 'Bearer $token'},
      );
      if (res.statusCode == 200) {
        final data = jsonDecode(utf8.decode(res.bodyBytes));
        setState(() {
          _notices = (data['content'] ?? data) as List<dynamic>;
        });
      }
    } catch (_) {
    } finally {
      setState(() => _isLoading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('공지사항 관리 (총 ${_notices.length}건)'),
        centerTitle: true,
        actions: [
          IconButton(
              icon: const Icon(Icons.refresh), onPressed: _fetchNotices),
        ],
      ),
      body: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : _notices.isEmpty
              ? const Center(child: Text('등록된 공지사항이 없습니다.'))
              : ListView.builder(
                  padding: const EdgeInsets.all(12),
                  itemCount: _notices.length,
                  itemBuilder: (_, i) {
                    final n = _notices[i];
                    final regDate = (n['regDate'] ?? '').toString();
                    final dateStr = regDate.length >= 10
                        ? regDate.substring(0, 10)
                        : regDate;
                    return Card(
                      margin: const EdgeInsets.only(bottom: 8),
                      child: ListTile(
                        leading: const CircleAvatar(
                          backgroundColor: Color(0x1A9C27B0),
                          child: Icon(Icons.campaign_outlined,
                              color: Colors.purple),
                        ),
                        title: Text(n['title'] ?? '-',
                            style: const TextStyle(
                                fontWeight: FontWeight.w500)),
                        subtitle: Text('등록일: $dateStr · 조회: ${n['viewCount'] ?? 0}'),
                        trailing: Text(
                          '#${n['id']}',
                          style: const TextStyle(
                              fontSize: 11, color: Colors.grey),
                        ),
                        onTap: () => Navigator.pushNamed(
                          context,
                          '/noticeDetail',
                          arguments: n['id'],
                        ),
                      ),
                    );
                  },
                ),
    );
  }
}
