package com.example.packyourbag;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.packyourbag.Adapter.CheckListAdapter;
import com.example.packyourbag.Constants.MyConstants;
import com.example.packyourbag.Data.AppData;
import com.example.packyourbag.Database.RoomDB;
import com.example.packyourbag.Models.Items;

import java.util.ArrayList;
import java.util.List;

public class Chcklist extends AppCompatActivity {

    RecyclerView recyclerView;
    CheckListAdapter checkListAdapter;
    RoomDB database;
    List<Items> itemsList=new ArrayList<>();
    String header,show;

    EditText txtAdd;
    Button btnAdd;
    LinearLayout linearLayout;


    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_one,menu);


        if (MyConstants.MY_SELECTIONS.equals(header)){
            menu.getItem(0).setVisible(false);
            menu.getItem(2).setVisible(false);
            menu.getItem(3).setVisible(false);
        }else if (MyConstants.MY_LIST_CAMEL_CASE.equals(header))
            menu.getItem(1).setVisible(false);

        MenuItem menuItem=menu.findItem(R.id.btnSearch);
        SearchView searchView=(SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Items> mFinalList=new ArrayList<>();
                for (Items items:itemsList){
                    if (items.getItemname().toLowerCase().startsWith(newText.toLowerCase())){
                        mFinalList.add(items);
                    }
                }
                updateRecycler(mFinalList);
                return false;
            }
        });

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        Intent intent=new Intent(this, Chcklist.class);
        AppData appData=new AppData(database,this);

        int id = item.getItemId();
        if (id == R.id.btnMySelection){
            intent.putExtra(MyConstants.HEADER_SMALL,MyConstants.MY_SELECTIONS);
            intent.putExtra(MyConstants.SHOW_SMALL,MyConstants.FALSE_STRING);
            startActivityForResult(intent,101);
        }
        else if (id == R.id.btnCustomList) {
            intent.putExtra(MyConstants.HEADER_SMALL,MyConstants.MY_LIST_CAMEL_CASE);
            intent.putExtra(MyConstants.SHOW_SMALL,MyConstants.TRUE_STRING);
            startActivity(intent);
        }
        else if (id == R.id.btnDeleteDefault) {
            new AlertDialog.Builder(this)
                    .setTitle("Delete default data")
                    .setMessage("Are you sure?\n\nAs this will delete the data provided by(Pack Your Bag)while instailling.")
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            appData.persistDataByCategory(header,true);
                            itemsList=database.mainDao().getAll(header);
                            updateRecycler(itemsList);

                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setIcon(R.drawable.warning)
                    .show();
        }
        else if (id == R.id.btnReset) {
            new AlertDialog.Builder(this)
                    .setTitle("Reset to default")
                    .setMessage("Are you sure?\n\nAs this will load the default data provided by(pack your Bag)"+"and will delete the custom data you have added in("+header+")")
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            appData.persistDataByCategory(header,false);
                            itemsList=database.mainDao().getAll(header);
                            updateRecycler(itemsList);

                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setIcon(R.drawable.warning)
                    .show();
        }
        else if (id == R.id.btnAboutUs) {
            intent=new Intent(this,AboutUs.class);
            startActivity(intent);
        }
        else if (id == R.id.btnExit) {
            this.finishAffinity();
            Toast.makeText(this, "Pack your bag|\nExit completed.", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,@NonNull Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode==101){
            itemsList=database.mainDao().getAll(header);
            updateRecycler(itemsList);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chcklist);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        header=intent.getStringExtra(MyConstants.HEADER_SMALL);
        show=intent.getStringExtra(MyConstants.SHOW_SMALL);

        getSupportActionBar().setTitle(header);


        txtAdd=findViewById(R.id.txtAddition);
        btnAdd=findViewById(R.id.btnAddition);
        recyclerView=findViewById(R.id.recyclerview);
        linearLayout=findViewById(R.id.linearLayout);

        database=RoomDB.getInstance(this);


        if (MyConstants.FALSE_STRING.equals(show)){
            linearLayout.setVisibility(View.GONE);
            itemsList=database.mainDao().getAllSelected(true);
        }else {
            itemsList=database.mainDao().getAll(header);
        }

        updateRecycler(itemsList);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName=txtAdd.getText().toString();
                if (itemName!=null && !itemName.isEmpty()){
                    addNewItem(itemName);
                    Toast.makeText(Chcklist.this, "Item Added", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(Chcklist.this, "Empty cant be added", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

    private void addNewItem(String itemName){
        Items items=new Items();
        items.setChecked(false);
        items.setCategory(header);
        items.setItemname(itemName);
        items.setAddedby(MyConstants.USER_SMALL);
        database.mainDao().saveItem(items);
        itemsList=database.mainDao().getAll(header);
        updateRecycler(itemsList);
        recyclerView.scrollToPosition(checkListAdapter.getItemCount()-1);
        txtAdd.setText("");
    }

    private void updateRecycler(List<Items> itemsList){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        checkListAdapter=new CheckListAdapter(Chcklist.this,itemsList,database,show);
        recyclerView.setAdapter(checkListAdapter);

    }

}