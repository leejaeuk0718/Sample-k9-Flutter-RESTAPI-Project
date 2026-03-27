# 📚 부산도서관 관리 시스템 (Busan Library Management System)

A comprehensive library management system featuring a Flutter mobile frontend, a Spring Boot REST API backend, and a Flask-based AI tool for image classification.

## 🚀 프로젝트 개요 (Project Overview)
- **프로젝트 명:** 부산도서관 관리 시스템 연동 REST API
- **설명:** 사용자(회원)와 도서관 관리자(관리자)를 위한 도서 대여/반납, 공지사항, 도서관 행사 예약, 1:1 문의 등 복합적인 기능을 제공하며 AI 이미지 분류 기능이 통합된 시스템입니다.

## 🛠 기술 스택 (Technology Stack)
### 📱 Frontend (Mobile App)
- **Framework:** Flutter (v3.29.0), Dart (v3.7.0)
- **State Management:** Provider (MVVM 패턴 유사 구조)
- **UI Library:** Material Design 3

### 💻 Backend (Main Server)
- **Framework:** Spring Boot (v3.4.x)
- **Database:** MySQL / MariaDB, Spring Data JPA
- **Security:** Spring Security + JWT Token Authentication

### 🤖 Backend (AI Server)
- **Framework:** Flask (Python)
- **Features:** Image classification models

## 📌 주요 기능 (Core Features)
- **회원 관리 시스템 (Member Management)**
  - JWT 기반 로그인/회원가입, 권한별(USER, ADMIN) 접근 제어 관리
- **도서 서비스 (Book Services)**
  - 도서 검색 및 상세 보기 (QR 코드 지원)
  - 도서 대여, 반납, 예약 시스템 연동
  - 희망도서 신청 (Wishlist)
- **도서관 정보 공유 (Information Sharing)**
  - 공지사항 열람 및 이달의 행사(Community Event) 리스트 
  - 행사 및 시설 예약(Application) 시스템
- **고객 지원 (Support)**
  - 1:1 문의 시스템 (사용자 질문 및 관리자 답변)
- **AI 이미지 툴 (AI Integration)**
  - 도서관 관련 이미지 AI 사전 훈련 분류 연동

## 🏗 데이터베이스 구조 핵심 (ERD Highlights)
- `Member`: uid, email, pw, nickname, role, regDate
- `Book`: bno, title, author, publisher, isbn, category, status
- `Rental`: rno, bno, uid, rentalDate, returnDate, status
- *그 외 Entity:* Notice, Event, Inquiry, Apply, WishBook

## 📂 프론트엔드 아키텍처 (Flutter Architecture)
- `lib/model`: 데이터 구조화 및 JSON 직렬화 지원 (fromJson, toJson)
- `lib/controller`: API 통신 비즈니스 로직, `ChangeNotifier` 기반 상태 관리
- `lib/screen`: 기능 및 도메인별 분리된 사용자 UI 위젯
- `lib/const`: API Base URL, App Colors 등 앱 공유 전역 상태 지정

## 📦 소스 코드 저장소 (Repository Repository)
본 프로젝트는 다음의 단일 저장소 구조 하에 통합 관리됩니다.
- **Repository URL:** [lsy3709/Sample-k9-Flutter-RESTAPI-Project](https://github.com/lsy3709/Sample-k9-Flutter-RESTAPI-Project)
