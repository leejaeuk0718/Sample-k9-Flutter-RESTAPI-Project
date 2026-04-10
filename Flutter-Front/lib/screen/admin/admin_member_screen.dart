import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:http/http.dart' as http;

import '../../const/api_constants.dart';

/// 관리자 - 회원 관리 화면
/// GET /api/member/list
class AdminMemberScreen extends StatefulWidget {
  const AdminMemberScreen({super.key});

  @override
  State<AdminMemberScreen> createState() => _AdminMemberScreenState();
}

class _AdminMemberScreenState extends State<AdminMemberScreen> {
  List<dynamic> _members = [];
  bool _isLoading = true;
  String _searchQuery = '';

  @override
  void initState() {
    super.initState();
    _fetchMembers();
  }

  Future<void> _fetchMembers() async {
    setState(() => _isLoading = true);
    try {
      final token =
          await const FlutterSecureStorage().read(key: 'accessToken');
      final res = await http.get(
        Uri.parse('${ApiConstants.springBaseUrl}/member/list'),
        headers: {'Authorization': 'Bearer $token'},
      );
      if (res.statusCode == 200) {
        setState(() {
          _members = jsonDecode(utf8.decode(res.bodyBytes)) as List<dynamic>;
        });
      }
    } catch (_) {
    } finally {
      setState(() => _isLoading = false);
    }
  }

  List<dynamic> get _filtered => _searchQuery.isEmpty
      ? _members
      : _members
          .where((m) =>
              (m['mid'] ?? '').toLowerCase().contains(_searchQuery) ||
              (m['mname'] ?? '').toLowerCase().contains(_searchQuery) ||
              (m['email'] ?? '').toLowerCase().contains(_searchQuery))
          .toList();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('회원 관리 (총 ${_members.length}명)'),
        centerTitle: true,
        actions: [
          IconButton(
              icon: const Icon(Icons.refresh), onPressed: _fetchMembers),
        ],
      ),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(12),
            child: TextField(
              decoration: const InputDecoration(
                hintText: '아이디, 이름 또는 이메일 검색',
                prefixIcon: Icon(Icons.search),
                border: OutlineInputBorder(),
                isDense: true,
              ),
              onChanged: (v) =>
                  setState(() => _searchQuery = v.toLowerCase()),
            ),
          ),
          Expanded(
            child: _isLoading
                ? const Center(child: CircularProgressIndicator())
                : _filtered.isEmpty
                    ? const Center(child: Text('회원이 없습니다.'))
                    : ListView.builder(
                        itemCount: _filtered.length,
                        itemBuilder: (_, i) {
                          final m = _filtered[i];
                          final isAdmin = m['role'] == 'ADMIN';
                          return ListTile(
                            leading: CircleAvatar(
                              backgroundColor: isAdmin
                                  ? Colors.indigo.withOpacity(0.15)
                                  : Colors.blue.withOpacity(0.12),
                              child: Text(
                                (m['mname'] ?? m['mid'] ?? '?')[0]
                                    .toUpperCase(),
                                style: TextStyle(
                                    color: isAdmin
                                        ? Colors.indigo
                                        : Colors.blue,
                                    fontWeight: FontWeight.bold),
                              ),
                            ),
                            title: Row(
                              children: [
                                Text(m['mname'] ?? '-',
                                    style: const TextStyle(
                                        fontWeight: FontWeight.w500)),
                                const SizedBox(width: 8),
                                if (isAdmin)
                                  Container(
                                    padding: const EdgeInsets.symmetric(
                                        horizontal: 6, vertical: 2),
                                    decoration: BoxDecoration(
                                      color: Colors.indigo,
                                      borderRadius:
                                          BorderRadius.circular(4),
                                    ),
                                    child: const Text('ADMIN',
                                        style: TextStyle(
                                            color: Colors.white,
                                            fontSize: 10)),
                                  ),
                              ],
                            ),
                            subtitle: Text(
                                '${m['mid'] ?? '-'} · ${m['email'] ?? '-'}'),
                            trailing: Text(
                              m['region'] ?? '',
                              style: const TextStyle(
                                  fontSize: 11, color: Colors.grey),
                            ),
                          );
                        },
                      ),
          ),
        ],
      ),
    );
  }
}
