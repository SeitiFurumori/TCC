package com.example.msa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class EsqueceuASenhaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueceu_asenha);
        EditText editTextEmailRecuperação = findViewById(R.id.editTextEmailDeRecuperacao);
        Button buttonRecuparsenha = findViewById(R.id.buttonEnviarEmail);
        Button buttonSairEsqueceu = findViewById(R.id.buttonSairEsqueceu);
        buttonRecuparsenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmailRecuperação.getText().toString();
                if (email.equals("")){
                    Toast.makeText(EsqueceuASenhaActivity.this, "Preencha o campo email", Toast.LENGTH_SHORT).show();
                }else {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(EsqueceuASenhaActivity.this, "Email de mudança enviado!", Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        Toast.makeText(EsqueceuASenhaActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EsqueceuASenhaActivity.this, "Erro "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
        }
        });

        buttonSairEsqueceu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}