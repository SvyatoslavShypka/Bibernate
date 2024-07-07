package dar3.eu.demo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Participant {
    private Integer id;
    private String firstName;
    private String lastName;
    private String city;
    private String company;
    private Integer yearsOfExperience;
    private LocalDateTime createdAt;

}
