# 작업 히스토리

트러블슈팅, 주요 결정, 변경 이력을 날짜 역순으로 기록한다.

---

## 2026-06-10

### Thymeleaf `th:each` 반복 변수로 예약어(`gt`) 사용 시 "Iteration variable cannot be null" 오류

**문제**
`client/list.html`에서 중첩 `th:each`를 사용할 때 500 에러 발생.

```
java.lang.IllegalArgumentException: Iteration variable cannot be null
  at Each.<init>(Each.java:49)
  at EachUtils.composeEach(EachUtils.java:169)
  at EachUtils.internalParseEach(EachUtils.java:94)
```

**원인**
`th:each="gt : ${...}"` 에서 반복 변수 이름 `gt`가 Thymeleaf 표현식의 **예약 연산자 토큰**이었음.

`GreaterLesserExpression.OPERATORS`에 `"gt"` (HTML-safe `>` 대체 표현)가 포함되어 있어, `ExpressionParsingUtil.parseAndCompose`가 `"gt"`를 이항 연산자로 인식하고 `null`을 반환함.

`EachUtils.java:150`에 별도 버그가 겹침 — `iterVarExpr == null` 체크를 해야 할 자리에 `iterVarStr == null`을 체크하는 코드가 있어 null이 그대로 `Each` 생성자에 전달됨.

Thymeleaf 예약 토큰 전체 목록: `gt`, `lt`, `ge`, `le`, `eq`, `ne`, `and`, `or`, `not`, `div`, `mod`

**해결 방법**
반복 변수 이름을 예약어가 아닌 이름으로 변경.

```html
<!-- 수정 전 -->
<span th:each="gt : ${client.grantTypeList()}" th:text="${gt}">

<!-- 수정 후 -->
<span th:each="grantType : ${client.grantTypeList()}" th:text="${grantType}">
```

채택하지 않은 방법: `ClientListView` 뷰 전용 DTO를 만들어 컨트롤러에서 미리 `List<String>`으로 변환 후 전달 → 원인 파악 후 롤백.

**결과**
`list.html`의 반복 변수 `gt` → `grantType`, `sc` → `scope` 로 변경하여 해결.

---

<!-- 아래 템플릿을 복사해서 사용 -->

<!--
## YYYY-MM-DD

### 제목

**문제**
(어떤 문제가 발생했는지)

**원인**
(왜 발생했는지)

**해결 방법**
(어떻게 해결했는지, 채택하지 않은 방법이 있으면 함께 기록)

**결과**
(PR 번호, 확인 방법 등)

---
-->
