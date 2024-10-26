package testutils;

import models.PlayerScore;

public class PlayerScoreCopyMaker {

    public static PlayerScore copy(PlayerScore playerScore) {
        var playerScoreCopy = new PlayerScore();

        playerScoreCopy.setPoints(playerScore.getPoints());
        playerScoreCopy.setGamePoints(playerScore.getGamePoints());
        playerScoreCopy.setSetPoints(playerScore.getSetPoints());
        playerScoreCopy.setTiebreakPoints(playerScore.getTiebreakPoints());

        playerScoreCopy.setAdvantage(playerScore.hasAdvantage());

        return playerScoreCopy;
    }
}
