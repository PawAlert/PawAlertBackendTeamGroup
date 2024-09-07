# 🐾 동물실종알림 / 공유 시스템 (PawAlert)

## 📋 서비스 소개

PawAlert는 반려동물을 잃어버린 사람들과 동물을 찾는 사람들을 연결하는 플랫폼입니다. 또한 동물병원과 보호센터 관계자들이 중요한 정보를 공유할 수 있는 공간을 제공합니다.

## 🔗 주요 링크

- 🌐 서비스 URL: [https://pawalert.co.kr/](https://pawalert.co.kr/)
- 📘 프로젝트 문서: [Notion](https://jinsung7605.notion.site/PawAlert-3309da778aeb4342ad733b6ba4089be9?pvs=4)
- 🎨 디자인: [Figma](https://www.figma.com/design/2Lqkxfy3c59ksHUiQCK6nj/PawAlert?node-id=0-1&t=uxyIHrYkqWhgKvSg-1)

## 🛠 기술 스택

### Frontend
![Vue.js](https://img.shields.io/badge/Vue.js-4FC08D?style=for-the-badge&logo=vue.js&logoColor=white) ![Vuetify](https://img.shields.io/badge/Vuetify-1867C0?style=for-the-badge&logo=vuetify&logoColor=white)

### Backend
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white) ![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white) ![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=json-web-tokens&logoColor=white) ![Spring Batch](https://img.shields.io/badge/Spring_Batch-6DB33F?style=for-the-badge&logo=spring&logoColor=white) ![WebSocket](https://img.shields.io/badge/WebSocket-010101?style=for-the-badge&logo=socket.io&logoColor=white)

![MariaDB](https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white) ![MongoDB](https://img.shields.io/badge/MongoDB-47A248?style=for-the-badge&logo=mongodb&logoColor=white) ![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)

## 👥 사용자 그룹

### 🙋‍♂️ 일반사용자 / 반려동물을 잃어버린 사용자

- 실종신고 글 작성 및 공유
- 발견 시 연락 받기
- 선택 지역의 실종 동물 알림 받기
- 댓글 및 채팅 알림

### 🏥 동물병원 / 보호센터 관계자

- 인증 시스템을 통한 검증된 가입
- 동물 예방 정보, 보호센터 임보, 봉사활동 등 관련 글 작성
- 분리된 전용 페이지

### 🔒 데이터 보안 및 인증

- 동물병원 및 보호센터 관계자의 인증은 국가동물보호정보시스템의 데이터를 기반으로 진행됩니다.
- 이를 통해 신뢰할 수 있는 정보 제공자만이 특별 권한을 가질 수 있도록 보장합니다.

### 🔄 자동화된 정보 업데이트

- Spring Batch를 활용하여 매일 새벽 1시와 2시에 자동으로 데이터를 업데이트합니다.
- 국가동물보호정보시스템에서 최신 동물병원 및 보호센터 정보를 가져와 데이터베이스를 갱신합니다.
- 이를 통해 사용자에게 항상 최신의 정확한 정보를 제공할 수 있습니다.
## 🚀 주요 기능

### 👤 계정 관리
- [x] 회원가입 (일반 / 간편가입: 카카오, 네이버, 구글)
- [x] 로그인 (일반 / 간편로그인: 카카오, 네이버, 구글)
- [x] 동물병원 / 보호센터 관계자 인증 절차
    - 국가동물보호정보시스템 데이터 기반 인증

### 🏠 마이페이지
- [x] 일반사용자에서 관계자로 권한 변경
- [x] 작성 게시글 확인
- [x] 안읽은 댓글 확인
- [x] 정보 업데이트
- [x] 로그아웃
- [x] 회원탈퇴

### 📢 게시판 기능
- [x] 동물병원 및 보호센터 관계자 전용 페이지
- [x] 실종 게시글 관리 (작성, 업데이트, 상태 변경)
- [x] 댓글 시스템 (작성, 삭제)

### 🔔 알림 기능
- [x] 관심 지역 선택 및 해당 지역 게시글 알림


## 🚀 배포 정보

이 프로젝트는 CloudType을 통해 배포되며, Git CI/CD를 사용하여 자동화된 배포 프로세스를 구현하고 있습니다.
## 🎨 디자인

프로젝트의 UI/UX 디자인은 Figma를 통해 구현되었습니다. 디자인 세부 사항은 [Figma 링크](https://www.figma.com/design/2Lqkxfy3c59ksHUiQCK6nj/PawAlert?node-id=0-1&t=uxyIHrYkqWhgKvSg-1)를 통해 확인할 수 있습니다.

## 📚 프로젝트 문서

프로젝트에 대한 자세한 정보와 개발 문서는 Notion에서 관리되고 있습니다. [Notion 문서](https://jinsung7605.notion.site/PawAlert-3309da778aeb4342ad733b6ba4089be9?pvs=4)를 통해 접근할 수 있습니다.

## 📊 API 문서

API 문서는 Swagger를 통해 제공됩니다. [API 문서](https://api.pawalert.co.kr/swagger-ui/index.html)에서 확인할 수 있습니다.



## 📞 연락처

프로젝트 관리자:
- 이메일: [dev.jinsung1017@gmail.com](mailto:dev.jinsung1017@gmail.com)
- 인스타그램: [@Joojinsung](https://www.instagram.com/j_m101707/)

프론트 Git: [https://github.com/orgs/PawAlert/repositories](https://github.com/orgs/PawAlert/repositories)