# carrot

DataBaseProgramming - JavaSpring(carrot)🥕

# Oracle Database Setup for Carrot Project

## Creating User and Granting Permissions

먼저 Oracle 데이터베이스에서 사용자 계정을 생성하고 권한을 부여합니다.

```sql
ALTER session set "_ORACLE_SCRIPT"=true;

-- 사용자 생성
CREATE USER CARROT IDENTIFIED BY 1234  -- 사용자 ID: carrot, 비밀번호: 1234
    DEFAULT TABLESPACE USERS
    TEMPORARY TABLESPACE TEMP;
GRANT connect, resource, dba TO CARROT; -- 권한 부여

----------------------------------------------------------------
-------------------- 데이터 및 테이블 삭제  ---------------------- 
----------------------------------------------------------------
DROP TABLE 게시글 CASCADE CONSTRAINTS;
DROP TABLE 즐겨찾기 CASCADE CONSTRAINTS;
DROP TABLE 쪽지 CASCADE CONSTRAINTS;
DROP TABLE 카테고리 CASCADE CONSTRAINTS;
DROP TABLE 학과 CASCADE CONSTRAINTS;
DROP TABLE 학생 CASCADE CONSTRAINTS;

----------------------------------------------------------------
------------------------ 저장 프로시저  -------------------------- 
----------------------------------------------------------------

-- 물품 게시글 작성 날짜 표시 저장 프로시저
create or replace NONEDITIONABLE PROCEDURE PostDateProcedure(
    post_number IN NUMBER,      -- 게시글번호 입력
    post_result OUT VARCHAR2    -- 계산 결과 출력
)
AS
    post_date DATE;      -- 게시글 작성 날짜를 저장할 변수
    days_diff NUMBER;    -- 현재 날짜와 게시글 작성 날짜의 차이를 저장할 변수
BEGIN
    -- 게시글 작성 날짜 조회
    SELECT 등록날짜 INTO post_date
    FROM 게시글
    WHERE 게시글번호 = post_number;

    -- 날짜 차이 계산
    -- TRUNC는 시간 정보를 제외한 날짜 정보만 볼 수 있음
    days_diff := TRUNC(SYSDATE) - TRUNC(post_date);

    -- 조건에 따라 결과 반환
    IF days_diff = 0 THEN
        post_result := '오늘';
    ELSIF days_diff = 1 THEN
        post_result := '어제';
    ELSIF days_diff > 2 AND days_diff <= 7 THEN
        post_result := days_diff || '일 전';
    ELSE
        -- 7일을 초과하면 게시 날짜로 출력
        -- 반환 타입이 VARCHER2이므로 타입 변환 필요
        -- post_date은 현재 DATE 타입이므로 TO_CHAR을 사용해 문자열 타입으로 변환
        -- 시간은 필요 없으므로 'YYYY-MM-DD'
        post_result := TO_CHAR(post_date, 'YYYY-MM-DD');
    END IF;
END;

-- 거래완료 횟수에 따른 학생 등급 출력 저장 프로시저
CREATE OR REPLACE PROCEDURE StudentGrade_Procedure(
    student_number IN VARCHAR2, -- 학번 입력
    student_grade OUT VARCHAR2  -- 학생 등급 출력
)
AS
    completed_count NUMBER;     -- 거래완료 횟수를 저장 할 변수
BEGIN
    -- 거래 완료 횟수 조회
    SELECT COUNT(*) INTO completed_count
    FROM 게시글
    WHERE 학번 = student_number AND 거래여부 = 'Y';
    
    -- 등급 결정
    IF completed_count <= 1 THEN
        student_grade := 'bronze';
    ELSIF completed_count <= 3 THEN
        student_grade := 'sliver';
    ELSIF completed_count <= 5 THEN
        student_grade := 'gold';
    ELSIF completed_count <= 7 THEN
        student_grade := 'platinum';
    ELSE
        student_grade := 'diamond';
    END IF;
END;

----------------------------------------------------------------
--------------------------- 트리거  ----------------------------- 
----------------------------------------------------------------

-- 게시글에 쪽지를 보낼 때 해당 게시글을 즐겨찾기에 추가
create or replace NONEDITIONABLE TRIGGER add_favorite_on_message
    AFTER INSERT 
    ON 쪽지
    FOR EACH ROW
BEGIN
    -- 해당 게시글이 이미 즐겨찾기에 추가되어 있는지 확인
    DECLARE
        favorite_count INTEGER;
    BEGIN
        SELECT COUNT(*)
        INTO favorite_count
        FROM 즐겨찾기
        WHERE 학번 = :NEW.학번
          AND 게시글번호 = :NEW.게시글번호;

        -- 즐겨찾기에 추가되지 않은 경우에만 추가
        IF favorite_count = 0 THEN
            INSERT INTO 즐겨찾기(즐겨찾기번호, 학번, 게시글번호)
            VALUES (FAVORITES_SEQUENCE.NEXTVAL, :NEW.학번, :NEW.게시글번호);
        END IF;
    END;
END;

-- 즐겨찾기가 추가 됐을 시, 게시글의 즐겨찾기 횟수를 1회 증가
create or replace NONEDITIONABLE TRIGGER increment_favorite_count_on_add
    AFTER INSERT ON 즐겨찾기
    FOR EACH ROW
BEGIN
    -- 즐겨찾기가 추가되었을 때, 해당 게시글의 즐겨찾기 횟수 증가
    UPDATE 게시글
    SET 즐겨찾기수 = 즐겨찾기수 + 1
    WHERE 게시글번호 = :NEW.게시글번호;
END;


-- 즐겨찾기가 삭제되었을 시, 게시글의 즐겨찾기 횟수를 1회 감소
create or replace NONEDITIONABLE TRIGGER increment_favorite_count_on_remove
AFTER DELETE ON 즐겨찾기
FOR EACH ROW
DECLARE
    favorite_count NUMBER;  -- 즐겨찾기횟수를 저장할 변수
BEGIN
    -- 삭제되는 즐겨찾기에 게시글번호를 favorite_count 변수에 저장
    SELECT 즐겨찾기수 INTO favorite_count
    FROM 게시글
    WHERE 게시글번호 = :OLD.게시글번호;

    -- 즐겨찾기횟수가 음수가 되면 안 되기 때문에 0 보다 클 때만 수행
    IF favorite_count > 0 THEN
        UPDATE 게시글
        SET 즐겨찾기수 = 즐겨찾기수 - 1
        WHERE 게시글번호 = :OLD.게시글번호;
    END IF;
END;

-- 학생이 게시글 등록 시 게시글등록수 1회 증가(insert), 삭제시 1회 감소
create or replace NONEDITIONABLE TRIGGER post_count
    AFTER INSERT OR DELETE ON 게시글
    FOR EACH ROW
BEGIN
    -- INSERT 이벤트: 게시글등록수 +1
    IF INSERTING THEN
        UPDATE 학생
        SET 게시글등록수 = 게시글등록수 + 1
        WHERE 학번 = :NEW.학번;
    END IF;

    -- DELETE 이벤트: 게시글등록수 -1
    IF DELETING THEN
        UPDATE 학생
        SET 게시글등록수 = 게시글등록수 - 1
        WHERE 학번 = :OLD.학번;
    END IF;
END;

-- 학생이 탈퇴를 했을경우 즐겨찾기 삭제 및 게시글 비활성화 처리
create or replace NONEDITIONABLE TRIGGER student_exit
AFTER UPDATE OF 탈퇴여부 ON 학생
FOR EACH ROW
BEGIN
    -- 탈퇴여부가 'Y'로 변경된 경우 실행
    IF :NEW.탈퇴여부 = 'Y' THEN
        DELETE FROM 즐겨찾기 WHERE 학번 = :NEW.학번;      
        UPDATE 게시글 SET 게시글활성여부 = 'N' WHERE 학번 = :NEW.학번;
    END IF;
END;