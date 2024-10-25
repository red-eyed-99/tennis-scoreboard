import models.CalculationMethod;
import models.MatchScore;
import models.PlayerScore;
import models.entities.Match;
import models.entities.Player;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import services.MatchScoreCalculationService;

import static org.junit.jupiter.api.Assertions.*;

class MatchScoreCalculationServiceTest {

    private static final String RESET_POINTS_MESSAGE = "Players points must be resetted after winning a game";
    private static final String INCREASE_GAME_POINTS_MESSAGE = "Player score game points must increase by one since the game is finished";
    private static final String NOT_CHANGE_GAME_POINTS_MESSAGE = "Player score game points must not change since the game is not finished";
    private static final String HAVE_ADVANTAGE_MESSAGE = "Player must have an advantage after winning a point";

    private final MatchScore matchScore;

    private final PlayerScore wonPointPlayerScore;
    private final PlayerScore anotherPlayerScore;

    MatchScoreCalculationServiceTest() {
        var firstPlayer = new Player("First Player");
        var secondPlayer = new Player("Second Player");

        var match = new Match(firstPlayer, secondPlayer);

        matchScore = match.getMatchScore();

        wonPointPlayerScore = matchScore.getFirstPlayerScore();
        anotherPlayerScore = matchScore.getSecondPlayerScore();
    }

    @Nested
    @DisplayName("Update match score when game score is ALL")
    class AllGameScoreCalculationTest {

        @Test
        @DisplayName("Player points increased when less than 40 points")
        void updateScoreOnAll_playersHaveLessThan40Points_scorePointsIncreased() {
            setAllMatchScore(30, 15);
            var matchScoreCalculationService = new MatchScoreCalculationService(matchScore, wonPointPlayerScore);

            matchScoreCalculationService.updateMatchScore();

            var expectedWonPointPlayerPoints = 40;

            assertEquals(expectedWonPointPlayerPoints, wonPointPlayerScore.getPoints(), "Player score points must be increased");
        }

        @Test
        @DisplayName("Player who won the point have 40 points and another 15")
        void updateScoreOnAll_scoreIs40And15_scoreUpdated() {
            setAllMatchScore(40, 15);
            var matchScoreCalculationService = new MatchScoreCalculationService(matchScore, wonPointPlayerScore);
            var wonPointPlayerGamePoints = wonPointPlayerScore.getGamePoints();

            matchScoreCalculationService.updateMatchScore();

            assertEquals(wonPointPlayerGamePoints + 1, wonPointPlayerScore.getGamePoints(), INCREASE_GAME_POINTS_MESSAGE);
            assertEquals(0, wonPointPlayerScore.getPoints(), RESET_POINTS_MESSAGE);
            assertEquals(0, anotherPlayerScore.getPoints(), RESET_POINTS_MESSAGE);
        }
    }

    @Test
    @DisplayName("Update match score when game score is DEUCE")
    void updateScoreOnDeuce_scoreUpdated() {
        setDeuceMatchScore();
        var matchScoreCalculationService = new MatchScoreCalculationService(matchScore, wonPointPlayerScore);
        var wonPointPlayerGamePoints = wonPointPlayerScore.getGamePoints();

        matchScoreCalculationService.updateMatchScore();

        assertEquals(wonPointPlayerGamePoints, wonPointPlayerScore.getGamePoints(), NOT_CHANGE_GAME_POINTS_MESSAGE);

        assertSame(CalculationMethod.ADVANTAGE, matchScore.getCalculationMethod(),
                "Calculating method must be changed to ADVANTAGE after winning a point");

        assertTrue(wonPointPlayerScore.hasAdvantage(), HAVE_ADVANTAGE_MESSAGE);
    }

    @Nested
    @DisplayName("Update match score when game score is ADVANTAGE")
    class AdvantageGameScoreCalculationTest {

