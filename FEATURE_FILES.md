# FEATURE_FILES.md — 프로젝트 기능 파일 현황 및 작업 체크리스트

> 최종 수정: 2026-04-10
> 프로젝트: Flutter + Spring Boot 도서관 통합 앱
> 구성: Flutter 프론트엔드 + Spring Boot 백엔드 (JWT 인증 + 도서관 API)

---

## 목차

1. [전체 아키텍처 개요](#1-전체-아키텍처-개요)
2. [회원 시스템 구조](#2-회원-시스템-구조)
3. [Flutter 화면 구조 (탭 기반)](#3-flutter-화면-구조-탭-기반)
4. [Flutter 파일 현황](#4-flutter-파일-현황)
5. [Spring 백엔드 파일 현황](#5-spring-백엔드-파일-현황)
6. [완료된 작업 체크리스트](#6-완료된-작업-체크리스트)
7. [현재 동작 흐름](#7-현재-동작-흐름)
8. [신규 기능 구현 체크리스트](#8-신규-기능-구현-체크리스트)
9. [API 엔드포인트 매핑표](#9-api-엔드포인트-매핑표)

---

## 1. 전체 아키텍처 개요

```
Flutter App (Android Emulator)
        |
        | HTTP (10.0.2.2:8080)
        v
Spring Boot 서버 (localhost:8080)
   +-- JWT 인증 시스템  (/generateToken, /api/member/*)
   |      +-- Member 테이블 (통합 단일 회원)
   +-- 도서관 API 시스템  (/api/*)
          +-- Member 테이블
              +-- Book, Rental, Notice, Event
              +-- Inquiry, Apply (시설예약)
              +-- WishBook, Reply
   +-- 미디어 저장소 (c:/upload/springTest)
          +-- 프로필 이미지 (UUID 파일명)
```

**주요 상수 (Flutter-Front/lib/const/api_constants.dart):**
- Spring API base: `http://10.0.2.2:8080/api`
- JWT base: `http://10.0.2.2:8080`
- Flask AI base: `http://10.0.2.2:5000/predict`

---

## 2. 회원 시스템 구조

> **핵심**: Spring에는 단일 Member 테이블을 사용합니다 (APIUser 통합 완료).

| 구분 | 엔티티 | 용도 | 엔드포인트 |
|------|--------|------|------------|
| 통합 회원 | `Member` | JWT 인증 + 도서관 모든 API | `POST /api/member/signup` |
| 로그인 | JWT | 토큰 발급 | `POST /generateToken` |

**FlutterSecureStorage 저장 키:**

| 키 | 값 | 용도 |
|----|-----|------|
| `accessToken` | JWT Bearer 토큰 | API 요청 인증 헤더 |
| `refreshToken` | JWT 리프레시 토큰 | 토큰 갱신 |
| `mid` | String (사용자 아이디) | 사용자 식별 |
| `memberId` | Long (DB 기본키) | 도서관 API 파라미터 |
| `profileImg` | 프로필 이미지 UUID 파일명 | UI 표시 (서버에서 로드) |

---

## 3. Flutter 화면 구조 (탭 기반)

### 앱 진입 흐름
```
MySplash (3초)
    |
    v
MainTabScreen  <-- 모든 진입점 (/main 라우트)
    |
    +-- [0] HomeTab       : 비로그인/로그인 공통
    +-- [1] BookTab       : 로그인 필요
    +-- [2] MyServiceTab  : 로그인 필요
    +-- [3] AiTab         : 로그인 필요
    +-- [4] MyPageTab     : 로그인 필요
```

### 탭별 구조

| 탭 | 화면명 | 주요 기능 |
|----|--------|----------|
| 0 홈 | HomeTab | PageView 배너(자동슬라이드) + 공지사항 미리보기 3건 + 이벤트 미리보기 3건 |
| 1 도서 | BookTab | 검색바(로컬필터) + 도서 ListView → /bookDetail |
| 2 내 서비스 | MyServiceTab | 중첩 TabBar: 대여현황 / 1:1문의(FAB) / 시설예약(폼+목록) |
| 3 AI | AiTab | AI이미지 카드 → /ai-image / AI주가 카드 → /ai-stock |
| 4 마이페이지 | MyPageTab | 회원정보(이름/이메일/지역/가입일) + 바로가기 + 관리자 진입 |

---

## 4. Flutter 파일 현황

### 4-1. 앱 진입점

| 파일 | 상태 | 설명 |
|------|------|------|
| Flutter-Front/lib/main.dart | 완료 | MultiProvider 등록 |
| Flutter-Front/lib/my_app.dart | 완료 | Material3 테마 + 전체 Named Route 등록 |
| Flutter-Front/lib/screen/my_splash.dart | 완료 | 스플래시 → /main 이동 |
| Flutter-Front/lib/const/api_constants.dart | 완료 | API base URL 상수 |

### 4-2. 탭 메인 화면

| 파일 | 상태 | 설명 |
|------|------|------|
| Flutter-Front/lib/screen/tab/main_tab_screen.dart | 완료 | BottomNavigationBar 5탭, IndexedStack 상태보존 |
| Flutter-Front/lib/screen/tab/home_tab.dart | 완료 | PageView 배너 + 공지/이벤트 미리보기 |
| Flutter-Front/lib/screen/tab/book_tab.dart | 완료 | 검색바 + 도서 ListView |
| Flutter-Front/lib/screen/tab/my_service_tab.dart | 완료 | 중첩 TabController |
| Flutter-Front/lib/screen/tab/ai_tab.dart | 완료 | AI 기능 선택 카드 2종 |
| Flutter-Front/lib/screen/tab/mypage_tab.dart | 신규기능 | 프로필 이미지 표시 + 관리자 진입 버튼 추가 예정 |

### 4-3. 인증 (Auth)

| 파일 | 상태 | 설명 |
|------|------|------|
| Flutter-Front/lib/controller/auth/login_controller.dart | 완료 | JWT 로그인, loadMemberInfo(), 로그아웃 |
| Flutter-Front/lib/controller/auth/signup_controller.dart | 신규기능 | 프로필 이미지 선택/업로드 추가 예정 |
| Flutter-Front/lib/screen/login_screen.dart | 완료 | 로그인 화면 |
| Flutter-Front/lib/screen/signup_screen.dart | 신규기능 | 프로필 이미지 선택 UI 추가 예정 |
| Flutter-Front/lib/screen/mypage/edit_profile_screen.dart | 신규 | 내 정보 수정 화면 (신규 생성) |

### 4-4. 관리자 (Admin) — 신규

| 파일 | 상태 | 설명 |
|------|------|------|
| Flutter-Front/lib/screen/admin/admin_dashboard_screen.dart | 신규 | 관리자 대시보드 (신규 생성) |
| Flutter-Front/lib/screen/admin/admin_book_screen.dart | 신규 | 도서 관리 화면 |
| Flutter-Front/lib/screen/admin/admin_member_screen.dart | 신규 | 회원 관리 화면 |
| Flutter-Front/lib/screen/admin/admin_event_screen.dart | 신규 | 이벤트 관리 화면 |
| Flutter-Front/lib/screen/admin/admin_notice_screen.dart | 신규 | 공지사항 관리 화면 |
| Flutter-Front/lib/screen/admin/admin_inquiry_screen.dart | 신규 | 문의 관리 화면 |
| Flutter-Front/lib/screen/admin/admin_facility_screen.dart | 신규 | 시설예약 관리 화면 |

### 4-5. 도서 (Book)

| 파일 | 상태 | 설명 |
|------|------|------|
| Flutter-Front/lib/controller/book_controller.dart | 완료 | fetchBooks(), fetchBookById() |
| Flutter-Front/lib/screen/book/book_list_screen.dart | 완료 | 도서 목록 화면 |
| Flutter-Front/lib/screen/book/book_detail_screen.dart | 완료 | 도서 상세 + 대여 신청 |

### 4-6. 이벤트 (Event)

| 파일 | 상태 | 설명 |
|------|------|------|
| Flutter-Front/lib/controller/event_controller.dart | 완료 | fetchEvents(), applyEvent() |
| Flutter-Front/lib/screen/event/event_list_screen.dart | 완료 | 이벤트 목록 |
| Flutter-Front/lib/screen/event/event_detail_screen.dart | 신규기능 | 행사 신청 버튼 API 연동 예정 |

---

## 5. Spring 백엔드 파일 현황

### JWT 인증 (통합)
| 파일 | 설명 |
|------|------|
| security/filter/APILoginFilter.java | POST /generateToken → JWT 발급 |
| security/handler/APILoginSuccessHandler.java | accessToken + refreshToken 응답 |
| security/filter/TokenCheckFilter.java | Bearer 토큰 검증 |
| security/APIUserDetailsService.java | Member 기반 UserDetails 로드 |

### 회원 API
| 파일 | 설명 |
|------|------|
| controller/MemberController.java | POST /api/member/signup, GET /api/member/me, PUT /api/member/update, PUT /api/member/profile-image |
| service/library/MemberLibraryServiceImpl.java | Member CRUD, BCrypt 암호화 |
| domain/library/Member.java | mid, mpw, mname, email, region, role, profileImage |

### 도서관 기능 API
| 컨트롤러 | 주요 엔드포인트 |
|----------|---------------|
| BookController.java | GET /api/book?page&size, GET /api/book/{id} |
| RentalController.java | GET /api/rental?memberId&page&size, POST /api/rental |
| NoticeController.java | GET /api/notice?page&size, GET /api/notice/{id} |
| EventController.java | GET /api/event?page&size, POST /api/event/{id}/apply?memberId |
| InquiryController.java | GET /api/inquiry/my?memberId, POST /api/inquiry?memberId |
| ApplyController.java | GET /api/apply/my?memberId&page&size, POST /api/apply?memberId |

### 미디어 저장소 설정 (신규)
| 파일 | 설명 |
|------|------|
| config/CustomServletConfig.java | /files/** → c:/upload/springTest 정적 서빙 추가 예정 |
| MemberController.java | PUT /api/member/profile-image (base64 → UUID 파일 저장) |

---

## 6. 완료된 작업 체크리스트

### 회원 시스템 통합 (APIUser → Member 단일화)
- [x] `APIUserDetailsService` — Member 기반으로 교체
- [x] `APIUserDTO` — APIUser 의존성 제거, mid 필드 유지
- [x] `MemberController` — /api/member/* 통합 (signup, me, update, profile-image, check-mid)
- [x] `APIUser`, `APlUserRepository` 삭제
- [x] `MemberService`, `MemberServiceImpl` (구버전) 삭제
- [x] `LibraryMemberController` 삭제
- [x] Git 커밋 및 푸시 완료

### 탭 기반 레이아웃 개편
- [x] `MainTabScreen` — NavigationBar 5탭, IndexedStack 상태보존
- [x] `HomeTab`, `BookTab`, `MyServiceTab`, `AiTab`, `MyPageTab`
- [x] 공통 위젯 (`AppBaseLayout`, `LoadingWidget`, `EmptyWidget` 등)

### LoginController 개선
- [x] `loadMemberInfo()` — 회원정보 메모리 저장
- [x] 로그아웃 시 모든 회원정보 초기화

---

## 7. 현재 동작 흐름

### 회원가입 플로우 (통합 후)
```
SignupScreen (아이디/이름/이메일/지역/비밀번호)
    +-- POST /api/member/signup  → Member 단일 생성
    +-- 성공 → /login
```

### 로그인 플로우
```
LoginScreen
    +-- POST /generateToken      → accessToken/refreshToken 저장
    +-- GET  /api/member/me      → memberId + 회원정보 저장
    +-- 성공 → /main (MainTabScreen)
```

---

## 8. 신규 기능 구현 체크리스트

> 이 섹션은 현재 세션에서 구현할 4가지 신규 기능의 상세 체크리스트입니다.

---

### Feature 1: 회원가입 프로필 이미지 📸

**목표**: 회원가입 시 갤러리/카메라로 프로필 이미지 선택 → 앱 내부 저장 → base64로 서버 전송 (5MB 미만)

#### 구현 방법
- Flutter `image_picker` 패키지로 갤러리/카메라 접근
- `path_provider` 패키지로 앱 내부 저장소 경로 획득 후 복사 저장
- 이미지를 base64 인코딩하여 JSON body에 포함
- 5MB(5,242,880 bytes) 초과 시 경고 다이얼로그 표시
- Spring에서 base64 디코딩 → UUID 파일명으로 `c:/upload/springTest`에 저장

#### Flutter 체크리스트
- [ ] `pubspec.yaml` — `path_provider: ^2.1.2` 추가
- [ ] `signup_controller.dart` — `pickProfileImage()` 메서드 (갤러리/카메라 선택)
- [ ] `signup_controller.dart` — `_saveToAppStorage()` 앱 내부 저장 (Documents 폴더)
- [ ] `signup_controller.dart` — `signup()` 수정: base64 인코딩 후 `profileImageBase64` 필드 포함
- [ ] `signup_controller.dart` — 5MB 초과 검사 및 경고
- [ ] `signup_screen.dart` — 프로필 이미지 선택 UI (CircleAvatar + 갤러리/카메라 버튼)

#### Spring 체크리스트
- [ ] `MemberController.java` — `POST /api/member/signup` 요청 body에 `profileImageBase64` 필드 처리
- [ ] `MemberLibraryServiceImpl.java` — base64 디코딩 → UUID 파일명 생성 → `c:/upload/springTest` 저장
- [ ] `Member.java` — `profileImage` 필드 (UUID 파일명 저장)
- [ ] `CustomServletConfig.java` — `/upload/**` → `c:/upload/springTest` 정적 파일 서빙 설정

---

### Feature 2: 마이페이지 프로필 이미지 + 내 정보 수정 👤

**목표**: 마이페이지에서 업로드된 프로필 이미지 표시 + 내 정보 수정 화면 구현

#### 구현 방법
- `LoginController.loadMemberInfo()` 에서 `profileImage` 필드 읽기
- 프로필 이미지 URL: `http://10.0.2.2:8080/upload/{uuid파일명}`
- `NetworkImage` 위젯으로 표시, 없으면 이니셜 CircleAvatar 폴백
- 내 정보 수정: 이름/이메일/지역 수정 → `PUT /api/member/update` 호출

#### Flutter 체크리스트
- [ ] `login_controller.dart` — `memberProfileImage` 필드 추가, `loadMemberInfo()`에서 파싱
- [ ] `mypage_tab.dart` — CircleAvatar를 NetworkImage 프로필 이미지로 교체 (폴백 포함)
- [ ] `mypage_tab.dart` — "내 정보 수정" 버튼 → `/editProfile` 라우트 이동
- [ ] `mypage_tab.dart` — `memberRole == 'ADMIN'` 시 "관리자 대시보드" 버튼 표시
- [ ] `edit_profile_screen.dart` — 신규 생성: 이름/이메일/지역 수정 폼
- [ ] `edit_profile_screen.dart` — `PUT /api/member/update` API 호출
- [ ] `my_app.dart` — `/editProfile` 라우트 등록

---

### Feature 3: 관리자 모드 🔧

**목표**: `memberRole == 'ADMIN'` 인 경우 관리자 대시보드 진입 가능, 도서/회원/이벤트/공지/문의/시설예약 관리

#### 구현 방법
- 마이페이지에 "관리자 대시보드" 버튼 (ADMIN 역할일 때만 표시)
- 관리자 대시보드: 6개 관리 항목 카드 그리드
- 각 관리 화면: 목록 조회 + 기본 CRUD 작업 (조회 위주)

#### Flutter 체크리스트
- [ ] `admin_dashboard_screen.dart` — 신규: 6개 관리 카드 그리드 (도서/회원/이벤트/공지/문의/시설)
- [ ] `admin_book_screen.dart` — 도서 목록 + 상태 변경 기능
- [ ] `admin_member_screen.dart` — 회원 목록 조회 (`GET /api/member/list`)
- [ ] `admin_event_screen.dart` — 이벤트 목록 + 신청자 확인
- [ ] `admin_notice_screen.dart` — 공지사항 목록 + 등록 폼
- [ ] `admin_inquiry_screen.dart` — 문의 목록 + 답변 처리
- [ ] `admin_facility_screen.dart` — 시설예약 목록 조회
- [ ] `my_app.dart` — `/admin`, `/adminBook`, `/adminMember` 등 라우트 등록

#### Spring 체크리스트
- [ ] `MemberController.java` — `GET /api/member/list` (ADMIN 전용 회원 목록 조회)

---

### Feature 4: 이벤트 참가 신청하기 🎉

**목표**: 이벤트 상세 화면의 "행사 참가 신청하기" 버튼을 실제 API와 연동

#### 구현 방법
- `EventController.applyEvent(int eventId)` 이미 구현됨
- `event_detail_screen.dart`의 TODO 버튼에서 `EventController.applyEvent()` 호출
- 신청 성공/실패 SnackBar 메시지 표시
- 중복 신청 방지 (서버 에러 메시지 처리)

#### Flutter 체크리스트
- [ ] `event_detail_screen.dart` — `StatelessWidget` → `StatefulWidget` 또는 `Consumer` 활용
- [ ] `event_detail_screen.dart` — 신청 버튼 onPressed에 `EventController.applyEvent(eventId)` 연결
- [ ] `event_detail_screen.dart` — 로딩 상태 표시 (버튼 비활성화)
- [ ] `event_detail_screen.dart` — 성공/실패 SnackBar 메시지

#### Spring (기존 완료)
- [x] `EventController.java` — `POST /api/event/{id}/apply?memberId` 이미 구현됨
- [x] `event_controller.dart` — `applyEvent(int eventId)` 이미 구현됨

---

## 9. API 엔드포인트 매핑표

| 기능 | 메서드 | 엔드포인트 | Flutter 파일 | JWT |
|------|--------|-----------|-------------|-----|
| 로그인 (JWT) | POST | `/generateToken` | login_controller.dart | 없음 |
| 회원 가입 | POST | `/api/member/signup` | signup_controller.dart | 없음 |
| 내 회원정보 | GET | `/api/member/me?mid={}` | login_controller.dart | JWT |
| 회원정보 수정 | PUT | `/api/member/update` | edit_profile_screen.dart | JWT |
| 프로필 이미지 업로드 | PUT | `/api/member/profile-image` | signup_controller.dart | JWT |
| 아이디 중복 체크 | GET | `/api/member/check-mid?mid={}` | signup_controller.dart | 없음 |
| 회원 목록 (관리자) | GET | `/api/member/list` | admin_member_screen.dart | JWT+ADMIN |
| 도서 목록 | GET | `/api/book?page&size` | book_controller.dart | JWT |
| 도서 상세 | GET | `/api/book/{id}` | book_controller.dart | JWT |
| 도서 대여 신청 | POST | `/api/rental?memberId&bookId` | rental_controller.dart | JWT |
| 내 대여 목록 | GET | `/api/rental?memberId&page&size` | rental_controller.dart | JWT |
| 공지사항 목록 | GET | `/api/notice?page&size` | notice_controller.dart | 선택 |
| 공지사항 상세 | GET | `/api/notice/{id}` | notice_controller.dart | 선택 |
| 이벤트 목록 | GET | `/api/event?page&size` | event_controller.dart | 선택 |
| 이벤트 신청 | POST | `/api/event/{id}/apply?memberId` | event_controller.dart | JWT |
| 내 문의 목록 | GET | `/api/inquiry/my?memberId` | inquiry_controller.dart | JWT |
| 문의 작성 | POST | `/api/inquiry?memberId` | inquiry_controller.dart | JWT |
| 내 예약 목록 | GET | `/api/apply/my?memberId&page&size` | reserve_controller.dart | JWT |
| 시설 예약 신청 | POST | `/api/apply?memberId` | reserve_controller.dart | JWT |
| 프로필 이미지 파일 | GET | `/upload/{filename}` | NetworkImage URL | 없음 |
