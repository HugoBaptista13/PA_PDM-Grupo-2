package ipca.example.lojasocialipca.models

import java.util.Date

data class Notificacao (
    var id : String = "",
    var mensagem : String = "",
    var dataEnvio : Date = Date(),
    var destinatario : String = "",
    var resolvida : Boolean = false
)