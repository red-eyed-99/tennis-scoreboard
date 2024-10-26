package services;

import models.CalculationMethod;
import models.MatchScore;
import models.PlayerScore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import testutils.MatchScoreConfigurator;

import static org.junit.jupiter.api.Assertions.*;
import static testutils.PlayerScoreCopyMaker.copy;

class MatchScoreCalculationServiceTest {

    private static final String RESET_POINTS_MESSAGE = "Players points must be resetted after winning a game";
    private static final String RESET_GAME_POINTS_MESSAGE = "Players game points must be resetted after winning a set or tiebreak";
    private static final String RESET_TIEBREAK_POINTS_MESSAGE = "Players tiebreak points must be resetted after winning a tiebreak";

    private static final String INCREASE_GAME_POINTS_MESSAGE = "Player game points must be increased by one since the game is finished";
    private static final String INCREASE_SET_POINTS_MESSAGE = "Player set points must be increased by one since the set is won";

    private static final String NOT_CHANGE_GAME_POINTS_MESSAGE = "Player game points must not change since the game/set/tiebreak is not finished";
    private static final String NOT_CHANGE_SET_POINTS_MESSAGE = "Player set points must not change since the set or tiebreak is not finished";

    private final MatchScoreConfigurator matchScoreConfigurator;

    private final MatchScore matchScore = new MatchScore();

    private final PlayerScore wonPointPlayerScore = matchScore.getFirstPlayerScore();
    private final PlayerScore anotherPlayerScore = matchScore.getSecondPlayerScore();

    MatchScoreCalculationServiceTest() {
        matchScoreConfigurator = new MatchScoreConfigurator(matchScore, wonPointPlayerScore, anotherPlayerScore);
    }

    @Nested
    @DisplayName("Update match score when game score is ALL")
    class AllGameScoreCalculationTest {

        @Test
        @DisplayName("Player wins a point when both have less than 40 points")
        void updateScoreOnAll_playersHaveLessThan40Points_scorePointsIncreased() {
            matchScoreConfigurator.setAllMatchScore(30, 15);
            var matchScoreCalculationService = new MatchScoreCalculationService(matchScore, wonPointPlayerScore);

            matchScoreCalculationService.updateMatchScore();

            assertEquals(40, wonPointPlayerScore.getPoints(), "Player score points must be increased");
        }

        @Test
        @DisplayName("Player who won the point have 40 points and another 15")
        void updateScoreOnAll_scoreIs40And15_scoreUpdated() {
            matchScoreConfigurator.setAllMatchScore(40, 15);
            var matchScoreCalculationService = new MatchScoreCalculationService(matchScore, wonPointPlayerScore);
            var wonPointPlayerGamePoints = wonPointPlayerScore.getGamePoints();

            matchScoreCalculationService.updateMatchScore();

            assertAll(
                    () -> assertEquals(wonPointPlayerGamePoints + 1, wonPointPlayerScore.getGamePoints(),
                            INCREASE_GAME_POINTS_MESSAGE),

                    () -> assertEquals(0, wonPointPlayerScore.getPoints(), RESET_POINTS_MESSAGE),
                    () -> assertEquals(0, anotherPlayerScore.getPoints(), RESET_POINTS_MESSAGE)
            );
        }
    }

    @Test
    @DisplayName("Update match score when game score is DEUCE")
    void updateScoreOnDeuce_scoreUpdated() {
        matchScoreConfigurator.setDeuceMatchScore();
        var matchScoreCalculationService = new MatchScoreCalculationService(matchScore, wonPointPlayerScore);
        var wonPointPlayerGamePoints = wonPointPlayerScore.getGamePoints();

        matchScoreCalculationService.updateMatchScore();

        assertAll(
                () -> assertEquals(wonPointPlayerGamePoints, wonPointPlayerScore.getGamePoints(),
                        NOT_CHANGE_GAME_POINTS_MESSAGE),

                () -> assertSame(CalculationMethod.ADVANTAGE, matchScore.getCalculationMethod(),
                        "Calculating method must be changed to ADVANTAGE after winning a point"),

                () -> assertTrue(wonPointPlayerScore.hasAdvantage(), "Player must have an advantage after winning a point")
        );
    }

