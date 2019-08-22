package app.creativestudio.dtu.truyenlalala.Activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

import app.creativestudio.dtu.truyenlalala.Model.Setting;
import app.creativestudio.dtu.truyenlalala.R;
import app.creativestudio.dtu.truyenlalala.RecyclerViewAdapter.BookStoreRecyclerViewAdapter;
import app.creativestudio.dtu.truyenlalala.RecyclerViewAdapter.ColorRecyclerAdapter;
import app.creativestudio.dtu.truyenlalala.RecyclerViewAdapter.RecyclerViewHolder.ColorRecyclerViewHolder;

public class SettingActivity extends AppCompatActivity  {
    TextView textSizeTv,textDemoTv;
    LinearLayout backgroundDemoLayout;
    ImageView textColorIv,backgroundColorIv;
    SeekBar textSizeSeekbar ;
    Button applyBtn,cancelBtn;
    RecyclerView textColorRecyclerView,backgroundColorRecyclerView;
    //
    ColorRecyclerAdapter textColorAdapter,backgroundColorAdapter;
    String[] colors = {"#ffffff","#000000","#b3c6ff","#ffff80","#008577"};
    Setting setting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        viewSetup();
    }
    void viewSetup(){
        setting = new Setting(this);
        textSizeTv = findViewById(R.id.text_size_tv);
        textSizeTv.setText(setting.getTextSize()+"");
        textColorIv = findViewById(R.id.text_color_iv);
        textColorIv.setBackgroundColor((setting.getTextColor()));
        backgroundColorIv = findViewById(R.id.background_color_iv);
        backgroundColorIv.setBackgroundColor((setting.getBackgroundColor()));
        textDemoTv = findViewById(R.id.text_view_demo_color_and_size);
        textDemoTv.setTextColor((setting.getTextColor()));
        textDemoTv.setTextSize((float) setting.getTextSize());
        backgroundDemoLayout = findViewById(R.id.background_demo_color);
        backgroundDemoLayout.setBackgroundColor((setting.getBackgroundColor()));
        seekBarSetting();
        buttonSetting();
        recyclerViewSetup();
    }
    void seekBarSetting(){
        final int max = 30, min = 10, smallestShare = 10;
        int currrentSize = setting.getTextSize();
        textSizeSeekbar = findViewById(R.id.text_size_seek_bar);
        textSizeSeekbar.setMax(smallestShare);
        textSizeSeekbar.setProgress((int)((currrentSize-min)/((max-min)/smallestShare)));
        textSizeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                long progress = i;
                long result =  min + progress * (( max - min )/smallestShare);
                textDemoTv.setTextSize(result);
                setting.setTextSize((int)result);
                textSizeTv.setText(setting.getTextSize()+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    void buttonSetting(){
        applyBtn = findViewById(R.id.setting_apply_btn);
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setting.updateSetting();
                finish();
            }
        });
        cancelBtn = findViewById(R.id.setting_cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    void recyclerViewSetup(){
        //textColor
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        textColorRecyclerView = findViewById(R.id.text_color_recycler_view);
        textColorRecyclerView.setLayoutManager(layoutManager);
        ColorRecyclerAdapter.ColorRecyclerViewAdapterInterface textColorCallback = new ColorRecyclerAdapter.ColorRecyclerViewAdapterInterface() {
            @Override
            public void onBindViewHolder(ColorRecyclerViewHolder holder,  String[] color,  int position) {
                final String hexColor = color[position];
                holder.imageView.setBackgroundColor(setting.makeColorFromString(hexColor));
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        textColorIv.setBackgroundColor(setting.makeColorFromString(hexColor));
                        textDemoTv.setTextColor(setting.makeColorFromString(hexColor));
                        setting.setTextColor(hexColor);
                    }
                });
            }
        };
        textColorAdapter = new ColorRecyclerAdapter(colors,textColorCallback);
        textColorRecyclerView.setAdapter(textColorAdapter);
        //background color
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        backgroundColorRecyclerView = findViewById(R.id.background_color_recycler_view);
        backgroundColorRecyclerView.setLayoutManager(layoutManager1);
        ColorRecyclerAdapter.ColorRecyclerViewAdapterInterface backgroundColorCallback = new ColorRecyclerAdapter.ColorRecyclerViewAdapterInterface() {
            @Override
            public void onBindViewHolder(ColorRecyclerViewHolder holder, String[] color, int position) {
                final String hexColor = color[position];
                holder.imageView.setBackgroundColor(setting.makeColorFromString(color[position]));
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        backgroundColorIv.setBackgroundColor(setting.makeColorFromString(hexColor));
                        backgroundDemoLayout.setBackgroundColor(setting.makeColorFromString(hexColor));
                        setting.setBackgroundColor(hexColor);
                    }
                });
            }
        };
        backgroundColorAdapter = new ColorRecyclerAdapter(colors,backgroundColorCallback);
        backgroundColorRecyclerView.setAdapter(backgroundColorAdapter);
    }

}
