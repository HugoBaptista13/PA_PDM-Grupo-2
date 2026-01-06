package ipca.example.lojasocialipca.models

import java.util.Date

data class Notificacao (
    var mensagem : String,
    var dataEnvio : Date,
    var destinatario : String
)