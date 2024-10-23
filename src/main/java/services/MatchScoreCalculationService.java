package services;

import models.CalculationOption;
import models.MatchScore;
import models.PlayerScore;

public class MatchScoreCalculationService {

    private final MatchScore matchScore;

    private final CalculationOption calculationOption;

    private final PlayerScore wonPointPlayerScore;

    private final PlayerScore firstPlayerScore;
    private final PlayerScore secondPlayerScore;

    public MatchScoreCalculationService(MatchScore matchScore, int wonPointPlayerNumber) {
        this.matchScore = matchScore;

        calculationOption = matchScore.getCalculationOption();

        firstPlayerScore = matchScore.getFirstPlayerScore();
        secondPlayerScore = matchScore.getSecondPlayerScore();

        if (wonPointPlayerNumber == 1) {
            wonPointPlayerScore = firstPlayerScore;
        } else {
            wonPointPlayerScore = secondPlayerScore;
        }
    }

    public void updateMatchScore() {
        if (calculationOption == CalculationOption.TIEBREAK) {
            updateScoreOnTiebreak();
            return;
        }

        updatePointsAndGames();
        updateSets();
    }

    private void updatePointsAndGames() {
        if (calculationOption == CalculationOption.ALL) {
            updateScoreOnAll();
        } else if (calculationOption == CalculationOption.DEUCE) {
            updateScoreOnDeuce();
        } else if (calculationOption == CalculationOption.ADVANTAGE) {
            updateScoreOnAdvantage();
        }
    }

    private void updateSets() {
        var firstPlayerGamesNumber = firstPlayerScore.getPlayerGames();
        var secondPlayerGamesNumber = secondPlayerScore.getPlayerGames();

        if (firstPlayerGamesNumber == 6 || secondPlayerGamesNumber == 6) {
            if (hasSetWinner(firstPlayerGamesNumber, secondPlayerGamesNumber)) {
                increaseScoreSets();
                resetScoreGames();
                return;
            } else if (firstPlayerGamesNumber == 6 && secondPlayerGamesNumber == 6) {
                matchScore.setCalculationOption(CalculationOption.TIEBREAK);
                return;
            }
        }

        if (firstPlayerGamesNumber == 7 || secondPlayerGamesNumber == 7) {
            increaseScoreSets();
            resetScoreGames();
        }
    }

    private void updateScoreOnAll() {
        if (calculationOption == CalculationOption.ALL) {
            var currentPlayerPoints = wonPointPlayerScore.getPlayerPoints();

            if (currentPlayerPoints != 40) {
                increaseScorePoints();
            } else {
                if (hasGameWinner()) {
                    increaseScoreGames();
                    resetScorePoints();
                } else {
                    wonPointPlayerScore.setAdvantage(true);
                    matchScore.setCalculationOption(CalculationOption.ADVANTAGE);
                }
            }
        }
    }

    private void updateScoreOnDeuce() {
        wonPointPlayerScore.setAdvantage(true);
        matchScore.setCalculationOption(CalculationOption.ADVANTAGE);
    }

    public void updateScoreOnAdvantage() {
        if (wonPointPlayerScore.hasAdvantage()) {
            increaseScoreGames();
            resetScorePoints();
            resetScoreAdvantages();
            matchScore.setCalculationOption(CalculationOption.ALL);
        } else {
            resetScoreAdvantages();
            matchScore.setCalculationOption(CalculationOption.DEUCE);
        }
    }

    private void updateScoreOnTiebreak() {
        increaseScoreTiebreakPoints();

        var firstPlayerTiebreakPoints = firstPlayerScore.getTiebreakPoints();
        var secondPlayerTiebreakPoints = secondPlayerScore.getTiebreakPoints();

        if (firstPlayerTiebreakPoints >= 7 || secondPlayerTiebreakPoints >= 7) {
            if (Math.abs(firstPlayerTiebreakPoints - secondPlayerTiebreakPoints) >= 2) {
                increaseScoreSets();
                resetScoreGames();
                resetScoreTiebreakPoints();
                matchScore.setCalculationOption(CalculationOption.ALL);
            }
        }
    }

    private void increaseScorePoints() {
        var currentPlayerPoints = wonPointPlayerScore.getPlayerPoints();

        if (currentPlayerPoints < 30) {
            wonPointPlayerScore.setPlayerPoints(currentPlayerPoints + 15);
        } else {
            wonPointPlayerScore.setPlayerPoints(currentPlayerPoints + 10);
        }
    }

    private void increaseScoreGames() {
        var currentGamesNumber = wonPointPlayerScore.getPlayerGames();
        wonPointPlayerScore.setPlayerGames(currentGamesNumber + 1);
    }

    private void increaseScoreSets() {
        var currentSetsNumber = wonPointPlayerScore.getPlayerSets();
        wonPointPlayerScore.setPlayerSets(currentSetsNumber + 1);
    }

    private void increaseScoreTiebreakPoints() {
        var currentTiebreakPoints = wonPointPlayerScore.getTiebreakPoints();
        wonPointPlayerScore.setTiebreakPoints(currentTiebreakPoints + 1);
    }

    private boolean hasGameWinner() {
        var firstPlayerPoints = firstPlayerScore.getPlayerPoints();
        var secondPlayerPoints = secondPlayerScore.getPlayerPoints();

        return firstPlayerPoints != secondPlayerPoints;
    }

    private boolean hasSetWinner(int firstPlayerGamesNumber, int secondPlayerGamesNumber) {
        return firstPlayerGamesNumber == 6 && secondPlayerGamesNumber < 5
                || firstPlayerGamesNumber < 5 && secondPlayerGamesNumber == 6;
    }

    private void resetScorePoints() {
        firstPlayerScore.setPlayerPoints(0);
        secondPlayerScore.setPlayerPoints(0);
    }

    private void resetScoreGames() {
        firstPlayerScore.setPlayerGames(0);
        secondPlayerScore.setPlayerGames(0);
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
