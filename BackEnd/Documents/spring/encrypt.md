# password加密

采用BCryptPasswordEncoder加密密码。(其实就一行，调用方法，然后没了)。

```java
@Component
public class PasswordEncoder {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public String encode(CharSequence rawPassword) {
        return encoder.encode(rawPassword);
    }

    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}
```

不过呢我们在BCrypt的源代码615行中看到了这么一句话：

```java
if (!for_check && passwordb.length > 72) {
    throw new IllegalArgumentException("password cannot be more than 72 bytes");
}
```

    密码的字节数不能超过72字节，所以我们需要注意一下。

    我给设置的是max = 64，留一定空间，防止出现问题。
