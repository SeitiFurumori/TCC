package com.example.msa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText edit_logar_email;
    private EditText edit_logar_senha;
    private Button btn_openCadastro;
    private Button btn_logar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_openCadastro = findViewById(R.id.btn_openCadastro);
        btn_logar = findViewById(R.id.btn_logar);
        edit_logar_senha = findViewById(R.id.edit_logar_senha);
        edit_logar_email = findViewById(R.id.edit_logar_mail);

        btn_logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edit_logar_email.getText().toString();
                String senha = edit_logar_senha.getText().toString();

                if(email == null || email.isEmpty() || senha == null || senha.isEmpty()) {
                    Toast.makeText(MainActivity.this,"Email e senha devem ser preenchidos",Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseAuth.getInstance().signInWithEmailAndPassword(email,senha)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    Log.i("Teste", task.getResult().getUser().getUid());

                                    Intent intent = new Intent(MainActivity.this, PaginaInicialActivity.class);

                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                    startActivity(intent);

                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Erro "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        btn_openCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this,CadastrarUsuario.class);
                startActivity(it);

            }
        });
    }
    public void OnClickEsqueceu(View view){
        Intent it = new Intent(MainActivity.this,EsqueceuASenhaActivity.class);
        startActivity(it);
    }
}