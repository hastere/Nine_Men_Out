package com.example.ninemenout;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;



public class RequestAdapter extends FirestoreRecyclerAdapter<Requests, RequestAdapter.RequestHolder> {
    private OnItemClickListener listener;
    public String TAG = "RequestAdapter";

    public RequestAdapter(@NonNull FirestoreRecyclerOptions<Requests> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RequestHolder holder, int position, @NonNull Requests model) {
        holder.textViewTitle.setText(model.getName());
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
        void onAcceptClick(DocumentSnapshot documentSnapshot, int position);
        void onDeclineClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(RequestAdapter.OnItemClickListener listener){
        this.listener = listener;
    }


    @NonNull
    @Override
    public RequestHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.request_item, viewGroup, false);
        return new RequestHolder(v);
    }

    class RequestHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle;
        Button yes, no;

        public RequestHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.Title);
            yes = itemView.findViewById(R.id.Accept);
            no = itemView.findViewById(R.id.Decline);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });

            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onAcceptClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });

            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onDeclineClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });


        }
    }
}

