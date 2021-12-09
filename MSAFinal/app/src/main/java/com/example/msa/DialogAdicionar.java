package com.example.msa;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

public class DialogAdicionar extends DialogFragment {
    FirebaseFirestore conexao = FirebaseFirestore.getInstance();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        HomeFragment homeFragment = new HomeFragment();
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        Bundle mArgs = getArguments();
        String PUID = mArgs.getString("PUID");
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_adicionar, null))
                // Add action buttons
                .setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText etNumero = getDialog().findViewById(R.id.quantidade);
                        int numero = Integer.parseInt(etNumero.getText().toString());
                        conexao.collection("produtos")
                                .whereEqualTo("puid",PUID)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()){
                                            for (QueryDocumentSnapshot doc : task.getResult()){
                                                int quantidade = Integer.parseInt(doc.get("quantidade_produto").toString());
                                                conexao.collection("produtos")
                                                        .document(PUID)
                                                        .update(
                                                                "quantidade_produto",quantidade + numero
                                                        );
                                            }
                                        }
                                    }
                                });
                        Toast.makeText(getActivity(), "Adicionado com sucesso", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DialogAdicionar.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }


}
