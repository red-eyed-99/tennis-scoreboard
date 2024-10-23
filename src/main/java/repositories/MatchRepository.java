package repositories;

import models.entities.Match;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;

import java.util.List;

@RequiredArgsConstructor
public class MatchRepository implements Repository<Match> {

    private final Session session;

    public void persist(Match match) {
        session.persist(match);
    }

    public List<Match> findAll() {
        return session.createQuery("select m from Match m", Match.class)
                .getResultList();
    }
}
