package dar3.eu.demo;

import dar3.eu.bibernate.Orm;
import dar3.eu.demo.entity.Participant;

public class Main {
    public static void main(String[] args) {
        String password = System.getenv("DB_PASSWORD");
        String username = System.getenv("DB_USER");
        var orm = new Orm("jdbc:postgresql://localhost:5433/bibernate", username, password);
        var participant = orm.findById(Participant.class, 4);
        System.out.println(participant);

    }
}