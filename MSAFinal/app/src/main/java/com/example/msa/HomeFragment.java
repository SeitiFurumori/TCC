package com.example.msa;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentHostCallback;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private GroupAdapter adapter;
    FirebaseFirestore conexao = FirebaseFirestore.getInstance();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView rv = view.findViewById(R.id.recycler);

        adapter = new GroupAdapter();
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));

        pegarprodutos();
        return view;
    }
    Button tncRennan;
    private void pegarprodutos() {
        FirebaseFirestore.getInstance().collection("produtos")
                .whereEqualTo("uid", FirebaseAuth.getInstance().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){

                            for (QueryDocumentSnapshot doc : task.getResult()){
                                Produto p = new Produto(doc.getString("nome_produto"),
                                        Double.parseDouble(doc.get("preco_produto").toString()),
                                        Integer.parseInt(doc.get("quantidade_produto").toString()),
                                        doc.getString("descricao_produto"),
                                        doc.getString("profileUrl"),
                                        Integer.parseInt(doc.get("comprado").toString()),
                                        doc.getString("uid"),
                                        doc.getString("puid"));


                                adapter.add(new ProdutoItem(p));
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    private class ProdutoItem extends Item<ViewHolder> {

        private final Produto produto;

        private ProdutoItem(Produto produto) {
            this.produto = produto;
        }

        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {
            TextView txtNome = viewHolder.itemView.findViewById(R.id.txtvNome);
            TextView txtPreco = viewHolder.itemView.findViewById(R.id.txtvPreco);
            TextView txtQuantidade = viewHolder.itemView.findViewById(R.id.txtvQuantidade);
            TextView txtDescricao = viewHolder.itemView.findViewById(R.id.txtvDescricao);
            TextView txtVendidos = viewHolder.itemView.findViewById(R.id.txtVendidos);
            ImageButton buttonAdd = viewHolder.itemView.findViewById(R.id.buttonAdd);
            ImageButton buttonVender = viewHolder.itemView.findViewById(R.id.buttonVender);
            ImageButton buttonDeletar = viewHolder.itemView.findViewById(R.id.buttonDeletar);
            ImageButton buttonTirar = viewHolder.itemView.findViewById(R.id.buttonTirar);
            ImageView imgP = viewHolder.itemView.findViewById(R.id.imgP);
            int adicionar = produto.getQuantidade_produto();

            txtNome.setText(produto.getNome_produto());
            txtPreco.setText("R$"+produto.getPreco_produto());
            txtQuantidade.setText("Qtd: "+produto.getQuantidade_produto());
            txtDescricao.setText(produto.getDescricao_produto());
            txtVendidos.setText("Vendidos: "+produto.getComprado());

            buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    DialogAdicionar dialogAdicionar = new DialogAdicionar();
                    Bundle args = new Bundle();
                    args.putString("PUID", produto.getPuid());
                    dialogAdicionar.setArguments(args);
                    dialogAdicionar.show(fm,"fragment_add");
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.detach(HomeFragment.this).attach(HomeFragment.this).commit();


                }
            });

            buttonVender.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    conexao.collection("produtos")
                            .document(produto.getPuid())
                            .update(
                                    "quantidade_produto", adicionar-1,
                                    "comprado",produto.getComprado()+1
                            ).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getActivity(), "Vendido", Toast.LENGTH_SHORT).show();

                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.detach(HomeFragment.this).attach(HomeFragment.this).commit();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Erro "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("TESTE",e.getMessage());
                        }
                    });
                }
            });

            buttonDeletar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    conexao.collection("produtos")
                            .document(produto.getPuid())
                            .delete();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(HomeFragment.this).attach(HomeFragment.this).commit();
                }
            });

            buttonTirar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    conexao.collection("produtos")
                            .document(produto.getPuid())
                            .update(
                                    "quantidade_produto", adicionar-1
                            ).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getActivity(), "Retirarado", Toast.LENGTH_SHORT).show();
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.detach(HomeFragment.this).attach(HomeFragment.this).commit();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Erro "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("TESTE",e.getMessage());
                        }
                    });
                }
            });

            //Picasso.get()
            //.load(produto.getProfileUrl())
            //.into(imgP);
            Picasso.get()
                    .load(produto.getProfileUrl())
                    .into(imgP);
        }
        @Override
        public int getLayout() {
            return R.layout.layout_listar;
        }
    }
}