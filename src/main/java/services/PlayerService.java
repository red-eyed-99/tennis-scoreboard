package services;

import dao.PlayerRepository;
import models.entities.Player;
import org.hibernate.Session;

import java.util.Optional;

public class PlayerService {

    private final Session session;

    private final PlayerRepository playerRepository;

    public PlayerService(Session session) {
        this.session = session;
        playerRepository = new PlayerRepository(session);
    }

    public void save(Player player) {
        session.beginTransaction();

        playerRepository.save(player);

        session.getTransaction().commit();
    }

    public Optional<Player> findByName(String name) {
        session.beginTransaction();

        var player = playerRepository.findByName(name);

        session.getTransaction().commit();

        return player;
    }
}