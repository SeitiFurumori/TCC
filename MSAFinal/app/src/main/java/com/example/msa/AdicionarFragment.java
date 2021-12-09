package com.example.msa;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdicionarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdicionarFragment extends Fragment {
    private EditText edit_nome_produto;
    private EditText edit_preco_produto;
    private EditText edit_quantidade_produto;
    private EditText edit_descricao_produto;
    private Button btnCadastrarProduto;
    private Button btnFoto;
    private Uri selectedUri;
    private ImageView imgFoto;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public AdicionarFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdicionarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdicionarFragment newInstance(String param1, String param2) {
        AdicionarFragment fragment = new AdicionarFragment();
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
        View view = inflater.inflate(R.layout.fragment_adicionar, container, false);
        edit_nome_produto = view.findViewById(R.id.edit_nome_produto);
        edit_preco_produto = view.findViewById(R.id.edit_preco_produto);
        edit_quantidade_produto = view.findViewById(R.id.edit_quantidade_produto);
        edit_descricao_produto = view.findViewById(R.id.edit_descricao_produto);
        btnCadastrarProduto = view.findViewById(R.id.btnCadastrarProduto);
        btnFoto = view.findViewById(R.id.btnFoto);
        imgFoto = view.findViewById(R.id.imgFoto);
        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecionarFoto();
            }
        });
        btnCadastrarProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedUri == null){
                    Toast.makeText(getActivity(), "Coloque uma imagem", Toast.LENGTH_SHORT).show();
                }else{
                    criarproduto();
                }

            }
        });
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (data ==null){
                Toast.makeText(getActivity(), "Imagem não selecionada", Toast.LENGTH_SHORT).show();
            }else {
                selectedUri = data.getData();
                if (selectedUri == null){
                    Toast.makeText(getActivity(), "Imagem não selecionada", Toast.LENGTH_SHORT).show();
                    Intent it = new Intent(getActivity(),PaginaInicialActivity.class);
                    startActivity(it);
                }else {
                    selectedUri = data.getData();
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedUri);
                        imgFoto.setImageDrawable(new BitmapDrawable(bitmap));
                        btnFoto.setAlpha(0);
                    } catch (IOException e) {
                        Toast.makeText(getActivity(), "Erro "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }
    }
    private void criarproduto() {
        FirebaseFirestore conexao = FirebaseFirestore.getInstance();
        String filename = UUID.randomUUID().toString();
        final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/" + filename);

            ref.putFile(selectedUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.i("Teste", uri.toString());
                                    if(edit_nome_produto.getText().toString() == null || edit_nome_produto.getText().toString().isEmpty() ||
                                            edit_preco_produto.getText().toString() == null || edit_preco_produto.getText().toString().isEmpty() ||
                                            edit_quantidade_produto.getText().toString() == null || edit_quantidade_produto.getText().toString().isEmpty() ||
                                            edit_descricao_produto.getText().toString() == null || edit_descricao_produto.getText().toString().isEmpty()) {
                                        Toast.makeText(getActivity(), "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                                    }else{
                                        String nome = edit_nome_produto.getText().toString();
                                        double preco = Double.parseDouble(edit_preco_produto.getText().toString());
                                        int quantidade = Integer.parseInt(edit_quantidade_produto.getText().toString());
                                        String descricao = edit_descricao_produto.getText().toString();
                                        String fotoUrl = uri.toString();
                                        String uid = FirebaseAuth.getInstance().getUid();
                                        String puid = filename;

                                        Produto p = new Produto(nome, preco, quantidade, descricao, fotoUrl,0, uid,puid);
                                        conexao.collection("produtos")
                                                .document(puid)
                                                .set(p)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(getActivity().getApplication(), "Cadastrado", Toast.LENGTH_SHORT).show();
                                                        edit_nome_produto.setText("");
                                                        edit_quantidade_produto.setText("");
                                                        edit_preco_produto.setText("");
                                                        edit_descricao_produto.setText("");
                                                        imgFoto.setImageDrawable(null);
                                                        btnFoto.setAlpha(1);
                                                    }
                                        })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getActivity().getApplication(), "Erro ao cadastrar", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                    }
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Teste", e.getMessage(), e);
                        }
                    });

    }

    private void selecionarFoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/+");
        startActivityForResult(intent, 0);
    }
}