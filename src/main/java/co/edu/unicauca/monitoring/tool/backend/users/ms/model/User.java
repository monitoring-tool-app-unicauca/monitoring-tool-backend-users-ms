package co.edu.unicauca.monitoring.tool.backend.users.ms.model;

import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_app")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(length = 50)
    private String documentNumber;

    @Column(length = 100)
    private String firstName;

    @Column(length = 100)
    private String secondName;

    @Column(length = 100)
    private String firstLastName;

    @Column(length = 100)
    private String secondLastName;

    @Column(length = 35)
    private Long phoneNumber;

    @Column(length = 100)
    private String email;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "user_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;
}