        @Test
        @DisplayName("Player with advantage wins a point")
        void updateScoreOnAdvantage_wonPointPlayerHasAdvantage_scoreUpdated() {
            setAdvantageMatchScore(true);
            var matchScoreCalculationService = new MatchScoreCalculationService(matchScore, wonPointPlayerScore);
            var wonPointPlayerGamePoints = wonPointPlayerScore.getGamePoints();

            matchScoreCalculationService.updateMatchScore();

            assertEquals(wonPointPlayerGamePoints + 1, wonPointPlayerScore.getGamePoints(), INCREASE_GAME_POINTS_MESSAGE);
            assertEquals(0, wonPointPlayerScore.getPoints(), RESET_POINTS_MESSAGE);
            assertEquals(0, anotherPlayerScore.getPoints(), RESET_POINTS_MESSAGE);

            assertFalse(wonPointPlayerScore.hasAdvantage(), "Player must not has an advantage after winning a game");

            assertSame(CalculationMethod.ALL, matchScore.getCalculationMethod(),
                    "Calculating method must be changed to ALL after winning a game");
        }

        @Test
        @DisplayName("Player without advantage wins a point")
        void updateScoreOnAdvantage_wonPointPlayerHasNoAdvantage_scoreUpdated() {
            setAdvantageMatchScore(false);
            var matchScoreCalculationService = new MatchScoreCalculationService(matchScore, wonPointPlayerScore);

            matchScoreCalculationService.updateMatchScore();

            assertFalse(anotherPlayerScore.hasAdvantage(), "Player must not have an advantage when score is DEUCE");

            assertSame(CalculationMethod.DEUCE, matchScore.getCalculationMethod(),
                    "Calculating method must be changed to DEUCE after player lost advantage");
        }
    }

    @Test
    @DisplayName("Player points increased by 15 when current points less than 30")
    void increaseScorePoints_playerPointsLessThan30_pointsIncreasedBy15() {
        setAllMatchScore(15, 0);
        var matchScoreCalculationService = new MatchScoreCalculationService(matchScore, wonPointPlayerScore);
        var wonPointPlayerPoints = wonPointPlayerScore.getPoints();

        matchScoreCalculationService.updateMatchScore();

        assertEquals(wonPointPlayerPoints + 15, wonPointPlayerScore.getPoints(),
                "Player score points must be increased by 15 if current less than 30");
    }

    @Test
    @DisplayName("Player points increased by 10 when current points equals 30")
    void increaseScorePoints_playerPointsEquals30_pointsIncreasedBy10() {
        setAllMatchScore(30, 0);
        var matchScoreCalculationService = new MatchScoreCalculationService(matchScore, wonPointPlayerScore);
        var wonPointPlayerPoints = wonPointPlayerScore.getPoints();

        matchScoreCalculationService.updateMatchScore();

        assertEquals(wonPointPlayerPoints + 10, wonPointPlayerScore.getPoints(),
                "Player score points must be increased by 10 if current equals 30");
    }

    void setAllMatchScore(int wonPointPlayerPoints, int anotherPlayerPoints) {
        wonPointPlayerScore.setPoints(wonPointPlayerPoints);
        anotherPlayerScore.setPoints(anotherPlayerPoints);
    }

    void setDeuceMatchScore() {
        wonPointPlayerScore.setPoints(40);
        anotherPlayerScore.setPoints(40);

        matchScore.setCalculationMethod(CalculationMethod.DEUCE);
    }

    void setAdvantageMatchScore(boolean wonPointPlayerHasAdvantage) {
        wonPointPlayerScore.setPoints(40);
        anotherPlayerScore.setPoints(40);

        if (wonPointPlayerHasAdvantage) {
            wonPointPlayerScore.setAdvantage(true);
        } else {
            anotherPlayerScore.setAdvantage(true);
        }

        matchScore.setCalculationMethod(CalculationMethod.ADVANTAGE);
    }

    void setTaibreakMatchScore(int wonPointPlayerTiebreakPoints, int anotherPlayerTiebreakPoints) {
        wonPointPlayerScore.setTiebreakPoints(wonPointPlayerTiebreakPoints);
        anotherPlayerScore.setTiebreakPoints(anotherPlayerTiebreakPoints);

        matchScore.setCalculationMethod(CalculationMethod.TIEBREAK);
    }
}
