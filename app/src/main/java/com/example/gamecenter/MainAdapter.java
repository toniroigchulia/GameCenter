package com.example.gamecenter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private List<DataItem> dataList;
    private Context context;
    private OnItemClickListener listener;

    // Interfaz para manejar los clics en los elementos del RecyclerView
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    // Constructor que recibe la lista de datos y el contexto
    public MainAdapter(List<DataItem> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    // Método para establecer el listener de clics
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Clase ViewHolder que representa cada elemento del RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view_title);
            imageView = itemView.findViewById(R.id.image_view);

            // Configurar el listener de clics para este elemento del RecyclerView
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

    // Método llamado cuando se crea un ViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, parent, false);
        return new ViewHolder(view);
    }

    // Método llamado para establecer el contenido de un elemento del RecyclerView
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DataItem item = dataList.get(position);
        holder.textView.setText(item.getText());
        holder.imageView.setImageResource(item.getImageResource());
    }

    // Método que devuelve la cantidad de elementos en la lista
    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
