package com.example.gamecenter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamecenter.BD.Usuario;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private List<DataItem> dataList;
    private Context context;
    private OnItemClickListener listener;
    private Usuario user;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public MainAdapter(List<DataItem> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imageView;
        private TextView recordNum;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view_title);
            imageView = itemView.findViewById(R.id.image_view);
            recordNum = itemView.findViewById(R.id.text_view_recordnum);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DataItem item = dataList.get(position);
        switch (item.getText()) {
            case "Senku":
                holder.recordNum.setText(String.valueOf(milisegundosATiempoCadena(user.getBestTimeSenku())));
                break;
            case "2048":
                holder.recordNum.setText(String.valueOf(user.getBestScore2048()));
                break;
            default:
                break;
        }
        holder.textView.setText(item.getText());
        holder.imageView.setImageResource(item.getImageResource());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public String milisegundosATiempoCadena(int milisegundos) {
        int minutos = (int) (milisegundos / 1000) / 60;
        int segundos = (int) (milisegundos / 1000) % 60;
        return String.format("%02d:%02d", minutos, segundos);
    }
}
