package com.example.msa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class GraficoActivity extends AppCompatActivity {
    FirebaseFirestore conexao = FirebaseFirestore.getInstance();
    //float itensGrafico[] = {98.6f,56.8f,35.6f,45.7f,10.5f};
    //String descricao[] = {"Item um","Item dois","Item três","Item Quatro","Item Cinco"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico);
        PieChart grafico = (PieChart) findViewById(R.id.chart);

        ArrayList<PieEntry> entradaGrafico = new ArrayList<>();

        conexao.collection("produtos")
                .whereEqualTo("uid", FirebaseAuth.getInstance().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot doc : task.getResult()){
                                if (Integer.parseInt(doc.get("comprado").toString()) != 0)
                                entradaGrafico.add(new PieEntry(Integer.parseInt(doc.get("comprado").toString()),doc.getString("nome_produto")));
                            }
                            PieDataSet dataSet = new PieDataSet(entradaGrafico,"   Quantidade de Produtos Vendidos");
                            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                            PieData pieData = new PieData(dataSet);

                            grafico.setData(pieData);

                            grafico.invalidate();
                        }
                    }
                });
        //for(int i = 0;i < itensGrafico.length; i++){
        //   entradaGrafico.add(new PieEntry(itensGrafico[i],descricao[i]));
        //}


    }
}