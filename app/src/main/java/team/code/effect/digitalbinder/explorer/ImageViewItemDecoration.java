package team.code.effect.digitalbinder.explorer;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ImageViewItemDecoration extends RecyclerView.ItemDecoration {
    private final int size;
    public ImageViewItemDecoration(int size) {
        this.size = size;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.top = size;
        outRect.left = size;
        outRect.right = size;
    }
}
