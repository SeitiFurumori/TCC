package com.example.msa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CadastrarUsuario extends AppCompatActivity {

    private EditText editNomeCadastro;
    private EditText editSenhaCadastro;
    private EditText editUsuarioCadastro;
    private EditText editEmailCadastro;
    private Button btnCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);

        editNomeCadastro = findViewById(R.id.editNomeCadastro);
        editEmailCadastro = findViewById(R.id.editEmailCadastro);
        editSenhaCadastro = findViewById(R.id.editSenhaCadastro);
        btnCadastrar = findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }

            
        });
        


    }

    private void createUser() {
            String nome = editNomeCadastro.getText().toString();
            String email = editEmailCadastro.getText().toString();
            String senha = editSenhaCadastro.getText().toString();

            if(nome == null|| nome.isEmpty() || email == null || email.isEmpty() || senha == null || senha.isEmpty()) {
                Toast.makeText(this,"Nome, senha e email devem ser preenchidos",Toast.LENGTH_SHORT).show();
                return;
            }
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveUserInFirebase();

                            Log.i("Teste", task.getResult().getUser().getUid());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("Teste",e.getMessage());
                        Toast.makeText(CadastrarUsuario.this, "Erro"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserInFirebase() {
        String uid = FirebaseAuth.getInstance().getUid();
        String nome = editNomeCadastro.getText().toString();

        Usuario usuario = new Usuario(uid,nome);

        FirebaseFirestore.getInstance().collection("usuarios")
                .document(uid)
                .set(usuario)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Intent intent = new Intent(CadastrarUsuario.this, PaginaInicialActivity.class);

                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("Teste", e.getMessage());
                    }
                });

    }

}