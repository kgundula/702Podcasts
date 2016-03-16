package defensivethinking.co.za.a702podcasts.utility;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import defensivethinking.co.za.a702podcasts.R;

/**
 * Created by Profusion on 2016-02-16.
 */
public class RecylerViewDividerItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;

    public RecylerViewDividerItemDecoration(Context context) {
        mDivider =  ContextCompat.getDrawable(context, R.drawable.line_divider);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++ ) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top +mDivider.getIntrinsicHeight();

            mDivider.setBounds(left,top, right, bottom);
            mDivider.draw(c);
        }

    }
}
