package com.example.ftn_android_10.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ftn_android_10.R;
import com.example.ftn_android_10.db.model.Filmovi;
import com.squareup.picasso.Picasso;

import java.util.List;


public class FilmoviAdapter extends RecyclerView.Adapter<FilmoviAdapter.MyViewHolder> {

    private Context context;
    private List<Filmovi> filmItem;
    private OnItemClickListener listener;
    private OnItemLongClickListener longClickListener;


    public FilmoviAdapter(Context context, List<Filmovi> film, OnItemClickListener listener, OnItemLongClickListener longClickListener) {
        this.context = context;
        this.filmItem = film;
        this.listener = listener;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.film_row, parent, false );

        return new MyViewHolder( view, listener, longClickListener );
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvNaziv.setText( filmItem.get( position ).getmNaziv() );
        holder.tvGodina.setText( filmItem.get( position ).getmGodina() );
        holder.tvDatum.setText( "Datum: " + filmItem.get( position ).getmDate() );
        holder.tvVreme.setText( "Vreme projekcije: " + filmItem.get( position ).getmTime() );
        holder.tvType.setText( "Jezik: " + filmItem.get( position ).getmJezik() );
        holder.tvOcena.setText( "Ocena filma: " + filmItem.get( position ).getmRating() );




        Picasso.with( context ).load( filmItem.get( position ).getmImage() ).into( holder.ivSlika );

    }

    @Override
    public int getItemCount() {
        return filmItem.size();
    }

    public Filmovi get(int position) {
        return filmItem.get( position );
    }

    public void removeAll() {
        filmItem.clear();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvNaziv;
        private TextView tvGodina;
        private TextView tvType;
        private ImageView ivSlika;
        private TextView tvVreme;
        private TextView tvOcena;
        private TextView tvDatum;

        private OnItemClickListener vhListener;
        private OnItemLongClickListener longClickListener;


        MyViewHolder(@NonNull View itemView, OnItemClickListener vhListener, final OnItemLongClickListener longClickListener) {
            super( itemView );

            tvNaziv = itemView.findViewById( R.id.tvTitle );
            tvGodina = itemView.findViewById( R.id.tvYear );
            tvType = itemView.findViewById( R.id.tvType );
            ivSlika = itemView.findViewById( R.id.ivPoster );
            tvVreme = itemView.findViewById( R.id.tvVreme );
            tvOcena = itemView.findViewById( R.id.tvOcena );
            tvDatum = itemView.findViewById( R.id.tvDatum );


            this.vhListener = vhListener;
            itemView.setOnClickListener( this );

            this.longClickListener = longClickListener;

            itemView.setOnLongClickListener( new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longClickListener.onItemLongClick( getAdapterPosition() );
                    return true;
                }
            } );
        }

        @Override
        public void onClick(View v) {
            vhListener.onItemClick( getAdapterPosition() );
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

}


