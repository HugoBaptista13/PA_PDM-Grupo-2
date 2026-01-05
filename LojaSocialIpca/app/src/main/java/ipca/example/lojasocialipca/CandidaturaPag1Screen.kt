package ipca.example.lojasocialipca

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CandidaturaPag1Screen(
    onBack: () -> Unit = {},
    onSeguinte: () -> Unit = {}
) {
    // Ano letivo (dropdown)
    val anosLetivos = listOf("2025/2026")
    var anoLetivo by remember { mutableStateOf(anosLetivos.first()) }
    var expandedAno by remember { mutableStateOf(false) }

    // Campos texto
    var nome by remember { mutableStateOf("") }
    var cc by remember { mutableStateOf("") }
    var telemovel by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    // Data de nascimento em 3 caixas
    var dia by remember { mutableStateOf("") }
    var mes by remember { mutableStateOf("") }
    var ano by remember { mutableStateOf("") }

    // ✅ ESTADO DE ERRO
    var erroValidacao by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // TOP BAR (igual)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color(0xFF006837))
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = Color.White
                    )
                }
                Text(
                    text = "Loja Social",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = "Notificações",
                        tint = Color.White
                    )
                }
                Text(
                    text = "IPCA",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // TÍTULO
        Text(
            text = "Identificação do candidato",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
        )

        // FORMULÁRIO
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Ano letivo (igual)
            Column {
                Text("Ano letivo", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                OutlinedTextField(
                    value = anoLetivo,
                    onValueChange = { },
                    modifier = Modifier.fillMaxWidth().clickable { expandedAno = true },
                    readOnly = true,
                    label = { Text("Escolhe o ano letivo") }
                )
                DropdownMenu(
                    expanded = expandedAno,
                    onDismissRequest = { expandedAno = false }
                ) {
                    anosLetivos.forEach { a ->
                        DropdownMenuItem(
                            text = { Text(a) },
                            onClick = {
                                anoLetivo = a
                                expandedAno = false
                            }
                        )
                    }
                }
            }

            // ✅ NOME COM VALIDAÇÃO
            Column {
                Text("Nome", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                OutlinedTextField(
                    value = nome,
                    onValueChange = {
                        nome = it
                        erroValidacao = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            if (erroValidacao && nome.isEmpty()) "Preenche o nome"
                            else "Insere o teu nome completo"
                        )
                    },
                    isError = erroValidacao && nome.isEmpty()
                )
            }

            // ✅ CC COM VALIDAÇÃO
            Column {
                Text("Cartão de Cidadão (CC)", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                OutlinedTextField(
                    value = cc,
                    onValueChange = {
                        cc = it
                        erroValidacao = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            if (erroValidacao && cc.isEmpty()) "Preenche o CC"
                            else "Número de CC"
                        )
                    },
                    isError = erroValidacao && cc.isEmpty()
                )
            }

            // Data de nascimento (igual)
            Text("Data de nascimento", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = dia,
                    onValueChange = { dia = it },
                    modifier = Modifier.weight(1f),
                    label = { Text("Dia") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = mes,
                    onValueChange = { mes = it },
                    modifier = Modifier.weight(1f),
                    label = { Text("Mês") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = ano,
                    onValueChange = { ano = it },
                    modifier = Modifier.weight(1f),
                    label = { Text("Ano") },
                    singleLine = true
                )
            }

            // ✅ TELEMÓVEL COM VALIDAÇÃO
            Column {
                Text("Telemóvel", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                OutlinedTextField(
                    value = telemovel,
                    onValueChange = {
                        telemovel = it
                        erroValidacao = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            if (erroValidacao && telemovel.isEmpty()) "Preenche o telemóvel"
                            else "Contacto"
                        )
                    },
                    isError = erroValidacao && telemovel.isEmpty()
                )
            }

            // ✅ EMAIL COM VALIDAÇÃO
            Column {
                Text("Email", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        erroValidacao = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            if (erroValidacao && email.isEmpty()) "Preenche o email"
                            else "Endereço de email"
                        )
                    },
                    isError = erroValidacao && email.isEmpty()
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // BARRA DE PROGRESSO (1/5)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .background(Color(0xFF006837), RoundedCornerShape(3.dp))
            )
            Box(
                modifier = Modifier
                    .weight(4f)
                    .height(6.dp)
                    .background(Color(0xFFE0E0E0), RoundedCornerShape(3.dp))
            )
        }

        // ✅ BOTÃO COM VALIDAÇÃO CORRIGIDA
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {  // ✅ CORRIGIDO: onClick = { ... }
                    if (nome.isNotEmpty() || cc.isNotEmpty() || telemovel.isNotEmpty() ||
                        email.isNotEmpty() || dia.isNotEmpty() || mes.isNotEmpty() || ano.isNotEmpty()) {
                        erroValidacao = false
                        onSeguinte()
                    } else {
                        erroValidacao = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006837)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Seguinte")
            }
        }
    }
}




