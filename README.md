# 회원 API

## 시스템 환경

* java8
* Spring boot 2.3.2

## 프로젝트 실행

### 1. DB 실행
```$script shell
docker-compose -f docker-compose-mysql.yml up -d
```

### 2. 프로젝트 싫행 후, Swagger-ui 접속
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)