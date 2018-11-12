package com.example.k.playapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button mLogout, mAdd, mView;

    private GoogleApiClient mGoogleClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mLogout = (Button) findViewById(R.id.main_button_logout);
        mAdd = (Button) findViewById(R.id.main_button_add);
        mView = (Button) findViewById(R.id.main_button_view);


        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent addIntent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(addIntent);

            }
        });

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewIntent = new Intent(MainActivity.this, ViewActivity.class);
                startActivity(viewIntent);
            }
        });




        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Apakah anda yakin untuk logout ?");
                builder.setCancelable(false);
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.cancel();

                    }
                });
                builder.setPositiveButton("Ya!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // Firebase sign out
                        if (Auth.GoogleSignInApi != null) {
                            // Google sign out
                            mGoogleClient = new GoogleApiClient.Builder(MainActivity.this)
                                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                                        @Override
                                        public void onConnected(@Nullable Bundle bundle) {
                                            //SIGN OUT HERE
                                            Auth.GoogleSignInApi.signOut(mGoogleClient).setResultCallback(
                                                    new ResultCallback<Status>() {
                                                        @Override
                                                        public void onResult(Status status) {
                                                            Auth.GoogleSignInApi.revokeAccess(mGoogleClient).setResultCallback(
                                                                    new ResultCallback<Status>() {
                                                                        @Override
                                                                        public void onResult(@NonNull Status status) {

                                                                            sendToStart();
                                                                            mAuth.signOut();

                                                                        }
                                                                    });
                                                        }
                                                    });
                                        }

                                        @Override
                                        public void onConnectionSuspended(int i) {/*ignored*/}
                                    })
                                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                                        @Override
                                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                                            /*ignored*/
                                        }
                                    })
                                    .addApi(Auth.GOOGLE_SIGN_IN_API) //IMPORTANT!!!
                                    .build();

                            mGoogleClient.connect();

                        }
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        final FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

            //Toast.makeText(this, "User Verified", Toast.LENGTH_SHORT).show();

            /*if (currentUser.isEmailVerified() == true){

                Toast.makeText(this, "User Verified", Toast.LENGTH_SHORT).show();

            }else{

                Intent checkIntent = new Intent(MainActivity.this, CheckActivity.class);
                checkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(checkIntent);
                finish();

            }*/


        } else {

            Intent welcomeIntent = new Intent(MainActivity.this, WelcomeActivity.class);
            welcomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(welcomeIntent);
            finish();

        }



    }
    private void sendToStart() {

        Intent redirectIntent = new Intent(MainActivity.this, WelcomeActivity.class);
        startActivity(redirectIntent);

    }

}
