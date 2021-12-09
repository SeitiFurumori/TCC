package com.example.msa;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilFragment extends Fragment {
    FirebaseFirestore conexao = FirebaseFirestore.getInstance();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        Button btnTrocaEmail = view.findViewById(R.id.buttonTrocaEmail);

        Button btnSair = view.findViewById(R.id.btnSair);
        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                verificaAutenticação();
            }
        });

                btnTrocaEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),EmailActivity.class);
                startActivity(intent);
            }
        });
        Button btTrocaNome = view.findViewById(R.id.buttonTrocaNome);
        TextView tvNome = view.findViewById(R.id.tvNome);
        //
        conexao.collection("usuarios")
                .whereEqualTo("uuid", FirebaseAuth.getInstance().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot doc : task.getResult()){
                                tvNome.setText(doc.getString("nome"));
                                Log.d("Teste",doc.getString("nome"));
                            }
                        }
                    }
                });
        //
        btTrocaNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getActivity(), TrocaNome.class);
                startActivity(it);
            }
        });

        Button btTrocaSenha = view.findViewById(R.id.buttonTrocaSenha);
        btTrocaSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it2 = new Intent(getActivity(), TrocaSenha.class);
                startActivity(it2);
            }
        });


        return view;
    }
    private void verificaAutenticação() {
        if (FirebaseAuth.getInstance().getUid() == null) {
            Intent intent = new Intent(getActivity(), MainActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);

        }
    }
}