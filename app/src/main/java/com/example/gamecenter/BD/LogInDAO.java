package com.example.gamecenter.BD;
import androidx.room.Dao;
import androidx.room.Query;
@Dao
public interface LogInDAO {
    @Query("Select * From usuarios WHERE user = :user AND password = :password")
    Usuario login(String user,String password);

    @Query("SELECT EXISTS(SELECT 1 FROM usuarios WHERE user = :user AND password = :password)")
    boolean existUser(String user,String password);
}