    @Nested
    @DisplayName("Update match score when one player have ADVANTAGE")
    class AdvantageGameScoreCalculationTest {

        @Test
        @DisplayName("Player with advantage wins a point")
        void updateScoreOnAdvantage_wonPointPlayerHasAdvantage_scoreUpdated() {
            matchScoreConfigurator.setAdvantageMatchScore(true);
            var matchScoreCalculationService = new MatchScoreCalculationService(matchScore, wonPointPlayerScore);
            var wonPointPlayerGamePoints = wonPointPlayerScore.getGamePoints();

            matchScoreCalculationService.updateMatchScore();

            assertAll(
                    () -> assertEquals(wonPointPlayerGamePoints + 1, wonPointPlayerScore.getGamePoints(),
                            INCREASE_GAME_POINTS_MESSAGE),

                    () -> assertEquals(0, wonPointPlayerScore.getPoints(), RESET_POINTS_MESSAGE),
                    () -> assertEquals(0, anotherPlayerScore.getPoints(), RESET_POINTS_MESSAGE),

                    () -> assertFalse(wonPointPlayerScore.hasAdvantage(), "Player must not has an advantage after winning a game"),

                    () -> assertSame(CalculationMethod.ALL, matchScore.getCalculationMethod(),
                            "Calculating method must be changed to ALL after winning a game")
            );
        }

        @Test
        @DisplayName("Player without advantage wins a point")
        void updateScoreOnAdvantage_wonPointPlayerHasNoAdvantage_scoreUpdated() {
            matchScoreConfigurator.setAdvantageMatchScore(false);
            var matchScoreCalculationService = new MatchScoreCalculationService(matchScore, wonPointPlayerScore);

            matchScoreCalculationService.updateMatchScore();

            assertAll(
                    () -> assertFalse(anotherPlayerScore.hasAdvantage(), "Player must not have an advantage when score is DEUCE"),

                    () -> assertSame(CalculationMethod.DEUCE, matchScore.getCalculationMethod(),
                            "Calculating method must be changed to DEUCE after player lost advantage")
            );
        }
    }

    @Test
    @DisplayName("Player points increased by 15 when current points less than 30")
    void increaseScorePoints_playerPointsLessThan30_pointsIncreasedBy15() {
        matchScoreConfigurator.setAllMatchScore(15, 0);
        var matchScoreCalculationService = new MatchScoreCalculationService(matchScore, wonPointPlayerScore);
        var wonPointPlayerPoints = wonPointPlayerScore.getPoints();

        matchScoreCalculationService.updateMatchScore();

        assertEquals(wonPointPlayerPoints + 15, wonPointPlayerScore.getPoints(),
                "Player score points must be increased by 15 if current less than 30");
    }

    @Test
    @DisplayName("Player points increased by 10 when current points equals 30")
    void increaseScorePoints_playerPointsEquals30_pointsIncreasedBy10() {
        matchScoreConfigurator.setAllMatchScore(30, 0);
        var matchScoreCalculationService = new MatchScoreCalculationService(matchScore, wonPointPlayerScore);
        var wonPointPlayerPoints = wonPointPlayerScore.getPoints();

        matchScoreCalculationService.updateMatchScore();

        assertEquals(wonPointPlayerPoints + 10, wonPointPlayerScore.getPoints(),
                "Player score points must be increased by 10 if current equals 30");
    }

    @Nested
    @DisplayName("Update match score set points during the game")
    class SetPointsCalculationTest {

