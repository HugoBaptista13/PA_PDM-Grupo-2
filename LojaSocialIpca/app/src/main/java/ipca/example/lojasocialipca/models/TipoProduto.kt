package ipca.example.lojasocialipca.models

data class TipoProduto (
    var tipo : String = "",
    var categorias: MutableList<String> = emptyList<String>() as MutableList<String>
)