package ipca.example.lojasocialipca.models

import java.util.Date

data class Candidatura(
    var idCandidatura: String = "",
    var anoLetivo : String = "",
    var nome : String = "",
    var cartaoCidadao : String = "",
    var dataNascimento : Date = Date(),
    var telemovel : String= "",
    var email : String = "",
    var grau : String = "",
    var curso : String = "",
    var numAluno : Int = 0,
    var tipologiaPedido : List<String> = emptyList(),
    var faes : Boolean = false,
    var bolseiro : Boolean = false,
    var valorBolsa : Double? = null,
    var dataSubmissao : Date = Date(),
    var estadoCandidatura : String = ""
)