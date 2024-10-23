package models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PlayerScore {

    private int playerPoints;

    private int playerGames;

    private int playerSets;

    private int tiebreakPoints;

    @Getter(AccessLevel.NONE)
    private boolean advantage;

    public boolean hasAdvantage() {
        return advantage;
    }
}
