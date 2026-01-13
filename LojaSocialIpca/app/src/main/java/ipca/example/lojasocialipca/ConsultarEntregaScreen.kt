package ipca.example.lojasocialipca.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import ipca.example.lojasocialipca.helpers.format
import ipca.example.lojasocialipca.models.Entrega
import java.util.*
import ipca.example.lojasocialipca.ui.components.TopBar

@Composable
fun ConsultarEntregasScreen(
    entregas: List<Entrega>,
    onRemarcarConfirmado: (Entrega) -> Unit = {},
    onRemarcarRejeitado: (Entrega) -> Unit = {},
    onMarcar: (Entrega) -> Unit = {},
    onBack: () -> Unit = {}
) {

    var mostrarDialog by remember { mutableStateOf(false) }
    var entregaSelecionada by remember { mutableStateOf<Entrega?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // TOP BAR
        TopBar(onBack)

        // LISTA
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            entregas.forEach { entrega ->
                EntregaCard(
                    entrega = entrega,
                    onAcaoPrincipal = {
                        if (entrega.estadoEntrega == "POR_REMARCAR") {
                            entregaSelecionada = entrega
                            mostrarDialog = true
                        } else {
                            onMarcar(entrega)
                        }
                    },
                    onCancelar = { onRemarcarRejeitado(entrega) }
                )
            }
        }
    }

    // DIALOG REMARCAR
    if (mostrarDialog && entregaSelecionada != null) {
        Dialog(onDismissRequest = { mostrarDialog = false }) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = { mostrarDialog = false }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Fechar",
                                tint = Color.Red
                            )
                        }
                    }

                    Text(
                        text = "Data Original: ${entregaSelecionada!!.dataEntrega!!.format()}",
                        fontSize = 18.sp
                    )

                    Text(
                        text = "Nova Data: ${
                            entregaSelecionada!!.dataRemarcacao?.format() ?: "--"
                        }",
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            mostrarDialog = false
                            onRemarcarConfirmado(entregaSelecionada!!)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006837)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Remarcar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            mostrarDialog = false
                            onRemarcarRejeitado(entregaSelecionada!!)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00000)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Rejeitar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun EntregaCard(
    entrega: Entrega,
    onAcaoPrincipal: () -> Unit,
    onCancelar: () -> Unit
) {
    val corFundo = if (entrega.estadoEntrega == "ENTREGUE")
        Color(0xFF00B050) else Color(0xFFE0E0E0)

    val estadoTexto = when (entrega.estadoEntrega) {
        "PENDENTE" -> "Pendente"
        "POR_ENTREGAR" -> "Por Entregar"
        "POR_REMARCAR" -> "Por Remarcar"
        "ENTREGUE" -> "Entregue"
        else -> entrega.estadoEntrega
    }

    val botaoTexto = when (entrega.estadoEntrega) {
        "PENDENTE" -> "Marcar"
        "POR_ENTREGAR" -> "Entregar"
        "POR_REMARCAR" -> "Remarcar"
        else -> null
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(corFundo)
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = entrega.dataEntrega!!.format(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Column(horizontalAlignment = Alignment.End) {
                Text(text = estadoTexto, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                entrega.dataRemarcacao?.let {
                    Text(text = it.format(), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(12.dp)
            ) {
                entrega.produtos.forEach {
                    Text(text = it, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            val botaoModifier = Modifier
                .width(108.dp)
                .height(44.dp)

            if (botaoTexto != null) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Button(
                        onClick = onAcaoPrincipal,
                        modifier = botaoModifier,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006837)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(botaoTexto)
                    }

                    Button(
                        onClick = onCancelar,
                        modifier = botaoModifier,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00000)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cancelar")
                    }
                }
            }

        }
    }
}

/* ---------- PREVIEW ---------- */

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ConsultarEntregasScreenPreview() {
    MaterialTheme {
        ConsultarEntregasScreen(
            entregas = listOf(
                Entrega(
                    destinatario = "João",
                    responsavel = "Funcionário A",
                    dataEntrega = Date(),
                    estadoEntrega = "PENDENTE",
                    produtos = listOf("Produto alimentar"),
                    dataSubmissao = Date(),
                    dataRemarcacao = Date(),
                    tipo = "",
                    descricao = ""
                ),
                Entrega(
                    destinatario = "Maria",
                    responsavel = "Funcionário B",
                    dataEntrega = Date(),
                    dataRemarcacao = Date(),
                    estadoEntrega = "POR_REMARCAR",
                    produtos = listOf("Arroz x 2", "Atum x 6"),
                    dataSubmissao = Date(),
                    tipo = "",
                    descricao = ""
                ),
                Entrega(
                    destinatario = "Ana",
                    responsavel = "Funcionário C",
                    dataEntrega = Date(),
                    estadoEntrega = "ENTREGUE",
                    produtos = listOf("Bolachas x 2"),
                    dataSubmissao = Date(),
                    dataRemarcacao = Date(),
                    tipo = "",
                    descricao = ""
                )
            )
        )
    }
}
