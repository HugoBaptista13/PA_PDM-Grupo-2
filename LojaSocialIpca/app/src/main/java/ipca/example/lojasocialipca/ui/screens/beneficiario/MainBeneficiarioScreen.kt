package ipca.example.lojasocialipca.ui.screens.beneficiario

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ipca.example.lojasocialipca.ui.components.BottomBar
import ipca.example.lojasocialipca.ui.components.TopBar

@Composable
fun MainBeneficiarioScreen(
    onFazerPedido: () -> Unit = {},
    onAcompanharPedido: () -> Unit = {},
    onLogout: () -> Unit = {},
    onPerfil: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopBar(mostrarBack = false)

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
        BottomBar(onLogout = onLogout, onPerfil = onPerfil)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainBeneficiarioPreview() {
    MainBeneficiarioScreen ()
}