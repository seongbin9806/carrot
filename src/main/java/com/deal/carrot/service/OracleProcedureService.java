package com.deal.carrot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.Map;

@Service
public class OracleProcedureService {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcCall postDateProcedureCall;
    private final SimpleJdbcCall studentGradeProcedureCall;

    @Autowired
    public OracleProcedureService(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        // 저장 프로시저 이름으로 SimpleJdbcCall 초기화
        this.postDateProcedureCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("PostDateProcedure"); // 기존 저장 프로시저

        this.studentGradeProcedureCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("StudentGrade_Procedure"); // 새로운 저장 프로시저
    }

    public String callPostDateProcedure(int postNumber) {
        // 입력 파라미터 설정
        Map<String, Object> inParams = Map.of("post_number", postNumber);

        // 출력 파라미터 선언
        postDateProcedureCall.declareParameters(
                new SqlParameter("post_number", Types.NUMERIC), // 입력 파라미터
                new SqlOutParameter("post_result", Types.VARCHAR) // 출력 파라미터
        );

        // 입력 파라미터로 저장 프로시저 실행 및 결과 반환
        Map<String, Object> outParams = postDateProcedureCall.execute(inParams);

        // 출력 파라미터 값 반환
        return (String) outParams.get("post_result");
    }

    public String callStudentGradeProcedure(int studentNumber) {
        // 입력 파라미터 설정
        Map<String, Object> inParams = Map.of("student_number", studentNumber);

        // 출력 파라미터 선언
        studentGradeProcedureCall.declareParameters(
                new SqlParameter("student_number", Types.VARCHAR), // 입력 파라미터
                new SqlOutParameter("student_grade", Types.VARCHAR) // 출력 파라미터
        );

        // 입력 파라미터로 저장 프로시저 실행 및 결과 반환
        Map<String, Object> outParams = studentGradeProcedureCall.execute(inParams);

        // 출력 파라미터 값 반환
        return (String) outParams.get("student_grade");
    }
}

