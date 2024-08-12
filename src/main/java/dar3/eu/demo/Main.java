package dar3.eu.demo;

import dar3.eu.bibernate.session.SessionFactoryImpl;
import dar3.eu.bibernate.session.SessionImpl;
import dar3.eu.demo.entity.Participant;

public class Main {
    public static void main(String[] args) {

        String password = System.getenv("DB_PASSWORD");
        String username = System.getenv("DB_USER");
        var sessionFactory = new SessionFactoryImpl("jdbc:postgresql://localhost:5433/bibernate", username, password);

        var session = sessionFactory.openSession();

        var participant = session.findById(Participant.class, 4);

        System.out.println(participant);
    }
}