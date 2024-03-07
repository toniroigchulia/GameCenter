package com.example.gamecenter.BD;

import androidx.room.Dao;
import androidx.room.Update;

@Dao
public interface UpdateUserDAO {
    @Update
    void updateUser(Usuario usuario);
}
