package ipca.example.lojasocialipca.models

import java.util.Date

data class Entrega(
    var destinatario : String,
    var responsavel : String? = null,
    var dataSubmissao : Date,
    var dataEntrega : Date? = null,
    var dataRemarcacao : Date? = null,
    var estadoEntrega : String,
    var produtos : List<String> = emptyList(),
    var tipo : String,
    var descricao : String
)