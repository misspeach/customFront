package com.example.shizhan.customfront.CustomManagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.shizhan.customfront.R;
import com.example.shizhan.customfront.adapter.CategoryAdapter;
import com.example.shizhan.customfront.model.Category;

import java.util.LinkedList;
import java.util.List;

public class CategoryChooseActivity extends AppCompatActivity {
    private ListView categoryListView;
    private List<Category> categories;
    private CategoryAdapter categoryAdapter;
    private String show_category = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("CategoryChooseActivity", "onCreate");
        setContentView(R.layout.activity_category_choose);
        Toolbar toolbar = (Toolbar) findViewById(R.id.add_toolbar);
        //设置返回键
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //把选中的那一项的textview值传给上一个活动
                Log.i("backtoAdd", "show_category");
                Intent intent = new Intent();
                intent.putExtra("category", show_category);
                setResult(RESULT_OK, intent);
                finish();//销毁当前活动
            }
        });
        initCategory();
        categoryListView = (ListView) findViewById(R.id.category_choose);
        categoryAdapter = new CategoryAdapter(CategoryChooseActivity.this, categories);
        categoryListView.setAdapter(categoryAdapter);
        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                categoryAdapter.clearPosition(position);
                show_category = categories.get(position).getCategoryName();
                Log.i("CategoryChooseActivity", show_category);
                categoryAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initCategory() {
        categories = new LinkedList<>();
        Category efficiency = new Category(R.mipmap.choose_pressed, "效率");
        categories.add(efficiency);
        Category exercise = new Category(R.mipmap.choose, "运动");
        categories.add(exercise);
        Category health = new Category(R.mipmap.choose, "健康");
        categories.add(health);
        Category learn = new Category(R.mipmap.choose, "学习");
        categories.add(learn);
        Category life = new Category(R.mipmap.choose, "生活");
        categories.add(life);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("CategoryChooseActivity", "onBackPressed");
        Intent intent = new Intent();
        intent.putExtra("category", show_category);
        setResult(RESULT_OK, intent);
        finish();
    }

}
