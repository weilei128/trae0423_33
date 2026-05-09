package com.jewelry.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Component
public class DatabaseInitializer implements ApplicationRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            System.out.println("开始初始化数据库...");
            
            // 读取 SQL 脚本并执行
            ClassPathResource resource = new ClassPathResource("schema.sql");
            String sqlScript = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)
            ).lines().collect(Collectors.joining("\n"));
            
            // 移除 USE db3 语句（因为我们已经连接到 db3）
            sqlScript = sqlScript.replaceFirst("CREATE DATABASE IF NOT EXISTS db3[^;]+;", "");
            sqlScript = sqlScript.replaceFirst("USE db3;", "");
            
            // 分割 SQL 语句并执行
            String[] sqlStatements = sqlScript.split(";");
            for (String sql : sqlStatements) {
                String trimmedSql = sql.trim();
                if (!trimmedSql.isEmpty() && !trimmedSql.startsWith("--")) {
                    try {
                        jdbcTemplate.execute(trimmedSql);
                    } catch (Exception e) {
                        System.err.println("执行 SQL 出错: " + e.getMessage());
                        System.err.println("SQL: " + trimmedSql);
                    }
                }
            }
            
            System.out.println("数据库初始化完成！");
        } catch (Exception e) {
            System.err.println("数据库初始化检查出错: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
