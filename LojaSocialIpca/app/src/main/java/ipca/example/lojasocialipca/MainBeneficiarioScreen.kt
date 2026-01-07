package ipca.example.lojasocialipca

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainBeneficiarioScreen(
    onFazerPedido: () -> Unit = {},
    onAcompanharPedido: () -> Unit = {},
    onLogout: () -> Unit = {},
    onHome: () -> Unit = {},
    onPerfil: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // TOP BAR: "Loja Social" à esquerda, ícone de notificação + "IPCA" à direita
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color(0xFF006837))
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Esquerda: LOJA SOCIAL
            Text(
                text = "Loja Social",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            // Direita: ícone de notificação + IPCA
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = { /* TODO: abrir notificações */ }) {
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

        // CONTEÚDO PRINCIPAL: botões grandes no centro
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onFazerPedido,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF006837)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Fazer Pedido",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onAcompanharPedido,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF006837)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Acompanhar Pedido",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }

        // BOTTOM BAR: Logout, Home, Perfil
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color(0xFF006837))
                .padding(horizontal = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Logout
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onLogout() }
            ) {
                Icon(
                    imageVector = Icons.Filled.ExitToApp,
                    contentDescription = "Logout",
                    tint = Color.White
                )
                Text(
                    text = "Logout",
                    color = Color.White,
                    fontSize = 10.sp
                )
            }

            // Home
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onHome() }
            ) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Home",
                    tint = Color.White
                )
                Text(
                    text = "Home",
                    color = Color.White,
                    fontSize = 10.sp
                )
            }

            // Perfil
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onPerfil() }
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Perfil",
                    tint = Color.White
                )
                Text(
                    text = "Perfil",
                    color = Color.White,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainBeneficiarioPreview() {
    MainBeneficiarioScreen ()
}