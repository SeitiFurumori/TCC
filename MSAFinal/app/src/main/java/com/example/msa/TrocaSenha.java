package com.example.msa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TrocaSenha extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_troca_senha);
        EditText etEmail = findViewById(R.id.EmailAntigo);
        EditText password = findViewById(R.id.editTextSenhaDeTroca);
        EditText newPass = findViewById(R.id.NovaSenha);
        Button atualizar = findViewById(R.id.btAtualizarSenha);
        ImageButton IbtVoltar = findViewById(R.id.IbtVoltar);

        atualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(TrocaSenha.this,PaginaInicialActivity.class);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String email = etEmail.getText().toString();
                String senha = password.getText().toString();
                String senhaN = newPass.getText().toString();
                if(email == null || email.isEmpty() || senha == null || senha.isEmpty() || senhaN == null || senhaN.isEmpty()) {
                    Toast.makeText(TrocaSenha.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                }else{
                    AuthCredential credential = EmailAuthProvider.getCredential(etEmail.getText().toString(), password.getText().toString()); // Current Login Credentials
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        user.updatePassword(newPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(TrocaSenha.this, "Senha alterada", Toast.LENGTH_SHORT).show();
                                                    Log.d("teste", "Senha alterada");
                                                    startActivity(it);
                                                } else {
                                                    Toast.makeText(TrocaSenha.this, "", Toast.LENGTH_SHORT).show();
                                                    Log.d("teste", "Error password not updated");
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(TrocaSenha.this, "Error auth failed", Toast.LENGTH_SHORT).show();
                                        Log.d("teste", "Error auth failed");
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(TrocaSenha.this, "Falha"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        IbtVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itvoltar = new Intent(TrocaSenha.this, PaginaInicialActivity.class);
                startActivity(itvoltar);
            }
        });
    }
}