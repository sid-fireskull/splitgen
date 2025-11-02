package in.splitgen.exception;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthenticationException extends RuntimeException {
    private String message;
    private Object data;

}

