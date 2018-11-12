package com.example.k.playapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {

    private TextInputLayout mTitle, mDesc;
    private Button mSave;
    private DatabaseReference mEventDatabase;
    private ProgressDialog mProgressDialog;
    private String textUid = FirebaseDatabase.getInstance().getReference().child("Event").push().getKey();

    private Map update_hashMap = new HashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mTitle = (TextInputLayout) findViewById(R.id.add_text_title);
        mDesc = (TextInputLayout) findViewById(R.id.add_text_desc);
        mSave = (Button) findViewById(R.id.add_button_save);

        mEventDatabase = FirebaseDatabase.getInstance().getReference().child("Event").child(textUid);


        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTitle.getEditText().getText().toString().isEmpty() || mDesc.getEditText().getText().toString().isEmpty()){

                    Toast.makeText(AddActivity.this, "Please fill the form correctly.", Toast.LENGTH_SHORT).show();

                }else {

                    mProgressDialog = new ProgressDialog(AddActivity.this);
                    mProgressDialog.setTitle("Saving Data");
                    mProgressDialog.setMessage("Please wait...");
                    mProgressDialog.setCanceledOnTouchOutside(false);
                    mProgressDialog.show();

                    String title = mTitle.getEditText().getText().toString();
                    String desc = mDesc.getEditText().getText().toString();

                    update_hashMap.put("title", title);
                    update_hashMap.put("desc", desc);

                    mEventDatabase.updateChildren(update_hashMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                            if (task.isSuccessful()){

                                Toast.makeText(AddActivity.this, "Success Uploading", Toast.LENGTH_SHORT).show();
                                mProgressDialog.dismiss();
                                Intent backIntent = new Intent(AddActivity.this, MainActivity.class);
                                startActivity(backIntent);
                                backIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                finish();

                            }

                        }
                    });

                }

            }
        });






    }
}
