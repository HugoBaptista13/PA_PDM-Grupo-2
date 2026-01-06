package ipca.example.lojasocialipca.models

import java.util.Date

data class Candidatura(
    var anoLetivo : String,
    var nome : String,
    var cartaoCidadao : String,
    var dataNascimento : Date,
    var telemovel : String,
    var email : String,
    var grau : String,
    var curso : String,
    var numAluno : Int,
    var tipologiaPedido : List<String>,
    var faes : Boolean,
    var bolseiro : Boolean,
    var valorBolsa : Double? = null,
    var dataSubmissao : Date,
    var estadoCandidatura : String
)