package com.example.k.playapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewActivity extends AppCompatActivity {

    private RecyclerView mList;
    private DatabaseReference mEventDatabase;


    private FirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        mList = (RecyclerView) findViewById(R.id.view_list);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewActivity.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);


        mEventDatabase = FirebaseDatabase.getInstance().getReference().child("Event");


        FirebaseRecyclerOptions<Event> options =
                new FirebaseRecyclerOptions.Builder<Event>()
                        .setQuery(mEventDatabase, Event.class)
                        .build();


        adapter = new FirebaseRecyclerAdapter<Event, EventViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull EventViewHolder holder, int position, @NonNull Event model) {

                holder.setTitle(model.getTitle());
                holder.setDesc(model.getDesc());

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Toast.makeText(ViewActivity.this, "Toast!", Toast.LENGTH_SHORT).show();

                    }
                });

            }

            @NonNull
            @Override
            public EventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.view_layout, viewGroup, false);

                return new EventViewHolder(view);
            }
        };

        mList.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();


    }


    public static class EventViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setTitle(String title) {
            TextView mTitle = (TextView) mView.findViewById(R.id.event_single_name);
            mTitle.setText(title);
        }
        public void setDesc(String desc) {
            TextView mDesc = (TextView) mView.findViewById(R.id.event_litle_description);
            mDesc.setText(desc);
        }
    }
}
