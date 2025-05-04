# 선착순 응모 테스트

이 레포지토리는 선착순 응모 시스템의 부하 테스트를 위한 코드베이스입니다.
[POV](https://github.com/leeseunghee00/pov-backend) 프로젝트 응모 시스템의 결제 기능을 제외한 핵심 로직을 중심으로 구성되어 있으며, 동시성 환경에서도 안정적으로 동작하는지 테스트할 수 있도록 설계되었습니다.
동시성 제어와 관련된 자세한 내용은 [해당 블로그 글](https://velog.io/@leeseunghee00/선착순-응모-시스템의-DB-정합성-보장을-위한-트러블-슈팅)을 참고해주세요. 

<br />

> 프로젝트 환경

- Java17
- Docker MySQL 8.x, Redis
- JMeter, nGrinder (test tools)

<br />

> 테스트 결과

- race condition 발생 : `synchronized`, `optimistic lock`, `pessimistic lock`

![image](https://github.com/user-attachments/assets/83960145-29ae-4b5c-a175-0db2fe480880)

![image](https://github.com/user-attachments/assets/ee01eae2-cb78-44b2-b023-3976e49cc9e3)

![image](https://github.com/user-attachments/assets/92660c28-39f9-4437-a487-2467b86211f5)


- 데이터 정합성 보장 : `redisson (distribute lock)`
  
![image](https://github.com/user-attachments/assets/c6924f96-9fce-4d69-87a2-34a73da59071)
