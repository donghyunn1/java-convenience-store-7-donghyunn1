# java-convenience-store-precourse


## 기능 구현 목록

### 파일 입출력
- [x] 파일을 읽어오는 기능 구현

### 입력 기능
- [x] 구입할 상품과 수량을 입력받음
- [x] 프로모션 적용이 가능한 상품에 대해 그 수량만큼 추가 여부를 입력받는다.
- [] 재고 부족하여 프로모션 없이 결제해야하는 경우, 일부 수량에 대해 정가로 결제할지 여부를 입력받는다.
- [] 멤버십 할인 적용 여부를 입력받는다.
- [] 추가 구매 여부를 입력받는다.


### 출력 기능
- [x] 환영인사와 함께 재고를 출력한다.
- [x] 프로모션 적용이 가능한 상품에 대해 해당 수량 만큼 가지고 오지 않았을 경우 안내 메시지 출력
- [x] 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제할지 여부에 대한 안내 메시지를 출력한다.
- [] 멤버십 할인 적용 여부를 확인하기 위해 안내 문구를 출력한다.
- [] 구매 상품 내역, 증정 상품 내역, 금액 정보를 출력한다.


### 도메인

- [] 각 상품의 재고를 파악하여 결재 가능한지 판단하는 기능 
- [x] 고객이 상품을 구매할 때, 결제된 수량만큼 재고에서 차감하는 기능
- [x] 차감된 재고만큼 재고를 업데이트 하는 기능

- [] 오늘 날짜가 프로모션 날짜기간내에 포함되어 있는지 확인하는 기능
- [] 프로모션 재고가 부족하면 일반 재고에서 꺼내 사용하는 기능


### 멤버십

- [] 멤버십에 가입한 회원인지 확인하는 기능
  - [] 회원이라면 프로모션 미적용 금액의 30% 할인하는 기능
  - [] 프로모션 적용후 남은 금액에 대해 멤버십 할인 적용
  - [] 멤버십 할인의 최대한도는 8000원


### 영수증
- [] 총 구매액, 행사할인, 멤버십 할인, 내실 돈의 구성으로 영수증 정보 저장
