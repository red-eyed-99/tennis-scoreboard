package models.entities;

import jakarta.persistence.*;
import lombok.*;
import models.MatchScore;

@Entity
@Table(name = "Matches")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "first_player_id", nullable = false)
    private Player firstPlayer;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "second_player_id", nullable = false)
    private Player secondPlayer;

    @Setter
    @ManyToOne
    @JoinColumn(name = "winner_id", nullable = false)
    private Player winner;

    @Transient
    private final MatchScore matchScore = new MatchScore();
}
