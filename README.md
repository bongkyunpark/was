# WAS 구현

자바로 간단한 WAS(Web Application Server)를 만들기

#### Dependency
* Open JDK 1.8
* maven


#### Install
##### 프로젝트 빌드 및 배포, 실행 가이드
다음과 같이 서버를 작동시키면 웹페지지를 localhost:80에서 확인가능합니다
(argument로 http_root폴더, port를 지정)
```shell
$ mvn clean package
$ java –jar was.jar ./bin/temp 7002
```