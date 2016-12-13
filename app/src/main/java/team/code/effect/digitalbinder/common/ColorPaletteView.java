package team.code.effect.digitalbinder.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class ColorPaletteView extends View {
    int index;
    boolean isClicked;

    public ColorPaletteView(Context context, int index) {
        super(context);
        this.index = index;
        this.isClicked = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {

    }
}
