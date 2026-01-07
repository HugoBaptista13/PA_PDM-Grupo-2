package ipca.example.lojasocialipca.models

import java.util.Date

data class Campanha (
    var nome : String,
    var dataInicio : Date,
    var dataFim : Date? = null,
    var descricao : String? = null,
    var tipo : String,
    var concluida : Boolean = false,
    var responsavel : String
)