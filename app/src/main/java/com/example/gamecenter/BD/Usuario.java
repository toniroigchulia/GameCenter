package com.example.gamecenter.BD;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "usuarios")
public class Usuario{
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String user = null;
    private byte[] fotoPerfil = null;
    private String password = null;
    private int bestScore2048 = 0;
    private int bestTimeSenku = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public byte[] getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(byte[] fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getBestScore2048() {
        return bestScore2048;
    }

    public void setBestScore2048(int bestScore2048) {
        this.bestScore2048 = bestScore2048;
    }

    public int getBestTimeSenku() {
        return bestTimeSenku;
    }

    public void setBestTimeSenku(int bestTimeSenku) {
        this.bestTimeSenku = bestTimeSenku;
    }
}
