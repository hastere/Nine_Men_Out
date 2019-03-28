package com.example.ninemenout;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;



public class UserAdapter extends FirestoreRecyclerAdapter<Users, UserAdapter.UserHolder> {
private OnItemClickListener listener;
public String TAG = "UserAdapter";
public UserAdapter(@NonNull FirestoreRecyclerOptions<Users> options) {
        super(options);
        }

@Override
protected void onBindViewHolder(@NonNull UserHolder holder, int position, @NonNull Users model) {
        holder.textViewTitle.setText(model.getName());
        holder.textViewDescription.setText("Points " + model.getPoints());
        }

@NonNull
@Override
public UserHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_item, viewGroup, false);
        return new UserHolder(v);
        }

class UserHolder extends RecyclerView.ViewHolder {

    TextView textViewTitle, textViewDescription, textViewPriority;

    public UserHolder(@NonNull View itemView) {
        super(itemView);
        textViewTitle = itemView.findViewById(R.id.text_view_title);
        textViewDescription = itemView.findViewById(R.id.text_view_description);
        String user = textViewTitle.toString();
        Log.d(TAG, user);
        //itemView.setOnClickListener(new View.OnClickListener() {
            /*@Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(getSnapshots().getSnapshot(position), position);
                }
            }
        }); */
    };
}

public interface OnItemClickListener {
    void onItemClick(DocumentSnapshot documentSnapshot, int position);
}

    public void setOnItemClickListener(UserAdapter.OnItemClickListener listener){
        this.listener = listener;
    }
}

