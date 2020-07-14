package com.codewarriors.hackathone.relaypension;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codewarriors.hackathone.relaypension.customvariablesforparsing.ConsituencyCustomVAR;
import com.codewarriors.hackathone.relaypension.reapplypack.AboutActivity;
import com.codewarriors.hackathone.relaypension.reapplypack.Helpactivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class StatusActivity extends AppCompatActivity {
    TextView name,aadharno,consituency,applictionstatus;
    ImageView pic;

   // Button reapplybt;

    DatabaseReference mDB;
    String aadharnost;//="499240755287";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        name=findViewById(R.id.statusnametv);
        aadharno=findViewById(R.id.aadharnostatustv);
        consituency=findViewById(R.id.consituencystatustv);
        applictionstatus=findViewById(R.id.applicationstatustv);
        pic=findViewById(R.id.statuspic);
      //  reapplybt=findViewById(R.id.reapplystatebt);
     //   reapplybt.setVisibility(View.INVISIBLE);

        SharedPreferences prefs = getSharedPreferences("codewarriors", MODE_PRIVATE);
        String restoredText = prefs.getString("userid", null);

        if(restoredText!=null)
        {
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("UserState/");
            rootRef.child(restoredText).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        aadharnost= dataSnapshot.getValue().toString();
                        mDB= FirebaseDatabase.getInstance().getReference("userstatecons/"+aadharnost);

                        StorageReference storageRef =
                                FirebaseStorage.getInstance().getReference();
                        storageRef.child(aadharnost+"/userpic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(StatusActivity.this).load(uri).into(pic);

                            }
                        });

                        mDB.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ConsituencyCustomVAR consituencyCustomVAR=dataSnapshot.getValue(ConsituencyCustomVAR.class);
                                name.setText(consituencyCustomVAR.getUsername());
                                aadharno.setText(consituencyCustomVAR.getAadharno());
                                consituency.setText(consituencyCustomVAR.getConsituency());
                                setstatus(consituencyCustomVAR.getApplicationstate());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        mDB.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                if(dataSnapshot.exists())
                                {
                                    String consituencyCustomVAR=dataSnapshot.getValue().toString();
                                    setstatus(consituencyCustomVAR);

                                }
                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                    else
                    {
                        Log.d("error in STATUSACTIVITY","datasnapshot empty");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Error in Fetching Data",Toast.LENGTH_LONG).show();
          //  Toasty.error(getApplicationContext(),"Error in Fetching Data",Toast.LENGTH_LONG,true).show();
        }


      /*  reapplybt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //re apply process will start from here

            }
        });*/


    }

    public void setstatus(String consitu)
    {
        switch (consitu)
        {
            case "0":
            {
               // reapplybt.setVisibility(View.VISIBLE);
                applictionstatus.setText("Rejected");
                break;
            }
            case "1":
            {
              //  reapplybt.setVisibility(View.INVISIBLE);
                applictionstatus.setText("IN QUEUE");
                break;
            }
            case "2":
            {
              //  reapplybt.setVisibility(View.INVISIBLE);
                applictionstatus.setText("IN READY");
                break;
            }
            case "4":
            {
              //  reapplybt.setVisibility(View.INVISIBLE);
                applictionstatus.setText("ACCEPTED");
                break;
            }
            default:
            {
               // reapplybt.setVisibility(View.INVISIBLE);
                applictionstatus.setText("UNKNOWN");

            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.sign_out: {
                SharedPreferences.Editor editor = getSharedPreferences("codewarriors", MODE_PRIVATE).edit();
                editor.putString("userid", null);
                editor.apply();
                Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                if (i != null) {
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                finish();
                startActivity(i);
                return true;
            }

            case R.id.about_actmenu:
            {

                startActivity(new Intent(StatusActivity.this, AboutActivity.class));

                return true;
            }

            case R.id.help_actmenu:
            {
                startActivity(new Intent(StatusActivity.this, Helpactivity.class));
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
