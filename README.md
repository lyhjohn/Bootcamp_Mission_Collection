# 개요
간단한 커머스 프로젝트

목표: 셀러와 구매자 사이에서 중개해 주는 커머스 서버를 구축한다.

기술: Spring, Jpa, Mysql, Redis, Docker, AWS

## 회원
### 공통
- [x] 이메일 인증을 통한 회원가입

### 고객
- [x] 회원 가입
- [x] 인증 (이메일)
- [x] 로그인 토큰 발행
- [x] 로그인 토큰을 통한 제어(JWT, Filter 사용)
- [x] 예치금 관리

### 셀러
- [x] 회원 가입

---

## 주문 서버
### 셀러
- [x] 상품 등록 / 수정
- [x] 상품 삭제
- [x] 상품 조회

### 구매자
- [x] 장바구니를 위한 Redis 연동
- [x] 상품 검색 & 상세 페이지
- [x] 장바구니에 상품 추가
- [x] 장바구니 확인
- [ ] 주문하기 | 상품 품절, 금액 변동 등 
- [ ] 주문내역 이메일로 발송
