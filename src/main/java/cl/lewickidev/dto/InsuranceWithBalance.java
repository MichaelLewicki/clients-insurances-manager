package cl.lewickidev.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class InsuranceWithBalance {

    private Integer insuranceId;
    private Integer balance;

}
