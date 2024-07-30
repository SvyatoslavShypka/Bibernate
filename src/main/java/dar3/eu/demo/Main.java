package dar3.eu.demo;

import dar3.eu.bibernate.session.SessionFactoryImpl;
import dar3.eu.bibernate.session.SessionImpl;
import dar3.eu.demo.entity.Participant;

public class Main {
    public static void main(String[] args) {
        for (int i = 1; i <= 100; i++) {
            if (i % 3 == 0 && i % 5 == 0) {
                System.out.println("FizzBuzz");
            } else if (i % 3 == 0) {
                System.out.println("Fizz");
            } else if (i % 5 == 0) {
                System.out.println("Buzz");
            } else {
                System.out.println(i);
            }
        }

        System.out.println(fizzBuzz(100));


/*
        String password = System.getenv("DB_PASSWORD");
        String username = System.getenv("DB_USER");
        var sessionFactory = new SessionFactoryImpl("jdbc:postgresql://localhost:5433/bibernate", username, password);
        var session = sessionFactory.openSession();
        var participant = session.findById(Participant.class, 4);
        System.out.println(participant);
*/

    }
    public static String fizzBuzz(int n) {
        String s = "";
        if (n == 0) {
            return s;
        }
        if ((n % 5) == 0) {
            s = "Buzz" + s;
        }
        if ((n % 3) == 0) {
            s = "Fizz" + s;
        }
        if (s.equals("")) {
            s = n + "";
        }
        return fizzBuzz(n - 1) + s + "\n";
    }

}