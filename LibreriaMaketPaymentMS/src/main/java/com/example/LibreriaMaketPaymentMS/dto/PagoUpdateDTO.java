package com.example.LibreriaMaketPaymentMS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagoUpdateDTO {

    private Long id;
    private Boolean revertido;

}
