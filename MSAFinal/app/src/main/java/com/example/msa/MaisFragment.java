package com.example.msa;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MaisFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MaisFragment extends Fragment {
    FirebaseFirestore conexao = FirebaseFirestore.getInstance();
    private static final int CREATEPDF = 1;
    private StringBuilder stringBuilder = new StringBuilder();
    private String texto;
    int paragrafo = 105;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MaisFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PdfFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MaisFragment newInstance(String param1, String param2) {
        MaisFragment fragment = new MaisFragment();
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
        View view = inflater.inflate(R.layout.fragment_mais, container, false);
        Button buttonGerarPDF = view.findViewById(R.id.buttonGerarPDF);
        Button buttonGrafico = view.findViewById(R.id.buttonGraficos);
        Button buttonReset = view.findViewById(R.id.buttonReset);
        buttonGerarPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/pdf");
                intent.putExtra(Intent.EXTRA_TITLE,"Produtos");
                startActivityForResult(intent,CREATEPDF);

            }
        });
        buttonGrafico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getActivity(),GraficoActivity.class);
                startActivity(it);
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                conexao.collection("produtos")
                        .whereEqualTo("uid",FirebaseAuth.getInstance().getUid())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    for (QueryDocumentSnapshot doc : task.getResult()){
                                        String puid = doc.get("puid").toString();
                                        conexao.collection("produtos")
                                                .document(puid)
                                                .update(
                                                        "comprado",0
                                                ).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getActivity(), "Resetado", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            }
                        });
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATEPDF)
        {
            if (data == null){
                Toast.makeText(getActivity(), "PDF Não gerado", Toast.LENGTH_SHORT).show();
            }else {
                if (data.getData()!=null){
                    conexao.collection("produtos")
                            .whereEqualTo("uid",FirebaseAuth.getInstance().getUid())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()){
                                        Uri caminhoDoArquivo = data.getData();
                                        PdfDocument pdfDocument = new PdfDocument();
                                        Paint paint = new Paint();
                                        TextPaint textPaint = new TextPaint();
                                        textPaint.setTextSize(26f);
                                        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1240,1754,1).create();
                                        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
                                        Canvas canvas = page.getCanvas();
                                        paint.setTextSize(40f);
                                        canvas.drawText("Produtos",100,paragrafo,paint);
                                        for (QueryDocumentSnapshot doc : task.getResult()){
                                            Produto p = new Produto(doc.getString("nome_produto"),Double.parseDouble(doc.get("preco_produto").toString()),Integer.parseInt(doc.get("quantidade_produto").toString()),doc.getString("descricao_produto"),Integer.parseInt(doc.get("comprado").toString()));
                                            stringBuilder.append(p.valores()+"\n");

                                        }
                                        texto = stringBuilder.toString();
                                        StaticLayout mTextLayout = new StaticLayout(texto, textPaint, 1140, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                                        canvas.save();

                                        int textX = 100;
                                        int textY = paragrafo;
                                        canvas.translate(textX, textY);
                                        mTextLayout.draw(canvas);
                                        canvas.restore();
                                        pdfDocument.finishPage(page);
                                        gravarPdf(caminhoDoArquivo,pdfDocument);
                                    }
                                }
                            });
                }
                else{
                    Intent voltar = new Intent(getActivity(),MainActivity.class);
                    startActivity(voltar);
                }
            }
        }


    }

    private void gravarPdf(Uri caminhoDoArquivo, PdfDocument pdfDocument) {
        try {
            BufferedOutputStream stream = new BufferedOutputStream(getActivity().getContentResolver().openOutputStream(caminhoDoArquivo));
            pdfDocument.writeTo(stream);
            pdfDocument.close();
            stream.flush();
            Toast.makeText(getActivity(), "PDF Gerado com sucesso", Toast.LENGTH_SHORT).show();
        }catch (FileNotFoundException e){
            Toast.makeText(getActivity(), "erro arquivo não encontrado", Toast.LENGTH_SHORT).show();
        }catch (IOException e){
            Toast.makeText(getActivity(), "Erro de entrada e saida", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(getActivity(), "Erro desconhecido "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}