        @Test
        @DisplayName("Both players have 6 game points")
        void updateSetPoints_playersHave6GamePoints_scoreUpdated() {
            matchScoreConfigurator.setGameScore(6, 6);
            var matchScoreCalculationService = new MatchScoreCalculationService(matchScore, wonPointPlayerScore);

            matchScoreCalculationService.updateMatchScore();

            assertSame(CalculationMethod.TIEBREAK, matchScore.getCalculationMethod(),
                    "Calculating method must be changed to TIEBREAK when players have 6 game points");
        }

        @Test
        @DisplayName("Player have 6 game points, another player have 5")
        void updateSetPoints_wonPointsPlayer6AndAnother5_scoreUpdated() {
            matchScoreConfigurator.setGameScore(6, 5);
            var matchScoreCalculationService = new MatchScoreCalculationService(matchScore, wonPointPlayerScore);
            var initWonPointPlayerScore = copy(wonPointPlayerScore);
            var initAnotherPlayerScore = copy(anotherPlayerScore);

            matchScoreCalculationService.updateMatchScore();

            assertAll(
                    () -> assertEquals(initWonPointPlayerScore.getSetPoints(), wonPointPlayerScore.getSetPoints(),
                            NOT_CHANGE_SET_POINTS_MESSAGE),

                    () -> assertEquals(initWonPointPlayerScore.getGamePoints(), wonPointPlayerScore.getGamePoints(),
                            NOT_CHANGE_GAME_POINTS_MESSAGE),

                    () -> assertEquals(initAnotherPlayerScore.getGamePoints(), anotherPlayerScore.getGamePoints(),
                            NOT_CHANGE_GAME_POINTS_MESSAGE)
            );
        }

        @Test
        @DisplayName("Player have 7 game points, another player have 5")
        void updateSetPoints_wonPointsPlayer7AndAnother5_scoreUpdated() {
            matchScoreConfigurator.setGameScore(7, 5);
            var matchScoreCalculationService = new MatchScoreCalculationService(matchScore, wonPointPlayerScore);
            var wonPointPlayerSetPoints = wonPointPlayerScore.getSetPoints();

            matchScoreCalculationService.updateMatchScore();

            assertAll(
                    () -> assertEquals(wonPointPlayerSetPoints + 1, wonPointPlayerScore.getSetPoints(),
                            INCREASE_SET_POINTS_MESSAGE),

                    () -> assertEquals(0, wonPointPlayerScore.getGamePoints(), RESET_GAME_POINTS_MESSAGE),
                    () -> assertEquals(0, anotherPlayerScore.getGamePoints(), RESET_GAME_POINTS_MESSAGE)
            );
        }

        @Test
        @DisplayName("Player have 6 game points, another player have 4")
        void updateSetPoints_wonPointsPlayer6AndAnother4_scoreUpdated() {
            matchScoreConfigurator.setGameScore(6, 4);
            var matchScoreCalculationService = new MatchScoreCalculationService(matchScore, wonPointPlayerScore);
            var wonPointPlayerSetPoints = wonPointPlayerScore.getSetPoints();

            matchScoreCalculationService.updateMatchScore();

            assertAll(
                    () -> assertEquals(wonPointPlayerSetPoints + 1, wonPointPlayerScore.getSetPoints(),
                            INCREASE_SET_POINTS_MESSAGE),

                    () -> assertEquals(0, wonPointPlayerScore.getGamePoints(), RESET_GAME_POINTS_MESSAGE),
                    () -> assertEquals(0, anotherPlayerScore.getGamePoints(), RESET_GAME_POINTS_MESSAGE)
            );
        }
    }

    @Nested
    @DisplayName("Update match score during TIEBREAK")
    class TiebreakScoreCalculationTest {

