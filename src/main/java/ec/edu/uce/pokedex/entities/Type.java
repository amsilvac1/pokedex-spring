package ec.edu.uce.pokedex.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Type {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column private String name; }
