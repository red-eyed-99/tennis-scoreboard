package dao;

import models.entities.Player;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;

import java.util.Optional;

@RequiredArgsConstructor
public class PlayerRepository implements Repository<Player> {

    private final Session session;

    public void save(Player player) {
        session.persist(player);
    }

    public Optional<Player> findByName(String name) {
        return session.byNaturalId(Player.class)
                .using("name", name)
                .loadOptional();
    }
}
