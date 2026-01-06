package ipca.example.lojasocialipca.models

import java.util.Date

data class Produto(
    var idProduto : String,
    var campanha : String,
    var nome : String,
    var tipo : String,
    var categoria : String,
    var validade : Date,
    var estadoProduto : String,
    var dataEntrada : Date,
    var responsavel : String
)