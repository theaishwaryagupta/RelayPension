package com.codewarriors.hackathone.relaypension.adminside.listallconspack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.codewarriors.hackathone.relaypension.R;
import com.codewarriors.hackathone.relaypension.adminside.tabactivitypack.TabactivityReadyQueue;
import com.codewarriors.hackathone.relaypension.customvariablesforparsing.ConstituencyHelperClass;
import com.codewarriors.hackathone.relaypension.reapplypack.AboutActivity;
import com.codewarriors.hackathone.relaypension.reapplypack.Helpactivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListAllConstituency extends AppCompatActivity {

    DatabaseReference rootreference= FirebaseDatabase.getInstance().getReference();
    ListView listallconstit;
    ArrayList<ListConstituencyVAR> listtodisplay;
    ListconstituencyAdapterExba listconstituencyAdapterExba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all_constituency);
        listallconstit=findViewById(R.id.listviewallconstituency);




        listtodisplay=new ArrayList<>();

        rootreference.child("consituency").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {


                    listtodisplay.clear();


                    ConstituencyHelperClass constituencyHelperClass=new ConstituencyHelperClass();

                    for(DataSnapshot dataSnapshotchild:dataSnapshot.getChildren())
                    {
                        String constituencyname=dataSnapshotchild.getKey();
                        Long nooformin_ready=dataSnapshotchild.child("ready").getChildrenCount();
                        Long nooformin_queue=dataSnapshotchild.child("queue").getChildrenCount();
                        ListConstituencyVAR temp=new ListConstituencyVAR(constituencyname,nooformin_ready,nooformin_queue,constituencyHelperClass.getlimit(constituencyname));
                        listtodisplay.add(temp);

                        Log.d("msg",constituencyname+"/"+nooformin_ready+"/"+nooformin_queue);

                    }
                    listconstituencyAdapterExba=new ListconstituencyAdapterExba(getApplicationContext(),listtodisplay);
                    listallconstit.setAdapter(listconstituencyAdapterExba);
                    listconstituencyAdapterExba.notifyDataSetChanged();



                }
                else
                {
                   // Toasty.warning(getApplicationContext(), "No Application Found", Toast.LENGTH_SHORT, true).show();
                    Toast.makeText(getApplicationContext(),"No Application Found",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

               // Toasty.error(getApplicationContext(), "Error In Fetching", Toast.LENGTH_SHORT, true).show();
                Toast.makeText(getApplicationContext(), "Error In Fetching",Toast.LENGTH_LONG).show();

            }
        });


        listallconstit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent it=new Intent(ListAllConstituency.this, TabactivityReadyQueue.class);
                it.putExtra("constituency",listtodisplay.get(i).getConstituencyname());
                startActivity(it);
            }
        });


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
                editor.putString("adminid", null);
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

                startActivity(new Intent(ListAllConstituency.this, AboutActivity.class));

                return true;
            }

            case R.id.help_actmenu:
            {
                startActivity(new Intent(ListAllConstituency.this, Helpactivity.class));
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
