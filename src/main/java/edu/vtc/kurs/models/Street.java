package edu.vtc.kurs.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

/**
 * The type Street.
 */
@Data
@ToString
@NoArgsConstructor
@Entity
@Table(name = "street")
public class Street {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "settlement_id", referencedColumnName = "id")
    @ToString.Exclude
    private Settlement settlement;
    @OneToMany(mappedBy = "street", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Address> addresses;
}
