package ipca.example.lojasocialipca.models

import java.util.Date

data class Beneficiario(
    var candidaturas : List<String>,
    var email : String,
    var dataAprovacao : Date
)