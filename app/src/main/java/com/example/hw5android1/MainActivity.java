package com.example.hw5android1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainAdapter.ItemClickListener {
    private RecyclerView recyclerView;
    private List<MainModel> list;
    private MainAdapter adapter;
    private Button btnAdd;
    public static final String KEY1 = "key";
    private static final int REQUEST_CODE = 2;
    public static final int REQUEST_COD = 3;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        recyclerView = findViewById(R.id.recyclerView);
        btnAdd = findViewById(R.id.btn_add);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new MainAdapter(list, this);
        recyclerView.setAdapter(adapter);
        adapter.setOnClickListener(this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ApplicationActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            MainModel model = (MainModel) data.getSerializableExtra(ApplicationActivity.KEY);
            adapter.addData(model);
        }
        if (requestCode == REQUEST_COD && resultCode == RESULT_OK && data != null) {
            MainModel model = (MainModel) data.getSerializableExtra(ApplicationActivity.KEY);
            adapter.changeData(model, position);
        }
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP | ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT
    ) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView,
                              @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int positionDrag = viewHolder.getAdapterPosition();
            int positionTarget = target.getAdapterPosition();
            Collections.swap(adapter.list, positionDrag, positionTarget);
            adapter.notifyItemMoved(positionDrag, positionTarget);
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            adapter.list.remove(viewHolder.getAdapterPosition());
            adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
        }
    };

    @Override
    public void onItemClick(int position) {
        this.position = position;
        Intent intent = new Intent(MainActivity.this, ApplicationActivity.class);
        intent.putExtra(KEY1, adapter.list.get(position));
        startActivityForResult(intent, REQUEST_COD);
    }
}