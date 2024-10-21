package models;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MatchScore {

    private PlayerScore firstPlayerScore = new PlayerScore();

    private PlayerScore secondPlayerScore = new PlayerScore();
}
