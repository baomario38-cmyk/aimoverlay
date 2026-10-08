package com.aimoverlay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class CrosshairView extends View {

    private Paint paintCross;
    private Paint paintCircle;
    private Paint paintDot;
    private Paint paintLine;
    private int screenW;
    private int screenH;
    private int size = 50;
    private int thickness = 4;

    public CrosshairView(Context context) {
        super(context);
        init();
    }

    private void init() {
        paintCross = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCross.setColor(Color.parseColor("#FF2B4D"));
        paintCross.setStrokeWidth(thickness);
        paintCross.setStyle(Paint.Style.STROKE);

        paintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCircle.setColor(Color.parseColor("#88FF2B4D"));
        paintCircle.setStrokeWidth(3f);
        paintCircle.setStyle(Paint.Style.STROKE);

        paintDot = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintDot.setColor(Color.parseColor("#FFFFFF"));
        paintDot.setStyle(Paint.Style.FILL);

        paintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintLine.setColor(Color.parseColor("#66FFFFFF"));
        paintLine.setStrokeWidth(2f);
        paintLine.setStyle(Paint.Style.STROKE);
    }

    public void setSize(int s) {
        this.size = s;
        invalidate();
    }

    public void setThickness(int t) {
        this.thickness = t;
        paintCross.setStrokeWidth(t);
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenW = w;
        screenH = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int cx = screenW / 2;
        int cy = screenH / 2;

        int crossSize = 40 + (size * 2);
        int gap = 12;
        int circleRadius = 120 + (size * 2);

        canvas.drawLine(cx - crossSize, cy, cx - gap, cy, paintCross);
        canvas.drawLine(cx + gap, cy, cx + crossSize, cy, paintCross);
        canvas.drawLine(cx, cy - crossSize, cx, cy - gap, paintCross);
        canvas.drawLine(cx, cy + gap, cx, cy + crossSize, paintCross);

        canvas.drawCircle(cx, cy, circleRadius, paintCircle);

        canvas.drawCircle(cx, cy, 5f, paintDot);

        canvas.drawLine(cx, 0, cx, cy - 200f, paintLine);
        canvas.drawLine(cx, cy + 200f, cx, screenH, paintLine);
        canvas.drawLine(0, cy, cx - 200f, cy, paintLine);
        canvas.drawLine(cx + 200f, cy, screenW, cy, paintLine);
    }
}
