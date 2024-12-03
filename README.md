# carrot
DataBaseProgramming - JavaSpring(carrot)🥕

# Oracle Database Setup for Carrot Project
## Creating User and Granting Permissions

먼저 Oracle 데이터베이스에서 사용자 계정을 생성하고 권한을 부여합니다.

```sql
-- SYSTEM에서 작업
ALTER session set "_ORACLE_SCRIPT"=true;

CREATE USER CARROT IDENTIFIED BY 1234  -- 사용자 ID: carrot, 비밀번호: 1234
    DEFAULT TABLESPACE USERS
    TEMPORARY TABLESPACE TEMP;
GRANT connect, resource, dba TO CARROT; -- 권한 부여

Running Spring Application
계정 생성 작업 후 Spring 애플리케이션을 실행하고, localhost:8080에 접속하여 테이블 생성 로그가 뜰 때까지 새로고침을 반복합니다.

Inserting Data Example - Using Sequences
게시글 삽입 예제
게시글을 삽입하는 예제입니다. 게시글번호는 시퀀스를 통해 자동으로 생성됩니다.

SELECT * FROM 게시글;

INSERT INTO 게시글 (게시글번호, 학번, 학과명, 카테고리명, 거래자, 거래금액, 물품명, 물품설명, 거래여부, 등록날짜)
VALUES (POST_SEQUENCE.NEXTVAL, 12345, 101, 202, 67890, 50000, 'Laptop', 'Brand new laptop', 'N', TO_DATE('2024-12-04 10:00:00', 'YYYY-MM-DD HH24:MI:SS'));

SELECT * FROM 게시글;

쪽지 삽입 예제
쪽지를 삽입하는 예제입니다. 쪽지번호는 시퀀스를 통해 자동으로 생성됩니다.

sql
코드 복사
SELECT * FROM 쪽지;

INSERT INTO 쪽지 (쪽지번호, 게시글번호, 학번, 제목, 내용, 쪽지일자)
VALUES (NOTE_SEQUENCE.NEXTVAL, 1, 20223978, '제목', '내용', TO_DATE('2024-12-04 10:00:00', 'YYYY-MM-DD HH24:MI:SS'));

SELECT * FROM 쪽지;

Dropping Tables
필요시 사용하기 위한 테이블 삭제 쿼리입니다.

DROP TABLE "게시글" CASCADE CONSTRAINTS;
DROP TABLE "즐겨찾기" CASCADE CONSTRAINTS;
DROP TABLE "쪽지" CASCADE CONSTRAINTS;
DROP TABLE "카테고리" CASCADE CONSTRAINTS;
DROP TABLE "학과" CASCADE CONSTRAINTS;
DROP TABLE "학생" CASCADE CONSTRAINTS;
