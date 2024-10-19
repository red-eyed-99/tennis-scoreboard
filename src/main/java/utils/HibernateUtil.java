package utils;

import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@Getter
public class HibernateUtil {
    private static final SessionFactory sessionFactory;

    static {
        var configuration = new Configuration();
        configuration.configure();

        sessionFactory = configuration.buildSessionFactory();
    }

    public static Session getSession() {
        return sessionFactory.openSession();
    }
}
