package validators;

import exceptions.PlayerNameValidationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import utils.RequestAttributeNames;
import utils.RequestParameter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class RequestParameterValidator {

    public boolean parametersAreValid(HttpServletRequest request, List<RequestParameter> parameters) {
        var validationResults = new HashMap<RequestParameter, ValidationResult>();

        parameters.forEach(parameter -> validationResults.put(parameter, validatePlayerName(parameter.getValue())));

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
        if (requestParameterName.equals(RequestAttributeNames.FIRST_PLAYER_NAME)) {
            return RequestAttributeNames.FIRST_PLAYER_ERROR_MESSAGE;
        }

        return RequestAttributeNames.SECOND_PLAYER_ERROR_MESSAGE;
    }
}
