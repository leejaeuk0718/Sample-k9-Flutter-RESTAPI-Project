import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:http/http.dart' as http;

import '../../const/api_constants.dart';

/// 관리자 - 문의 관리 화면
/// GET /api/inquiry/all (전체 조회 — 없으면 /api/inquiry?page=0&size=100)
class AdminInquiryScreen extends StatefulWidget {
  const AdminInquiryScreen({super.key});

  @override
  State<AdminInquiryScreen> createState() => _AdminInquiryScreenState();
}

class _AdminInquiryScreenState extends State<AdminInquiryScreen> {
  List<dynamic> _inquiries = [];
  bool _isLoading = true;

  @override
  void initState() {
    super.initState();
    _fetchInquiries();
  }

  Future<void> _fetchInquiries() async {
    setState(() => _isLoading = true);
    try {
      final token =
          await const FlutterSecureStorage().read(key: 'accessToken');
      // 관리자 전용 전체 문의 조회 (백엔드에 /api/inquiry/all 없으면 /api/inquiry?page=0&size=200 시도)
      final res = await http.get(
        Uri.parse('${ApiConstants.springBaseUrl}/inquiry?page=0&size=200'),
        headers: {'Authorization': 'Bearer $token'},
      );
      if (res.statusCode == 200) {
        final data = jsonDecode(utf8.decode(res.bodyBytes));
        setState(() {
          _inquiries = (data['content'] ?? data) as List<dynamic>;
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
        title: Text('문의 관리 (총 ${_inquiries.length}건)'),
        centerTitle: true,
        actions: [
          IconButton(
              icon: const Icon(Icons.refresh), onPressed: _fetchInquiries),
        ],
      ),
      body: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : _inquiries.isEmpty
              ? const Center(child: Text('문의가 없습니다.'))
              : ListView.builder(
                  padding: const EdgeInsets.all(12),
                  itemCount: _inquiries.length,
                  itemBuilder: (_, i) {
                    final inq = _inquiries[i];
                    final answered =
                        inq['answered'] ?? inq['isReplied'] ?? false;
                    return Card(
                      margin: const EdgeInsets.only(bottom: 8),
                      child: ListTile(
                        leading: CircleAvatar(
                          backgroundColor: answered
                              ? Colors.green.withOpacity(0.15)
                              : Colors.red.withOpacity(0.15),
                          child: Icon(
                            answered
                                ? Icons.check_circle
                                : Icons.pending_outlined,
                            color:
                                answered ? Colors.green : Colors.red,
                          ),
                        ),
                        title: Text(inq['title'] ?? '-',
                            style: const TextStyle(
                                fontWeight: FontWeight.w500)),
                        subtitle: Text(
                            '작성자: ${inq['mid'] ?? inq['memberId'] ?? '-'}'),
                        trailing: Chip(
                          label: Text(
                            answered ? '답변완료' : '미답변',
                            style: const TextStyle(
                                fontSize: 11, color: Colors.white),
                          ),
                          backgroundColor:
                              answered ? Colors.green : Colors.red,
                          padding: EdgeInsets.zero,
                        ),
                      ),
                    );
                  },
                ),
    );
  }
}
