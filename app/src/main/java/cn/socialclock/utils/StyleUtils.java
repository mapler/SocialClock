package cn.socialclock.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.widget.TextView;
import cn.socialclock.R;

public class StyleUtils {

    public static Drawable getShapeGradientRed(Context context) {
        return 	context.getResources().getDrawable(R.drawable.gradient_red);
    }
    public static Drawable getShapeGradientBlue(Context context) {
        return 	context.getResources().getDrawable(R.drawable.gradient_blue);
    }
    public static Drawable getShapeGradientGray(Context context) {
        return 	context.getResources().getDrawable(R.drawable.gradient_gray);
    }
    public static void toBold(TextView tv){
        TextPaint tp = tv.getPaint();
        tp.setFakeBoldText(true);
    }
}