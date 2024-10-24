package models;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MatchScore {

    private final PlayerScore firstPlayerScore = new PlayerScore();

    private final PlayerScore secondPlayerScore = new PlayerScore();

    private CalculationMethod calculationMethod = CalculationMethod.ALL;
}
