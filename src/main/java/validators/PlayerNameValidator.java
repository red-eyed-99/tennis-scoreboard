package validators;

import exceptions.PlayerNameValidationException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PlayerNameValidator {

    public void validate(String playerName) {
        playerName = playerName.stripTrailing();

        if (playerName.isBlank()) {
            throw new PlayerNameValidationException("Player name must not be empty");
        }

        if (playerName.length() > 40) {
            throw new PlayerNameValidationException("Player name must be no more than 40 characters");
        }

        if (!containsValidCharacters(playerName)) {
            throw new PlayerNameValidationException("Only Latin letters, spaces and the following symbols are allowed: ' ’ . -");
        }

        if (!startsWithLetter(playerName)) {
            throw new PlayerNameValidationException("Player name must start with a letter");
        }

        if (!specialCharsPlacedCorrectly(playerName)) {
            throw new PlayerNameValidationException("There must be a letter after special characters");
        }
    }

    private boolean containsValidCharacters(String playerName) {
        return playerName.matches("^[a-zA-Z'’.\\-\\s]+$");
    }

    private boolean startsWithLetter(String playerName) {
        return playerName.matches("^[a-zA-Z]+[a-zA-Z'’.\\-\\s]*$");
    }

    private boolean specialCharsPlacedCorrectly(String playerName) {
        return playerName.matches("[a-zA-Z]+([\\s\\-'’.][A-Za-z]+)*");
    }
}
