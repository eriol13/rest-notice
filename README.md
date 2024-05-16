# Spring Boot와 Spring Data JPA를 사용하여 구현한 REST API 공지사항 프로젝트
---

### 프로젝트 개요
  * Spring Boot와 Spring Data JPA를 활용하여 간단하게 구현해본 공지사항 REST API 서버

### 주요 기능

  #### 1. 공지사항 목록 조회
    - 엔드포인트 : `GET /v1/notices`
    - 요청 : 없음
    - 응답 :
      - `200 OK` : 성공적으로 목록 조회
  
  #### 2. 공지사항 상세 조회
    - 엔드포인트 : `GET /v1/notices/{id}`
    - 요청 : 공지사항의 ID(숫자)를 경로 매개변수로 전달
    - 응답 :
      - `200 OK` : 성공적으로 목록 조회
      - `404 Not Found` : 해당 ID로 조회되는 공지사항이 존재하지 않음
  
  #### 3. 공지사항 등록
    - 엔드포인트 : `POST /v1/notices`
    - 요청 : JSON 형식의 공지사항 데이터를 본문에 포함하여 전달
    - 응답 :
      - `201 Created` : 성공적으로 공지사항 정보 저장 성공
      - `400 Bad Request` : 공지사항 정보 저장 중 오류가 발생
     
  #### 4. 공지사항 수정
    - 엔드포인트 : `PUT /v1/notices/{id}`
    - 요청 : 공지사항의 ID(숫자)를 경로 매개변수로 전달하고 JSON 형식의 공지사항의 수정할 데이터를 본문에 포함하여 전달
    - 응답 :
      - `201 Created` : 성공적으로 공지사항 정보 저장 성공
      - `400 Bad Request` : 공지사항 정보 수정 중 오류가 발생
      - `404 Not Found` : 매개변수로 전달받은 ID로 조회되는 공지사항 정보가 존재하지 않음
     
  #### 5. 공지사항 삭제
    - 엔드포인트 : `DELETE /v1/notices/{id}`
    - 요청 : 공지사항의 ID(숫자)를 경로 매개변수로 전달
    - 응답 :
      - `204 No Content` : 성공적으로 공지사항 정보가 삭제
      - `404 Not Found` : 매개변수로 전달받은 ID로 조회되는 공지사항 정보가 존재하지 않음

### 실행 방법

  ##### 1. PC 혹은 사용하는 IDE에 jdk 21을 설치
  ##### 2. MariaDB 11.4 이상 버전을 설치(11.4 버전은 LTS 버전. 참고사이트 : <https://mariadb.org/11-4-lts/>) 
> 기본 charset은 utf8mb4_general_ci
  ##### 3. MariaDB 접속하여 아래의 방법으로 간단하게 계정 생성 및 권한 부여
    1) CREATE DATABASE `test`; -- DB 생성
    2) create USER 'test'@'%' IDENTIFIED BY 'test1234!'; -- 계정 생성
    3) GRANT ALL PRIVILEGES ON test.* TO 'test'@'%'; -- 2에서 생성한 계정에 1에서 생성한 DB에 대한 접근 권한 부여
    4) flush PRIVILEGES; 로 4의 권한 부여 적용
> 현재 Spring Boot의 ddl-auto값이 update로 적용되어져 있기 때문에 간단한 테스트 용도로는 따로 테이블 생성SQL 없이 실행 및 기능 확인 가능

  ##### 4. IDE를 사용중이라면 IDE를 사용하여 RestApplication.java를 실행 시키고 아니라면 gradlew를 이용하여 build한 뒤, jar파일을 실행시킨다.
  ##### 5. 이후 테스트


### 핵심 문제해결 전략

  ##### 대용량 트래픽을 고려하였지만 '공지사항' 이기 때문에 다른 WebFlux나 Redis와 같은 기능은 사용하지 않고 Tomcat에서 제공하는 Virtual Threads를 활용
  ##### 다수의 동시 조회 시, 조회수를 증가하는 update쿼리로 인하여 조금씩 딜레이가 생길 수도 있는 점을 고려하여 조회정보를 저장하는 테이블을 따로 구현하고 해당 테이블을 noticeId로 count() 조회하여 조회수를 노출하는 방법으로 구현
  ##### 조회정보를 저장하는 메소드는 @Async Annotation을 사용하여 비동기로 호출 및 작동하게 하여서 select에 딜레이가 생기지 않도록 조치
