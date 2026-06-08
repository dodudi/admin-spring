# Controller 규칙

이 파일은 Controller 작성 시 항상 따라야 할 규칙을 정의한다.

---

## @RestController 기본 구조

클래스 선언부에 `@RestController`, `@RequestMapping`, `@RequiredArgsConstructor` 세 어노테이션을 함께 사용한다.
의존성은 `private final` 필드로만 선언한다.

```java
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(userService.findById(id)));
    }
}
```

```java
// ❌ 잘못된 예
@Controller                          // @ResponseBody 없이 사용
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired                       // 필드 주입 금지
    private UserService userService;

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {   // Entity 직접 반환
        return userRepository.findById(id).get();  // Repository 직접 호출
    }
}
```
