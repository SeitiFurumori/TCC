package com.example.msa;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Usuario {

    private String uuid;
    private String nome;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Usuario(String uuid, String nome) {
        this.uuid = uuid;
        this.nome = nome;
    }
}

