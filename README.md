# Spring Boot와 Spring Data JPA를 사용하여 구현한 REST API 공지사항 프로젝트
---

### 프로젝트 개요
* Spring Boot와 Spring Data JPA를 활용하여 간단하게 구현해본 공지사항 REST API 서버
* 

### 주요 기능

####1. 공지사항 목록 조회
  - 엔드포인트 : `GET /v1/notices`
  - 요청 : 없음
  - 응답 :
    - `200 OK` : 성공적으로 목록 조회

####2. 공지사항 상세 조회
  - 엔드포인트 : `GET /v1/notices/{id}`
  - 요청 : 공지사항의 ID(숫자)를 경로 매개변수로 전달
  - 응답 :
    - `200 OK` : 성공적으로 목록 조회
    - `404 Not Found` : 해당 ID로 조회되는 공지사항이 존재하지 않음

####3. 공지사항 등록
  - 엔드포인트 : `POST /v1/notices`
  - 요청 : JSON 형식의 공지사항 데이터를 본문에 포함하여 전달
  - 응답 :
    - `201 Created` : 성공적으로 공지사항 정보 저장 성공
    - `400 Bad Request` : 공지사항 정보 저장 중 오류가 발생
   
####4. 공지사항 수정
  - 엔드포인트 : `PUT /v1/notices/{id}`
  - 요청 : 공지사항의 ID(숫자)를 경로 매개변수로 전달하고 JSON 형식의 공지사항의 수정할 데이터를 본문에 포함하여 전달
  - 응답 :
    - `201 Created` : 성공적으로 공지사항 정보 저장 성공
    - `400 Bad Request` : 공지사항 정보 수정 중 오류가 발생
    - `404 Not Found` : 매개변수로 전달받은 ID로 조회되는 공지사항 정보가 존재하지 않음
   
####5. 공지사항 삭제
  - 엔드포인트 : `DELETE /v1/notices/{id}`
  - 요청 : 공지사항의 ID(숫자)를 경로 매개변수로 전달
  - 응답 :
    - `204 No Content` : 성공적으로 공지사항 정보가 삭제
    - `404 Not Found` : 매개변수로 전달받은 ID로 조회되는 공지사항 정보가 존재하지 않음
