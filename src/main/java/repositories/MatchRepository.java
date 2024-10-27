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

    public List<Match> findByPlayerName(String playerName) {
        var hql = "select m from Match m where firstPlayer.name=:playerName or secondPlayer.name=:playerName";
        return session.createQuery(hql, Match.class)
                .setParameter("playerName", playerName)
                .list();
    }

    public List<Match> findAll() {
        return session.createQuery("select m from Match m", Match.class)
                .list();
    }
}
