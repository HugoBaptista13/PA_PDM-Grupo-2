package ipca.example.lojasocialipca.ui.screens.beneficiario

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.Query
import ipca.example.lojasocialipca.AppModule
import ipca.example.lojasocialipca.models.Entrega
import ipca.example.lojasocialipca.ui.components.ComboBox
import ipca.example.lojasocialipca.ui.components.TopBar
import java.util.Date

@Composable
fun InserirPedidoScreen(
    onBack: () -> Unit = {},
    onInserirPedido: () -> Unit = {}
) {
    val firestore = AppModule.firestore
    val auth = AppModule.auth
    val context = LocalContext.current

    var tipo by remember { mutableStateOf("Produto Alimentar") }
    val tipos = listOf("Produto Alimentar", "Produto de Higiene Pessoal", "Produto de Limpeza")
    var descricao by remember { mutableStateOf("") }

    var aSubmeter by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // TOP BAR
        TopBar(true, onBack)

        // TÍTULO
        Text(
            text = "Realizar Pedido",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
        )

        // FORMULÁRIO
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            ComboBox(
                label = "Tipo",
                selected = tipo,
                options = tipos,
                onSelect = { tipo = it },
                modifier = Modifier.fillMaxWidth()
            )

            // Descrição do produto
            Column {
                Text("Descrição", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                OutlinedTextField(
                    value = descricao,
                    onValueChange = { descricao = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    label = { Text("Descrição do pedido") },
                    singleLine = false,
                    maxLines = 6
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // BOTÃO INSERIR PEDIDO
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    if (descricao.isBlank()) {
                        Toast.makeText(context, "Preencha a descrição do pedido", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    aSubmeter = true

                    val uid = auth.currentUser?.uid
                    if (uid == null) {
                        Toast.makeText(context, "Utilizador não autenticado", Toast.LENGTH_SHORT).show()
                        aSubmeter = false
                        return@Button
                    }

                    // Buscar beneficiário
                    firestore.collection("beneficiarios")
                        .document(uid)
                        .get()
                        .addOnSuccessListener { benSnapshot ->
                            if (!benSnapshot.exists()) {
                                Toast.makeText(context, "Beneficiário não encontrado", Toast.LENGTH_SHORT).show()
                                aSubmeter = false
                                return@addOnSuccessListener
                            }

                            val destinatarioId = benSnapshot.id

                            // Buscar último numEntrega
                            firestore.collection("entregas")
                                .orderBy("numEntrega", Query.Direction.DESCENDING)
                                .limit(1)
                                .get()
                                .addOnSuccessListener { lastSnapshot ->
                                    val lastNum =
                                        lastSnapshot.documents.firstOrNull()?.getLong("numEntrega")?.toInt() ?: 0

                                    val docRef = firestore.collection("entregas").document()

                                    val novaEntrega = Entrega(
                                        idEntrega = docRef.id, // ✅ ID real
                                        numEntrega = lastNum + 1,
                                        destinatario = destinatarioId,
                                        dataSubmissao = Date(),
                                        tipo = tipo,
                                        descricao = descricao,
                                        estadoEntrega = "PENDENTE"
                                    )

                                    docRef.set(novaEntrega)
                                        .addOnSuccessListener {
                                            Toast.makeText(context, "Pedido realizado com sucesso!", Toast.LENGTH_SHORT).show()
                                            aSubmeter = false
                                            onInserirPedido()
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(context, "Erro ao criar pedido", Toast.LENGTH_SHORT).show()
                                            aSubmeter = false
                                        }
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Erro ao buscar número de entrega", Toast.LENGTH_SHORT).show()
                                    aSubmeter = false
                                }
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Erro ao buscar beneficiário", Toast.LENGTH_SHORT).show()
                            aSubmeter = false
                        }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006837)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (aSubmeter) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Text("Realizar Pedido")
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun InserirPedidoScreenPreview() {
    InserirPedidoScreen()
}
