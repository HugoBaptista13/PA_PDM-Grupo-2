package ipca.example.lojasocialipca.models

import java.util.Date

data class Produto(
    var idProduto : String = "",
    var campanha : String? = null,
    var nome : String = "",
    var tipo : String = "",
    var categoria : String = "",
    var validade : Date = Date(),
    var estadoProduto : String = "",
    var dataEntrada : Date = Date(),
    var responsavel : String = ""
)