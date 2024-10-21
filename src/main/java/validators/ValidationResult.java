package validators;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class ValidationResult {

    private final ValidationStatus status;

    private String errorMessage;
}
