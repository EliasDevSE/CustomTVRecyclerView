import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.Rect;


public class CustomLayoutManager extends LinearLayoutManager {
    private int mExtraLayoutSpace = 500;

    public CustomLayoutManager(Context context) {
        super(context);
    }

    public CustomLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public CustomLayoutManager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, 0);
    }

    @Override
    public boolean requestChildRectangleOnScreen(RecyclerView parent, View child, Rect rect, boolean immediate, boolean focusedChildVisible) {
            return parent.requestChildRectangleOnScreen(child, rect, immediate);
    }

    @Override
    protected int getExtraLayoutSpace(RecyclerView.State state) {
        return mExtraLayoutSpace;
    }

    public void setExtraLayoutSpace(int extraLayoutSpace) {
        mExtraLayoutSpace = extraLayoutSpace;
    }
}
