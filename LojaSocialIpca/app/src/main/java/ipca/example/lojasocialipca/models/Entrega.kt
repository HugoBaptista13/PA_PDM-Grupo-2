package ipca.example.lojasocialipca.models

import java.util.Date

data class Entrega(
    var destinatario : String,
    var responsavel : String,
    var dataEntrega : Date,
    var dataRemarcacao : Date? = null,
    var estadoEntrega : String,
    var produtos : List<String>
)