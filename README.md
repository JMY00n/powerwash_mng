<img width="700" alt="main" src="https://github.com/user-attachments/assets/1f58ee5b-051b-4e8e-ad0e-b277c1d85f11" />

# POWER WASH 생산관리 ERP

고압 세척기 생산 현장을 대상으로 한 **소규모 생산관리(ERP) 데스크톱 프로그램**입니다.
Java Swing과 MySQL을 이용해 로그인, 제품/부품 재고관리, 생산 작업 일지 기록 기능을 구현했습니다.

## 목차

- [프로젝트 개요](#1-프로젝트-개요)
- [기술 스택](#2-기술-스택)
- [주요 기능](#3-주요-기능)
- [데이터베이스 구조](#4-데이터베이스-구조요약)

---

## 1. 프로젝트 개요

20살 때 고압 세척기 생산 공장에서 근무하면서,
하루 생산량을 수기로 작성 → 다시 엑셀로 옮기는 비효율적인 과정을 직접 경험했습니다.

그 경험을 바탕으로,

- **현장에서 바로 사용할 수 있는 간단한 생산관리 툴**
- **작업자의 사번(ID)을 기준으로 생산 이력을 남길 수 있는 시스템**

을 목표로 이 프로젝트를 진행했습니다.

---

## 2. 기술 스택

- **Language** : Java (JDK 21 기준)
- **IDE** : Eclipse
- **GUI** : Java Swing
- **Database** : MySQL 8.x
- **JDBC Driver** : MySQL Connector/J

---

## 3. 주요 기능

### 3-1. 로그인

- 사용자 ID(사번)와 비밀번호로 로그인
- MySQL `user` 테이블과 연동하여 검증
- 성공 시 "로그인 성공" 다이얼로그 후 메인 화면으로 이동
- 실패 시 에러 메시지 후 다시 로그인 가능

<img width="350" alt="로그인 화면" src="https://github.com/user-attachments/assets/c626abbe-27b2-48ab-93eb-96d2e752db89" />

### 3-2. 메인 화면(Home)

- 좌측 네비게이션 바
  - **홈**
  - **전체 제품**
  - **전기식**
  - **연료식**
  - **부품**
  - **생산 일지**
- 우측 메인 영역에서 각 메뉴별 화면을 스위칭

<img width="700" alt="메인 화면" src="https://github.com/user-attachments/assets/eb5c9d25-0211-43c4-af79-19432c984320" />

### 3-3. 제품 관리

- 제품 목록 카드 UI
- 카테고리(전체 / 전기식 / 연료식) 필터링
- **상세보기** : 해당 제품의 구성 부품(레시피) 조회
- **생산하기** : 생산 수량 입력 시
  - 레시피 기준으로 필요한 부품 수량 계산
  - 부품 재고가 충분한 경우 → 부품 재고 차감, 제품 재고 증가
  - 부족한 부품이 있는 경우 → 부족한 부품명, 부족 개수를 "영수증" 형태로 팝업 표시

<table>
  <tr>
    <td align="center">
      <img width="400" alt="연료식 필터" src="https://github.com/user-attachments/assets/3ea725eb-0ae6-42a3-9b62-ca7bb6a5910b" /><br/>
      <sub>연료식 필터</sub>
    </td>
    <td align="center">
      <img width="400" alt="전기식 필터" src="https://github.com/user-attachments/assets/58900f54-e254-404c-bd43-ed4c2072b7c0" /><br/>
      <sub>전기식 필터</sub>
    </td>
  </tr>
</table>

<img width="500" alt="상세보기 - 구성 부품 조회" src="https://github.com/user-attachments/assets/44180555-3a82-46ad-ab83-835bd953e6f2" /><br/>
<sub>상세보기 — 제품의 구성 부품(레시피) 조회</sub>

<img width="350" alt="생산 수량 입력" src="https://github.com/user-attachments/assets/ddb70d73-831c-47f3-91d3-a961a583add4" /><br/>
<sub>생산하기 — 생산 수량 입력</sub>

<img width="700" alt="생산 전후 재고 반영" src="https://github.com/user-attachments/assets/837121b1-5787-44a2-91bc-e6f3ae50d35f" /><br/>
<sub>생산 완료 후 재고가 즉시 반영되는 모습</sub>

<img width="350" alt="부품 재고 부족 시 영수증 팝업" src="https://github.com/user-attachments/assets/d1923c11-4d16-4f8e-bf68-b03ab4e81d73" /><br/>
<sub>부품 재고 부족 시 "영수증" 형태로 안내</sub>

### 3-4. 부품 관리

- 각 부품에 대해 이미지, 이름, 현재 재고 표시
- **주문하기** : 주문 수량 입력 시 재고 증가
- **상세보기** : 제조사, 공급사, 단가, 안전재고, 설명 등 상세 정보 확인

<img width="700" alt="부품 목록" src="https://github.com/user-attachments/assets/7910424d-0272-4f11-be6b-15650debe16d" /><br/>
<sub>부품 목록 화면</sub>

<img width="500" alt="부품 주문하기" src="https://github.com/user-attachments/assets/3b8ec9a6-9bd1-45aa-85f0-955c6e782126" /><br/>
<sub>주문하기 — 주문 수량 입력 시 재고 증가</sub>

<img width="500" alt="부품 상세정보" src="https://github.com/user-attachments/assets/ec2a48e3-ea63-4520-8906-ecc04f44fab5" /><br/>
<sub>상세보기 — 제조사·공급사·단가·안전재고 확인</sub>

### 3-5. 생산 작업 일지(Production Log)

- 로그인한 사용자 ID(사번)를 기준으로 생산 기록 저장
- 어떤 작업자가 / 어떤 제품을 / 몇 개 / 언제 생산했는지 조회 가능
- 일자/제품/사용자 기준으로 간단한 추적이 가능하도록 설계

<img width="700" alt="생산 작업 일지" src="https://github.com/user-attachments/assets/5a0866cc-5014-4596-9d0b-5f2b4ae21f3e" />

---

## 4. 데이터베이스 구조(요약)

주요 테이블:

- `products`
  - `product_id`, `name`, `type`, `stock`, `image_name`
- `parts`
  - `part_id`, `name`, `stock`, `image_name`,
    `manufacturer`, `supplier`, `unit_price`, `safety_stock`, `description`
- `production_recipes`
  - `product_id`, `part_id`, `quantity_needed`
- `users`
  - `user_id`(사번), `name`, `password` …
