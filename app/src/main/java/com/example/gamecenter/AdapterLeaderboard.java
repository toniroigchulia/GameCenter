package com.example.gamecenter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gamecenter.BD.Usuario;
import java.util.List;

public class AdapterLeaderboard extends RecyclerView.Adapter<AdapterLeaderboard.ViewHolder> {

    private List<Usuario> usuarios;

    public void setData(List<Usuario> usuarios) {
        this.usuarios = usuarios;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Usuario usuario = usuarios.get(position);
        holder.bind(usuario);
    }

    @Override
    public int getItemCount() {
        return usuarios == null ? 0 : usuarios.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewUsername;
        private TextView textViewScore2048;
        private TextView textViewScoreSenku;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUsername = itemView.findViewById(R.id.textViewUsername);
            textViewScore2048 = itemView.findViewById(R.id.textViewScore2048);
            textViewScoreSenku = itemView.findViewById(R.id.textViewScoreSenku);
        }

        public void bind(Usuario usuario) {
            textViewUsername.setText(usuario.getUser());
            textViewScore2048.setText(String.valueOf(usuario.getBestScore2048()));
            textViewScoreSenku.setText(milisegundosATiempoCadena(usuario.getBestTimeSenku()));
        }

        public String milisegundosATiempoCadena(int milisegundos) {
            int minutos = (int) (milisegundos / 1000) / 60;
            int segundos = (int) (milisegundos / 1000) % 60;
            return String.format("%02d:%02d", minutos, segundos);
        }
    }


}
