package edu.vtc.kurs.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@ToString
@NoArgsConstructor
@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "settlement_id", referencedColumnName = "id")
    private Settlement settlement;
    @ManyToOne
    @JoinColumn(name = "street_id", referencedColumnName = "id")
    private Street street;
    @Column(name = "house_number")
    private int houseNumber;
    @Column(name = "flat_number")
    private int flatNumber;
}
