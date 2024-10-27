package services;

import models.entities.Match;
import org.hibernate.Session;
import repositories.MatchRepository;

import java.util.List;

public class FinishedMatchesService {

    private final Session session;

    private final MatchRepository matchRepository;

    public FinishedMatchesService(Session session) {
        this.session = session;
        matchRepository = new MatchRepository(session);
    }

    public void persist(Match match) {
        session.beginTransaction();

        matchRepository.persist(match);

        session.getTransaction().commit();
    }

    public List<Match> findByPlayerName(String name) {
        session.beginTransaction();

        var matches = matchRepository.findByPlayerName(name);

        session.getTransaction().commit();

        return matches;
    }

    public List<Match> findAll() {
        session.beginTransaction();

        var matches = matchRepository.findAll();

        session.getTransaction().commit();

        return matches;
    }
}
