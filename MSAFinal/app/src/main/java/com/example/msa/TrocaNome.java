package com.example.msa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class TrocaNome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_troca_nome);
        EditText editText = findViewById(R.id.editTextTrocaNome);
        ImageButton IbtVoltar = findViewById(R.id.imageBtnVoltar);
        Button buttonAtualizarNome = findViewById(R.id.buttonAtualizarNome);
        FirebaseFirestore conexao = FirebaseFirestore.getInstance();

        buttonAtualizarNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = editText.getText().toString();
                if(nome == null || nome.isEmpty()) {
                    Toast.makeText(TrocaNome.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                }else{
                    conexao.collection("usuarios")
                            .document(FirebaseAuth.getInstance().getUid())
                            .update(
                                    "nome", editText.getText().toString()
                            ).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(TrocaNome.this, "Atualizado", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(TrocaNome.this, "Erro "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    finish();
                }
            }
        });


        IbtVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(TrocaNome.this, PaginaInicialActivity.class);
                startActivity(it);
            }
        });


    }
}