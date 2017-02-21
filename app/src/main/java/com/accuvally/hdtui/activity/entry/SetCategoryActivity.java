package com.accuvally.hdtui.activity.entry;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.accuvally.hdtui.BaseActivity;
import com.accuvally.hdtui.R;
import com.accuvally.hdtui.adapter.CategoryAdapter;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.config.Keys;
import com.accuvally.hdtui.config.Url;
import com.accuvally.hdtui.manager.AccountManager;
import com.accuvally.hdtui.model.BaseResponse;
import com.accuvally.hdtui.utils.HttpCilents;
import com.accuvally.hdtui.utils.LoginUtil;
import com.alibaba.fastjson.JSON;

import java.util.ArrayList;

public class SetCategoryActivity extends BaseActivity implements View.OnClickListener{

    private GridView gridView;
    private TextView category_skip;
    private TextView category_select_entry;
    private CategoryAdapter categoryAdapter;
    private ArrayList<String> categoryList =new ArrayList<>();

    private ArrayList<String> select_categoryList =new ArrayList<>();

    public static final String fromSetting="fromSetting";
    private boolean fromset=false;//是否从设置界面进来
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_category);

        fromset=getIntent().getBooleanExtra(fromSetting,false);


        ArrayList<String> arrayList=getCacheCategorys();
        if(arrayList!=null){
            categoryList.addAll(arrayList);
        }

        initCacheSelectCategorys();

        initView();
        initData();


    }



    private boolean isInselect_categoryList(String s){
        if(select_categoryList.contains(s)){
            return true;
        }else {
            return false;
        }
    }

    private void initData(){
        HttpCilents httpCilents = new HttpCilents(SetCategoryActivity.this);
        httpCilents.get(Url.GET_PREQUERY_TAGS, new HttpCilents.WebServiceCallBack() {
            @Override
            public void callBack(int code, Object result) {
                switch (code) {
                    case Config.RESULT_CODE_SUCCESS:
                        BaseResponse messageInfo = JSON.parseObject(result.toString(), BaseResponse.class);
                        String categorys = messageInfo.getResult();
                        application.sharedUtils.writeString(Keys.categorys, categorys);//写入sharepreference中
                        categoryList.clear();
                        categoryList.addAll(JSON.parseArray(categorys, String.class)) ;
                        categoryAdapter.notifyDataSetChanged();
//                        Trace.e("", categorys);
                        application.sharedUtils.writeBoolean(Keys.have_select_categorys, true);//写入sharepreference中,开机不再进入这个界面
                        break;
                    case Config.RESULT_CODE_ERROR:
                        if (!fromset) {//从启动页进入
                            toCategoryNext();
                            finish();
                        }
                        break;
                }
            }
        });

    }

    private ArrayList<String> getCacheCategorys(){
        String str=application.sharedUtils.readString(Keys.categorys);
       return (ArrayList<String>) JSON.parseArray(str,String.class);
    }

    private void initCacheSelectCategorys(){
        String str=application.sharedUtils.readString(Keys.select_categorys);
        if(str!=null){
           String[] strings= str.split(",");
            for(String s:strings){
                select_categoryList.add(s);
            }
        }

    }


    public static String parseCacheSelectCategorys(ArrayList<String> cacheSelectCategorys){

        StringBuilder stringBuilder=new StringBuilder();
        int size=cacheSelectCategorys.size();
        for(int i=0;i<size;i++){
            stringBuilder.append(cacheSelectCategorys.get(i));
            if(i<size-1)
                stringBuilder.append(",");
        }
//        Trace.e("setCacheSelectCategorys",stringBuilder.toString());
        return stringBuilder.toString();

    }


    private void initView(){
        category_skip= (TextView) findViewById(R.id.category_skip);
        category_skip.setOnClickListener(this);
        category_select_entry= (TextView) findViewById(R.id.category_select_entry);
        category_select_entry.setOnClickListener(this);
        gridView= (GridView) findViewById(R.id.category_gridview);
        categoryAdapter=new CategoryAdapter(this, categoryList);
        gridView.setAdapter(categoryAdapter);
        categoryAdapter.setSelect_categoryList(select_categoryList);
        set_select_entry_background();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = categoryList.get((int) id);
                if (isInselect_categoryList(s)) {
                    select_categoryList.remove(s);
                    categoryAdapter.setSelect_categoryList(select_categoryList);
                } else {
                    select_categoryList.add(s);
                    categoryAdapter.setSelect_categoryList(select_categoryList);
                }
                set_select_entry_background();

            }
        });
    }

    private void set_select_entry_background(){
        if(select_categoryList.size()==0){
            category_select_entry.setBackgroundResource(R.drawable.category_background_entry_nor);
        }else {
            category_select_entry.setBackgroundResource(R.drawable.category_background_entry_set);

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        String s=parseCacheSelectCategorys(select_categoryList);
        application.sharedUtils.writeString(Keys.select_categorys, s);//退出时保存到本地

        if(AccountManager.checkIsLogin()){
            LoginUtil.setCategory(httpCilents, s);//退出的时候保存到服务器
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_set_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void toCategoryNext(){
        if(!fromset){
            if (!application.hasActivity(MainActivityNew.class)){
                Intent intent = new Intent(SetCategoryActivity.this, MainActivityNew.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.category_skip:
                toCategoryNext();
                finish();
                break;
            case R.id.category_select_entry:

                if(select_categoryList.size()!=0){
                    toCategoryNext();
                    finish();
                }else {
                    application.showMsg("请先选择你感兴趣的标签");
                }

                break;
        }
    }
}
