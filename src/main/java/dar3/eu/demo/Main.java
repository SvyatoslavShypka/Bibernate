package dar3.eu.demo;

import dar3.eu.bibernate.Orm;
import dar3.eu.bibernate.session.SessionFactoryImpl;
import dar3.eu.bibernate.session.SessionImpl;
import dar3.eu.demo.entity.Participant;

public class Main {
    public static void main(String[] args) {

        String password = System.getenv("DB_PASSWORD");
        String username = System.getenv("DB_USER");
        String jdbcUrl = "jdbc:postgresql://localhost:5433/bibernate";
//        var sessionFactory = new SessionFactoryImpl(jdbcUrl, username, password);
/*
        var session = sessionFactory.openSession();
        var participant = session.findById(Participant.class, 4);
        session.close();
        System.out.println(participant);
*/
        var orm = new Orm(jdbcUrl, username, password);
        var participant = orm.findById(Participant.class, 4);
        System.out.println(participant);
    }
}