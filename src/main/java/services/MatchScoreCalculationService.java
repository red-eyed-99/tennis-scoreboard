package services;

import models.CalculationMethod;
import models.MatchScore;
import models.PlayerScore;

public class MatchScoreCalculationService {

    private final MatchScore matchScore;

    private final CalculationMethod calculationMethod;

    private final PlayerScore wonPointPlayerScore;

    private final PlayerScore firstPlayerScore;
    private final PlayerScore secondPlayerScore;

    public MatchScoreCalculationService(MatchScore matchScore, PlayerScore wonPointPlayerScore) {
        this.matchScore = matchScore;

        calculationMethod = matchScore.getCalculationMethod();

        firstPlayerScore = matchScore.getFirstPlayerScore();
        secondPlayerScore = matchScore.getSecondPlayerScore();

        this.wonPointPlayerScore = wonPointPlayerScore;
    }

    public void updateMatchScore() {
        if (calculationMethod == CalculationMethod.TIEBREAK) {
            updateScoreOnTiebreak();
            return;
        }

        updatePointsAndGames();
        updateSetPoints();
    }

    private void updatePointsAndGames() {
        if (calculationMethod == CalculationMethod.ALL) {
            updateScoreOnAll();
        } else if (calculationMethod == CalculationMethod.DEUCE) {
            updateScoreOnDeuce();
        } else if (calculationMethod == CalculationMethod.ADVANTAGE) {
            updateScoreOnAdvantage();
        }
    }

    private void updateSetPoints() {
        var firstPlayerGamesNumber = firstPlayerScore.getGamePoints();
        var secondPlayerGamesNumber = secondPlayerScore.getGamePoints();

        if (firstPlayerGamesNumber == 6 || secondPlayerGamesNumber == 6) {
            if (hasSetWinner(firstPlayerGamesNumber, secondPlayerGamesNumber)) {
                increaseScoreSetPoints();
                resetScoreGamePoints();
                return;
            } else if (firstPlayerGamesNumber == 6 && secondPlayerGamesNumber == 6) {
                matchScore.setCalculationMethod(CalculationMethod.TIEBREAK);
                return;
            }
        }

        if (firstPlayerGamesNumber == 7 || secondPlayerGamesNumber == 7) {
            increaseScoreSetPoints();
            resetScoreGamePoints();
        }
    }

    private void updateScoreOnAll() {
        var currentPlayerPoints = wonPointPlayerScore.getPoints();

        if (currentPlayerPoints != 40) {
            increaseScorePoints();
        } else if (hasGameWinner()) {
            increaseScoreGamePoints();
            resetScorePoints();
        } else {
            wonPointPlayerScore.setAdvantage(true);
            matchScore.setCalculationMethod(CalculationMethod.ADVANTAGE);
        }
    }

    private void updateScoreOnDeuce() {
        wonPointPlayerScore.setAdvantage(true);
        matchScore.setCalculationMethod(CalculationMethod.ADVANTAGE);
    }

    private void updateScoreOnAdvantage() {
        if (wonPointPlayerScore.hasAdvantage()) {
            increaseScoreGamePoints();
            resetScorePoints();
            resetScoreAdvantages();
            matchScore.setCalculationMethod(CalculationMethod.ALL);
        } else {
            resetScoreAdvantages();
            matchScore.setCalculationMethod(CalculationMethod.DEUCE);
        }
    }

    private void updateScoreOnTiebreak() {
        increaseScoreTiebreakPoints();

        var firstPlayerTiebreakPoints = firstPlayerScore.getTiebreakPoints();
        var secondPlayerTiebreakPoints = secondPlayerScore.getTiebreakPoints();

        if (firstPlayerTiebreakPoints >= 7 || secondPlayerTiebreakPoints >= 7) {
            if (Math.abs(firstPlayerTiebreakPoints - secondPlayerTiebreakPoints) >= 2) {
                increaseScoreSetPoints();
                resetScoreGamePoints();
                resetScoreTiebreakPoints();
                matchScore.setCalculationMethod(CalculationMethod.ALL);
            }
        }
    }

    private void increaseScorePoints() {
        var currentPlayerPoints = wonPointPlayerScore.getPoints();

        if (currentPlayerPoints < 30) {
            wonPointPlayerScore.setPoints(currentPlayerPoints + 15);
        } else {
            wonPointPlayerScore.setPoints(currentPlayerPoints + 10);
        }
    }

    private void increaseScoreGamePoints() {
        var currentGamePoints = wonPointPlayerScore.getGamePoints();
        wonPointPlayerScore.setGamePoints(currentGamePoints + 1);
    }

    private void increaseScoreSetPoints() {
        var currentSetPoints = wonPointPlayerScore.getSetPoints();
        wonPointPlayerScore.setSetPoints(currentSetPoints + 1);
    }

    private void increaseScoreTiebreakPoints() {
        var currentTiebreakPoints = wonPointPlayerScore.getTiebreakPoints();
        wonPointPlayerScore.setTiebreakPoints(currentTiebreakPoints + 1);
    }

    private boolean hasGameWinner() {
        var firstPlayerPoints = firstPlayerScore.getPoints();
        var secondPlayerPoints = secondPlayerScore.getPoints();

        return firstPlayerPoints != secondPlayerPoints;
    }

    private boolean hasSetWinner(int firstPlayerGamePoints, int secondPlayerGamePoints) {
        return firstPlayerGamePoints == 6 && secondPlayerGamePoints < 5
                || firstPlayerGamePoints < 5 && secondPlayerGamePoints == 6;
    }

    private void resetScorePoints() {
        firstPlayerScore.setPoints(0);
        secondPlayerScore.setPoints(0);
    }

    private void resetScoreGamePoints() {
        firstPlayerScore.setGamePoints(0);
        secondPlayerScore.setGamePoints(0);
    }

    private void resetScoreTiebreakPoints() {
        firstPlayerScore.setTiebreakPoints(0);
        secondPlayerScore.setTiebreakPoints(0);
    }

    private void resetScoreAdvantages() {
        firstPlayerScore.setAdvantage(false);
        secondPlayerScore.setAdvantage(false);
    }
}
