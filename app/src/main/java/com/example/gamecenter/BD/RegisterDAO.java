package com.example.gamecenter.BD;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
@Dao
public interface RegisterDAO {

    @Query("SELECT EXISTS(SELECT 1 FROM usuarios WHERE user = :user)")
    boolean alreadyExist(String user);

    @Insert
    void insert(Usuario usuario);

    @Query("SELECT * FROM usuarios")
    List<Usuario> getAllUsers();
}
