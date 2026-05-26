INSERT INTO metodos_pago (nombre, requiere_api_externa, servicio_url, disponible) VALUES
                                                                                      (
                                                                                          'efectivo',
                                                                                          FALSE,
                                                                                          NULL,
                                                                                          TRUE
                                                                                      ),
                                                                                      (
                                                                                          'MerkadoPay',
                                                                                          TRUE,
                                                                                          'http://merkadopay.cl/api-solicitud-pago',
                                                                                          TRUE
                                                                                      );