package cn.socialclock.graph;

import android.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.View;

public class bgDrawableView extends View {

    public bgDrawableView(Context context) {
        super(context);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(R.color.black);
        ShapeDrawable sdTitleLine = new ShapeDrawable(new RectShape());
//        sdTitleLine.getPaint();
//        sdTitleLine.setBounds(this.getLeft(), this.getTop()+30, this.getRight(), this.getTop()+31);
//        sdTitleLine.draw(canvas);
        ShapeDrawable sdBaseLine = new ShapeDrawable(new RectShape());
//        sdBaseLine.getPaint();
//        sdBaseLine.setBounds(this.getLeft()+15, this.getBottom()/2+12, this.getRight()-15, this.getBottom()/2+13);
//        sdBaseLine.draw(canvas);
    }
}
