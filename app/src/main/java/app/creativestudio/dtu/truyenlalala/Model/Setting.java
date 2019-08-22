package app.creativestudio.dtu.truyenlalala.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

import app.creativestudio.dtu.truyenlalala.Ref.AppConfig;

public class Setting {
    SharedPreferences sharedPreferences;
    public int textSize;
    public String textColor,backgroundColor;
    public Setting(Context context){
        sharedPreferences = context.getSharedPreferences(AppConfig.SHARED_REF_NAME, Context.MODE_PRIVATE);
        textSize = sharedPreferences.getInt("textSize",15);
        textColor = sharedPreferences.getString("textColor","#000000");
        backgroundColor = sharedPreferences.getString("backgroundColor","#ffffff");
    }

    public void updateSetting() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("textSize",textSize);
        editor.putString("textColor",textColor);
        editor.putString("backgroundColor",backgroundColor);
        editor.commit();
    }

    public int makeColorFromString(String str){
        return Color.parseColor(str);
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getTextColor() {
        return makeColorFromString(textColor);
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public int getBackgroundColor() {
        return makeColorFromString(backgroundColor);
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
