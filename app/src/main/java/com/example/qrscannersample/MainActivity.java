package com.example.qrscannersample;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.qrscannersample.Adapters.Adapter;
import com.example.qrscannersample.Database.DbHelper;
import com.example.qrscannersample.Model.ListItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{

    RecyclerView recyclerView;
    ArrayList<ListItem> arrayList;
    Adapter myAdapter;

    DbHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=(RecyclerView)findViewById(R.id.recyleView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        helper = new DbHelper(this);


        //get data from the db

        arrayList = helper.getAllInformation();

        if(arrayList.size()>0)
        {
            myAdapter = new Adapter(arrayList,this);
            recyclerView.setAdapter(myAdapter);


        }
        else
            {
            Toast.makeText(getApplicationContext(),"No data find", Toast.LENGTH_LONG).show();
        }

        //swipe to remove data
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public int getMovementFlags(RecyclerView recyclerView,RecyclerView.ViewHolder viewHolder) {
                return 0;
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                ListItem listItem = arrayList.get(position);

                //remove data from database

                helper.deleteRow(listItem.getId());

                arrayList.remove(position);
                myAdapter.notifyItemRemoved(position);
                myAdapter.notifyItemRangeChanged(position,arrayList.size());

            }
        }).attachToRecyclerView(recyclerView);

        final IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setBeepEnabled(true);

        // (0) is for the rear camera, (1) is for the front camera
        intentIntegrator.setCameraId(0);

        FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(R.id.fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentIntegrator.initiateScan();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        if(result!=null)
        {
            if(result.getContents() == null)
            {
                Toast.makeText(getApplicationContext(), "No result found", Toast.LENGTH_LONG).show();
            }
            else
            {
                boolean isInserted = helper.insertData(result.getFormatName(), result.getContents());

                if(isInserted)
                {
                    arrayList.clear();
                    arrayList = helper.getAllInformation();
                    myAdapter = new Adapter(arrayList,this);
                    recyclerView.setAdapter(myAdapter);
                    myAdapter.notifyDataSetChanged();
                }
            }
        }
        else
        {
            super.onActivityResult(requestCode,resultCode,data);
        }
    }
}
