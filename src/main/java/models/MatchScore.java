package models;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MatchScore {
    private int firstPlayerPoints;
    private int secondPlayerPoints;

    private int firstPlayerGames;
    private int secondPlayerGames;

    private int firstPlayerSets;
    private int secondPlayerSets;
}
