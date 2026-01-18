package ipca.example.lojasocialipca.ui.screens.funcionario

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import ipca.example.lojasocialipca.AppModule
import ipca.example.lojasocialipca.models.Funcionario
import ipca.example.lojasocialipca.ui.components.TopBar

@Composable
fun ConsultarFuncionarioScreen(
    onBack: () -> Unit = {},
    onInserir: () -> Unit = {}
) {
    val firestore = AppModule.firestore
    var funcionarios by remember { mutableStateOf<List<Funcionario>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    // Listener para carregar funcionários do Firestore
    DisposableEffect(Unit) {
        val listener = firestore.collection("funcionarios")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    loading = false
                    return@addSnapshotListener
                }

                loading = true

                funcionarios = snapshot.documents.mapNotNull { doc ->
                    Funcionario(
                        idFuncionario = doc.id,
                        nome = doc.getString("nome") ?: "",
                        email = doc.getString("email") ?: ""
                    )
                }

                loading = false
            }

        onDispose { listener.remove() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // TopBar
        TopBar(true, onBack)

        // Botão Adicionar Funcionário
        Button(
            onClick = onInserir,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006837))
        ) {
            Text("Adicionar Funcionário", color = Color.White, fontSize = 16.sp)
        }

        // Conteúdo
        if (loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF006837))
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(funcionarios) { funcionario ->
                    FuncionarioCard(funcionario = funcionario)
                }
            }
        }
    }
}

@Composable
fun FuncionarioCard(
    funcionario: Funcionario
) {
    var mostrarDialog by remember { mutableStateOf(false) }
    var aAtualizar by remember { mutableStateOf(false) }
    val firestore = AppModule.firestore
    val context = LocalContext.current
    val letraInicial = funcionario.nome.firstOrNull()?.uppercaseChar()?.toString() ?: "?"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // CABEÇALHO
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF006837)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = letraInicial,
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(funcionario.nome, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text("Email: ${funcionario.email}", fontSize = 14.sp)
                }
            }

            Spacer(Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(Modifier.height(12.dp))

            // BOTÃO REMOVER
            Button(
                onClick = { mostrarDialog = true },
                modifier = Modifier.fillMaxWidth(),
                enabled = !aAtualizar,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00000)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Remover", color = Color.White)
            }
        }
    }

    // DIÁLOGO
    if (mostrarDialog) {
        Dialog(onDismissRequest = { if (!aAtualizar) mostrarDialog = false }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Remover Funcionário?",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "O funcionário \"${funcionario.nome}\" será removido.",
                        fontSize = 14.sp
                    )

                    Button(
                        enabled = !aAtualizar,
                        onClick = {
                            aAtualizar = true
                            firestore.collection("funcionarios")
                                .document(funcionario.idFuncionario)
                                .delete()
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        context,
                                        "Funcionário removido com sucesso",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    mostrarDialog = false
                                    aAtualizar = false
                                }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        context,
                                        "Erro ao remover funcionário",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    aAtualizar = false
                                }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00000)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (aAtualizar) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Confirmar remoção", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun FuncionarioCardPreview() {
    FuncionarioCard(
        funcionario = Funcionario()
    )
}