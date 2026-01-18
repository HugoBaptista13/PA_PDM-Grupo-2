package ipca.example.lojasocialipca.models

import java.util.Date

data class Beneficiario(
    var idBeneficiario: String = "",
    var candidaturas : List<String> = emptyList(),
    var email : String = "",
    var dataAprovacao : Date? = null,
    var aceite : Boolean = false
)