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

public class EmailActivity extends AppCompatActivity {
    AuthCredential credential;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        Button btAtualizarEmail = findViewById(R.id.buttonAtualizarEmail);
        EditText etEmail = findViewById(R.id.editTextEmail);
        EditText etSenha= findViewById(R.id.editTextSenha);
        EditText etNovoEmail = findViewById(R.id.editTextNewEmail);
        ImageButton imageButtonVoltar = findViewById(R.id.imageButtonVoltar);

        btAtualizarEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String senha = etSenha.getText().toString();
                String emailN = etNovoEmail.getText().toString();
                Intent it = new Intent(EmailActivity.this,PaginaInicialActivity.class);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(email == null || email.isEmpty() || senha == null || senha.isEmpty() || emailN == null || emailN.isEmpty()) {
                    Toast.makeText(EmailActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                }else {
                    credential = EmailAuthProvider.getCredential(etEmail.getText().toString(), etSenha.getText().toString()); // Current Login Credentials
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        user.updateEmail(etNovoEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(EmailActivity.this, "Email alterado", Toast.LENGTH_SHORT).show();
                                                    Log.d("teste", "Email alterado");
                                                    startActivity(it);
                                                } else {
                                                    Toast.makeText(EmailActivity.this, "Error email not updated", Toast.LENGTH_SHORT).show();
                                                    Log.d("teste", "Error email not updated");
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(EmailActivity.this, "Error auth failed", Toast.LENGTH_SHORT).show();
                                        Log.d("teste", "Error auth failed");
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EmailActivity.this, "Falha "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                }
        });
        imageButtonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itvoltar = new Intent(EmailActivity.this, PaginaInicialActivity.class);
                startActivity(itvoltar);
            }
        });
    }
}