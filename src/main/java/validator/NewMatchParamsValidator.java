package validator;

import exceptions.PlayerNameValidationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

import static utils.NewMatchAttributeNames.*;

@UtilityClass
public class NewMatchParamsValidator {

    public boolean parametersAreValid(HttpServletRequest request, RequestParameter[] parameters) {
        var validationResults = new HashMap<RequestParameter, ValidationResult>();

        for (var parameter : parameters) {
            var validationResult = validatePlayerName(parameter.getValue());
            validationResults.put(parameter, validationResult);
        }

        configureRequestAttributes(request, validationResults);

        return validationResults.values().stream()
                .noneMatch(validationResult -> validationResult.getErrorMessage() != null);
    }

    private ValidationResult validatePlayerName(String playerName) {
        try {
            PlayerNameValidator.validate(playerName);
            return new ValidationResult(ValidationStatus.VALID);
        } catch (PlayerNameValidationException ex) {
            return new ValidationResult(ValidationStatus.INVALID, ex.getMessage());
        }
    }

    private void configureRequestAttributes(HttpServletRequest request,
                                            Map<RequestParameter, ValidationResult> validationResults) {

        for (var entry : validationResults.entrySet()) {
            var requestParameter = entry.getKey();
            var validationResult = entry.getValue();

            var requestParameterName = requestParameter.getName();
            var requestParameterValue = requestParameter.getValue();

            var errorMessageAttributeName = defineErrorMessageAttributeName(requestParameterName);

            request.setAttribute(requestParameterName, requestParameterValue);

            if (validationResult.getStatus() == ValidationStatus.VALID) {
                request.removeAttribute(errorMessageAttributeName);
            } else {
                request.setAttribute(errorMessageAttributeName, validationResult.getErrorMessage());
            }
        }
    }

    private String defineErrorMessageAttributeName(String requestParameterName) {

        if (requestParameterName.equals(FIRST_PLAYER_NAME)) {
            return FIRST_PLAYER_ERROR_MESSAGE;
        }

        return SECOND_PLAYER_ERROR_MESSAGE;
    }
}
