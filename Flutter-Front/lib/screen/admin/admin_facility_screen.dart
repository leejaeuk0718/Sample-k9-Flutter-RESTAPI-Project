import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:http/http.dart' as http;

import '../../const/api_constants.dart';

/// 관리자 - 시설예약 관리 화면
/// GET /api/apply?page=0&size=100
class AdminFacilityScreen extends StatefulWidget {
  const AdminFacilityScreen({super.key});

  @override
  State<AdminFacilityScreen> createState() => _AdminFacilityScreenState();
}

class _AdminFacilityScreenState extends State<AdminFacilityScreen> {
  List<dynamic> _applies = [];
  bool _isLoading = true;

  @override
  void initState() {
    super.initState();
    _fetchApplies();
  }

  Future<void> _fetchApplies() async {
    setState(() => _isLoading = true);
    try {
      final token =
          await const FlutterSecureStorage().read(key: 'accessToken');
      final res = await http.get(
        Uri.parse('${ApiConstants.springBaseUrl}/apply?page=0&size=200'),
        headers: {'Authorization': 'Bearer $token'},
      );
      if (res.statusCode == 200) {
        final data = jsonDecode(utf8.decode(res.bodyBytes));
        setState(() {
          _applies = (data['content'] ?? data) as List<dynamic>;
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
        title: Text('시설예약 관리 (총 ${_applies.length}건)'),
        centerTitle: true,
        actions: [
          IconButton(
              icon: const Icon(Icons.refresh), onPressed: _fetchApplies),
        ],
      ),
      body: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : _applies.isEmpty
              ? const Center(child: Text('시설 예약 내역이 없습니다.'))
              : ListView.builder(
                  padding: const EdgeInsets.all(12),
                  itemCount: _applies.length,
                  itemBuilder: (_, i) {
                    final a = _applies[i];
                    final facilityName =
                        a['facilityType'] ?? a['facilityName'] ?? '-';
                    final reserveDate =
                        a['reserveDate'] ?? a['applyDate'] ?? '-';
                    return Card(
                      margin: const EdgeInsets.only(bottom: 8),
                      child: ListTile(
                        leading: const CircleAvatar(
                          backgroundColor: Color(0x1A009688),
                          child: Icon(Icons.meeting_room_outlined,
                              color: Colors.teal),
                        ),
                        title: Text(facilityName,
                            style: const TextStyle(
                                fontWeight: FontWeight.w500)),
                        subtitle: Text(
                            '예약일: $reserveDate\n회원: ${a['memberId'] ?? '-'}'),
                        isThreeLine: true,
                        trailing: Text(
                          '#${a['id']}',
                          style: const TextStyle(
                              fontSize: 11, color: Colors.grey),
                        ),
                      ),
                    );
                  },
                ),
    );
  }
}
