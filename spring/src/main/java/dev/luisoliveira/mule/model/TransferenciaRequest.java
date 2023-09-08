package dev.luisoliveira.mule.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TransferenciaRequest {
    private UUID deId;
    private UUID paraId;
    private BigDecimal valor;
}