package models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PlayerScore {

    private int points;

    private int gamePoints;

    private int setPoints;

    private int tiebreakPoints;

    @Getter(AccessLevel.NONE)
    private boolean advantage;

    public boolean hasAdvantage() {
        return advantage;
    }
}
