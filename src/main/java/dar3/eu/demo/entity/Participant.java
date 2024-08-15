package dar3.eu.demo.entity;

import dar3.eu.bibernate.annotation.Column;
import dar3.eu.bibernate.annotation.Entity;
import dar3.eu.bibernate.annotation.Id;
import dar3.eu.bibernate.annotation.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table("participants")
public class Participant {
    @Id
    private Integer id;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName; // last_name
    private String city;
    private String company;
    private Integer yearsOfExperience;
    private LocalDateTime createdAt;

}
