package dev.luisoliveira.mule.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Table(name = "TB_CONTA")
public class ContaModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private UUID contaId;
    @Column(nullable = false, unique = true)
    private String nome;
    private BigDecimal saldo;
}