        @Test
        @DisplayName("Player with 6 tiebreak points wins a point, another player have 5")
        void updateScoreOnTiebreak_wonPointPlayer6AndAnother5_ScoreUpdated() {
            matchScoreConfigurator.setTiebreakMatchScore(6, 5);
            var matchScoreCalculationService = new MatchScoreCalculationService(matchScore, wonPointPlayerScore);
            var wonPointPlayerSetPoints = wonPointPlayerScore.getSetPoints();

            matchScoreCalculationService.updateMatchScore();

            assertAll(
                    () -> assertEquals(wonPointPlayerSetPoints + 1, wonPointPlayerScore.getSetPoints(),
                            "Player set points must be increased by one since the set is finished"),

                    () -> assertEquals(0, wonPointPlayerScore.getTiebreakPoints(), RESET_TIEBREAK_POINTS_MESSAGE),
                    () -> assertEquals(0, anotherPlayerScore.getTiebreakPoints(), RESET_TIEBREAK_POINTS_MESSAGE),

                    () -> assertEquals(0, wonPointPlayerScore.getGamePoints(), RESET_GAME_POINTS_MESSAGE),
                    () -> assertEquals(0, anotherPlayerScore.getGamePoints(), RESET_GAME_POINTS_MESSAGE),

                    () -> assertSame(CalculationMethod.ALL, matchScore.getCalculationMethod(),
                            "Calculating method must be changed to ALL after winning a tiebreak")
            );
        }

        @Test
        @DisplayName("Player with 6 tiebreak points wins a point, another player have 6")
        void updateScoreOnTiebreak_wonPointPlayer6AndAnother6_ScoreUpdated() {
            matchScoreConfigurator.setTiebreakMatchScore(6, 6);
            var matchScoreCalculationService = new MatchScoreCalculationService(matchScore, wonPointPlayerScore);
            var initWonPointPlayerScore = copy(wonPointPlayerScore);
            var initAnotherPlayerScore = copy(anotherPlayerScore);

            matchScoreCalculationService.updateMatchScore();

            assertValuesCorrect(initWonPointPlayerScore, initAnotherPlayerScore);
        }

        @Test
        @DisplayName("Player with 5 tiebreak points wins a point, another player have 4")
        void updateScoreOnTiebreak_wonPointPlayer5AndAnother4_ScoreUpdated() {
            matchScoreConfigurator.setTiebreakMatchScore(5, 4);
            var matchScoreCalculationService = new MatchScoreCalculationService(matchScore, wonPointPlayerScore);
            var initWonPointPlayerScore = copy(wonPointPlayerScore);
            var initAnotherPlayerScore = copy(anotherPlayerScore);

            matchScoreCalculationService.updateMatchScore();

            assertValuesCorrect(initWonPointPlayerScore, initAnotherPlayerScore);
        }

        private void assertValuesCorrect(PlayerScore initWonPointPlayerScore, PlayerScore initAnotherPlayerScore) {
            var initWonPointPlayerTiebreakPoints = initWonPointPlayerScore.getTiebreakPoints();

            assertAll(
                    () -> assertEquals(initWonPointPlayerTiebreakPoints + 1, wonPointPlayerScore.getTiebreakPoints(),
                            "Player tiebreak points must be increased by one since the tiebreak is not finished"),

                    () -> assertEquals(initWonPointPlayerScore.getSetPoints(), wonPointPlayerScore.getSetPoints(),
                            NOT_CHANGE_SET_POINTS_MESSAGE),

                    () -> assertEquals(initAnotherPlayerScore.getSetPoints(), anotherPlayerScore.getSetPoints(),
                            NOT_CHANGE_SET_POINTS_MESSAGE),

                    () -> assertEquals(initWonPointPlayerScore.getGamePoints(), wonPointPlayerScore.getGamePoints(),
                            NOT_CHANGE_GAME_POINTS_MESSAGE),

                    () -> assertEquals(initAnotherPlayerScore.getGamePoints(), anotherPlayerScore.getGamePoints(),
                            NOT_CHANGE_GAME_POINTS_MESSAGE),

                    () -> assertSame(CalculationMethod.TIEBREAK, matchScore.getCalculationMethod(),
                            "Calculating method must not be changed since tiebreak is going on")
            );
        }
    }
}