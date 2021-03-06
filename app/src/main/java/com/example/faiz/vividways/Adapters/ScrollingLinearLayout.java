package com.example.faiz.vividways.Adapters;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Faiz on 7/22/2017.
 */

public class ScrollingLinearLayout extends LinearLayoutManager {

    private final int duration;
    public boolean isScroll = true;
    public ScrollingLinearLayout(Context context, int orientation, boolean reverseLayout, int duration) {
        super(context, orientation, reverseLayout);
        this.duration = duration;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state,
                                       int position) {
        View firstVisibleChild = recyclerView.getChildAt(0);
        int itemHeight = firstVisibleChild.getHeight();
        int currentPosition = recyclerView.getChildPosition(firstVisibleChild);
        int distanceInPixels = Math.abs((currentPosition - position) * itemHeight);
        if (distanceInPixels == 0) {
            distanceInPixels = (int) Math.abs(firstVisibleChild.getY());
        }
        SmoothScroller smoothScroller = new SmoothScroller(recyclerView.getContext(), distanceInPixels, duration);
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }


    private class SmoothScroller extends LinearSmoothScroller {
        private static final int TARGET_SEEK_SCROLL_DISTANCE_PX = 10000;
        private final float distanceInPixels;
        private final float duration;

        public SmoothScroller(Context context, int distanceInPixels, int duration) {
            super(context);
            this.distanceInPixels = distanceInPixels;
            float millisPerPx = calculateSpeedPerPixel(context.getResources().getDisplayMetrics());
            this.duration = distanceInPixels < TARGET_SEEK_SCROLL_DISTANCE_PX ? (int) (Math.abs(distanceInPixels) * millisPerPx) : duration;
        }

        @Override
        public PointF computeScrollVectorForPosition(int targetPosition) {
            return ScrollingLinearLayout.this.computeScrollVectorForPosition(targetPosition);
        }

        @Override
        protected int calculateTimeForScrolling(int dx) {
            float proportion = (float) dx / distanceInPixels;
            return (int) (duration * proportion);
        }
    }

    @Override
    public void setSmoothScrollbarEnabled(boolean enabled) {
      //  super.setSmoothScrollbarEnabled(enabled);
        this.isScroll = enabled;
    }
}
