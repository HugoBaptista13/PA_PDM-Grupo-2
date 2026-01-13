package ipca.example.lojasocialipca.ui.screens.funcionario

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ipca.example.lojasocialipca.R
import ipca.example.lojasocialipca.ui.theme.LojaSocialIpcaTheme
import ipca.example.lojasocialipca.ui.components.BottomBar

@Composable
fun MainFuncionarioScreen(
    onProdutos: () -> Unit = {},
    onCampanhas: () -> Unit = {},
    onCandidaturas: () -> Unit = {},
    onEntregas: () -> Unit = {},
    onLogout: () -> Unit = {},
    onPerfil: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // TOP BAR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color(0xFF006837))
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Loja Social",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { /* TODO: notificações */ }) {
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

        // TÍTULO ALERTAS
        Text(
            text = "Alertas",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)
        )

        // LISTA DE ALERTAS
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            listOf(
                "Validade de produto termina em 15 dias",
                "Validade de produto termina em 5 dias",
                "Entrega marcada para amanhã"
            ).forEach { texto ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                ) {
                    Text(
                        text = "• $texto",
                        modifier = Modifier.padding(12.dp),
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // QUATRO BOTÕES NO FORMATO DO MOCKUP
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FuncionarioTile(texto = "Produtos", iconRes = R.drawable.ic_produtos, onClick = onProdutos)
                    FuncionarioTile(texto = "Campanhas", iconRes = R.drawable.ic_campanhas, onClick = onCampanhas)

                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FuncionarioTile(texto = "Candidaturas", iconRes = R.drawable.ic_candidaturas, onClick = onCandidaturas)
                    FuncionarioTile(texto = "Entregas", iconRes = R.drawable.ic_entregas, onClick = onEntregas)

                }
            }

            }
        BottomBar (
            onLogout = onLogout,
            onPerfil= onPerfil
        )
        }


}

@Composable
fun FuncionarioTile(
    texto: String,
    iconRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(width=120.dp,height=120.dp)          // retângulo baixo e largo, como no mockup
            .background(Color(0xFF006837), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ÍCONE 512x512 escalado (zona de cima)
            Image(
                painter = painterResource(iconRes),
                contentDescription = texto,
                modifier = Modifier
                    .height(64.dp)   // ajusta se quiseres maior
            )

            // Texto em baixo
            Text(
                text = texto,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
@Composable
@Preview(showBackground = true)
fun MainFuncionariopreview() {
    LojaSocialIpcaTheme {
        MainFuncionarioScreen()
    }
}


