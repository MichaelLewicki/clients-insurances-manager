package cl.lewickidev.dto;

import cl.lewickidev.model.Cliente;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class ClientWithBalance {

    private final Cliente client;
    private final Double totalBalance;

}
