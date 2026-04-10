import 'package:flutter/material.dart';

/// 관리자 대시보드 화면
/// - ADMIN 역할 회원만 접근 가능
/// - 도서/회원/이벤트/공지/문의/시설예약 관리 카드 그리드
class AdminDashboardScreen extends StatelessWidget {
  const AdminDashboardScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final items = [
      _AdminItem(
        icon: Icons.menu_book,
        color: Colors.blue,
        title: '도서 관리',
        subtitle: '도서 목록 및 상태 관리',
        route: '/adminBook',
      ),
      _AdminItem(
        icon: Icons.people,
        color: Colors.green,
        title: '회원 관리',
        subtitle: '전체 회원 조회',
        route: '/adminMember',
      ),
      _AdminItem(
        icon: Icons.celebration,
        color: Colors.orange,
        title: '이벤트 관리',
        subtitle: '행사 목록 및 신청 현황',
        route: '/adminEvent',
      ),
      _AdminItem(
        icon: Icons.campaign_outlined,
        color: Colors.purple,
        title: '공지사항 관리',
        subtitle: '공지 등록 및 목록',
        route: '/adminNotice',
      ),
      _AdminItem(
        icon: Icons.question_answer_outlined,
        color: Colors.red,
        title: '문의 관리',
        subtitle: '1:1 문의 답변 처리',
        route: '/adminInquiry',
      ),
      _AdminItem(
        icon: Icons.meeting_room_outlined,
        color: Colors.teal,
        title: '시설예약 관리',
        subtitle: '시설 예약 현황 조회',
        route: '/adminFacility',
      ),
    ];

    return Scaffold(
      appBar: AppBar(
        title: const Text('관리자 대시보드'),
        centerTitle: true,
        backgroundColor: Colors.indigo,
        foregroundColor: Colors.white,
      ),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: GridView.builder(
          itemCount: items.length,
          gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
            crossAxisCount: 2,
            crossAxisSpacing: 12,
            mainAxisSpacing: 12,
            childAspectRatio: 1.1,
          ),
          itemBuilder: (context, index) {
            final item = items[index];
            return _AdminCard(item: item);
          },
        ),
      ),
    );
  }
}

class _AdminItem {
  final IconData icon;
  final Color color;
  final String title;
  final String subtitle;
  final String route;

  const _AdminItem({
    required this.icon,
    required this.color,
    required this.title,
    required this.subtitle,
    required this.route,
  });
}

class _AdminCard extends StatelessWidget {
  final _AdminItem item;

  const _AdminCard({required this.item});

  @override
  Widget build(BuildContext context) {
    return Card(
      elevation: 2,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
      child: InkWell(
        borderRadius: BorderRadius.circular(16),
        onTap: () => Navigator.pushNamed(context, item.route),
        child: Padding(
          padding: const EdgeInsets.all(16),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              CircleAvatar(
                radius: 28,
                backgroundColor: item.color.withOpacity(0.15),
                child: Icon(item.icon, color: item.color, size: 28),
              ),
              const SizedBox(height: 12),
              Text(item.title,
                  style: const TextStyle(
                      fontWeight: FontWeight.bold, fontSize: 14)),
              const SizedBox(height: 4),
              Text(item.subtitle,
                  textAlign: TextAlign.center,
                  style:
                      const TextStyle(fontSize: 11, color: Colors.grey)),
            ],
          ),
        ),
      ),
    );
  }
}
