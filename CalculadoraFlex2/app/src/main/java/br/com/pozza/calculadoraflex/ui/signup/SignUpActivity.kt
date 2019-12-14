package br.com.pozza.calculadoraflex.ui.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.*
import br.com.pozza.calculadoraflex.R
import br.com.pozza.calculadoraflex.R.*
import br.com.pozza.calculadoraflex.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_sign_up)
        mAuth = FirebaseAuth.getInstance()
        btCreate.setOnClickListener {
            mAuth.createUserWithEmailAndPassword(
                inputEmail.text.toString(),
                inputPassword.text.toString()
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    saveInRealTimeDatabase()
                } else {
                    makeText(
                        this@SignUpActivity, it.exception?.message,
                        LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun saveInRealTimeDatabase() {
        val user = User(
            inputName.text.toString(), inputEmail.text.toString(),
            inputPhone.text.toString()
        )
        FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .setValue(user)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    makeText(
                        this, "Usuário criado com sucesso",
                        LENGTH_SHORT
                    ).show()
                    val returnIntent = Intent()
                    returnIntent.putExtra("email", inputEmail.text.toString())
                    setResult(RESULT_OK, returnIntent)
                    finish()
                } else {
                    makeText(this, "Erro ao criar o usuário", LENGTH_SHORT).show()
                }
            }
    }
}