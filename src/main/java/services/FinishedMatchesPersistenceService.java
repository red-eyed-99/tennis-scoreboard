package services;

import models.entities.Match;
import org.hibernate.Session;
import repositories.MatchRepository;

import java.util.List;

public class FinishedMatchesPersistenceService {

    private final Session session;

    private final MatchRepository matchRepository;

    public FinishedMatchesPersistenceService(Session session) {
        this.session = session;
        matchRepository = new MatchRepository(session);
    }

    public void persist(Match match) {
        session.beginTransaction();

        matchRepository.persist(match);

        session.getTransaction().commit();
    }

    public List<Match> findAll() {
        session.beginTransaction();

        var match = matchRepository.findAll();

        session.getTransaction().commit();

        return match;
    }
}
