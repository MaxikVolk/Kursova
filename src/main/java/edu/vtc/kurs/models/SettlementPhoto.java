package edu.vtc.kurs.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;


/**
 * The type Settlement photo.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "settlement_photos")
public class SettlementPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @ManyToOne
    @JoinColumn(name = "settlement_id", referencedColumnName = "id")
    private Settlement settlement;
    @Column(name = "photo_url")
    private String photoUrl;
}
