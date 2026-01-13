package ipca.example.lojasocialipca

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.getValue

object AppModule {

    // Firebase Authentication
    val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    // Firebase Firestore
    val firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
}