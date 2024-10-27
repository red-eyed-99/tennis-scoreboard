package testutils;

import lombok.AllArgsConstructor;
import models.CalculationMethod;
import models.MatchScore;
import models.PlayerScore;

@AllArgsConstructor
public class MatchScoreConfigurator {

    private final MatchScore matchScore;

    private final PlayerScore wonPointPlayerScore;
    private final PlayerScore anotherPlayerScore;

    public void setAllMatchScore(int wonPointPlayerPoints, int anotherPlayerPoints) {
        wonPointPlayerScore.setPoints(wonPointPlayerPoints);
        anotherPlayerScore.setPoints(anotherPlayerPoints);
    }

    public void setDeuceMatchScore() {
        wonPointPlayerScore.setPoints(40);
        anotherPlayerScore.setPoints(40);

        matchScore.setCalculationMethod(CalculationMethod.DEUCE);
    }

    public void setAdvantageMatchScore(boolean wonPointPlayerHasAdvantage) {
        wonPointPlayerScore.setPoints(40);
        anotherPlayerScore.setPoints(40);

        if (wonPointPlayerHasAdvantage) {
            wonPointPlayerScore.setAdvantage(true);
        } else {
            anotherPlayerScore.setAdvantage(true);
        }

        matchScore.setCalculationMethod(CalculationMethod.ADVANTAGE);
    }

    public void setGameScore(int wonPointPlayerGamePoints, int anotherPlayerGamePoints) {
        wonPointPlayerScore.setGamePoints(wonPointPlayerGamePoints);
        anotherPlayerScore.setGamePoints(anotherPlayerGamePoints);
    }

    public void setTiebreakMatchScore(int wonPointPlayerTiebreakPoints, int anotherPlayerTiebreakPoints) {
        wonPointPlayerScore.setGamePoints(6);
        anotherPlayerScore.setGamePoints(6);

        wonPointPlayerScore.setTiebreakPoints(wonPointPlayerTiebreakPoints);
        anotherPlayerScore.setTiebreakPoints(anotherPlayerTiebreakPoints);

        matchScore.setCalculationMethod(CalculationMethod.TIEBREAK);
    }
}
