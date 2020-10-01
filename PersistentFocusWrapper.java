import android.content.Context;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;

import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PersistentFocusWrapper extends FrameLayout {
    private static final String TAG = "PersistentFocusWrapper";
    private static final boolean DEBUG = false;

    private int mSelectedPosition = -1;

    /**
     * By default, focus is persisted when searching vertically
     * but not horizontally.
     */
    private boolean mPersistFocusVertical = true;

    public PersistentFocusWrapper(@NonNull Context context) {
        super(context);
    }

    public PersistentFocusWrapper(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PersistentFocusWrapper(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    int getGrandChildCount() {
        ViewGroup wrapper = (ViewGroup) getChildAt(0);
        return wrapper == null ? 0 : wrapper.getChildCount();
    }

    /**
     * Clears the selected position and clears focus.
     */
    public void clearSelection() {
        mSelectedPosition = -1;
        if (hasFocus()) {
            clearFocus();
        }
    }

    /**
     * Persist focus when focus search direction is up or down.
     */
    public void persistFocusVertical() {
        mPersistFocusVertical = true;
    }

    /**
     * Persist focus when focus search direction is left or right.
     */
    public void persistFocusHorizontal() {
        mPersistFocusVertical = false;
    }

    private boolean shouldPersistFocusFromDirection(int direction) {
        return ((mPersistFocusVertical && (direction == FOCUS_UP || direction == FOCUS_DOWN)) ||
                (!mPersistFocusVertical && (direction == FOCUS_LEFT || direction == FOCUS_RIGHT)));
    }

    @Override
    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        if (DEBUG) Log.v(TAG, "addFocusables");
        if (hasFocus() || getGrandChildCount() == 0 ||
                !shouldPersistFocusFromDirection(direction)) {
            super.addFocusables(views, direction, focusableMode);
        } else {
            // Select a child in requestFocus
            views.add(this);
        }
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
        View view = focused;
        while (view != null && view.getParent() != child) {
            view = (View) view.getParent();
        }
        mSelectedPosition = view == null ? -1 : ((ViewGroup) child).indexOfChild(view);
        if (DEBUG) Log.v(TAG, "requestChildFocus focused " + focused + " mSelectedPosition " + mSelectedPosition);
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        if (DEBUG) Log.v(TAG, "requestFocus mSelectedPosition " + mSelectedPosition);
        ViewGroup wrapper = (ViewGroup) getChildAt(0);
        if (wrapper != null && mSelectedPosition >= 0 && mSelectedPosition < getGrandChildCount()) {
            if (wrapper.getChildAt(mSelectedPosition).requestFocus(
                    direction, previouslyFocusedRect)) {
                return true;
            }
        }
        return super.requestFocus(direction, previouslyFocusedRect);
    }

    static class SavedState extends View.BaseSavedState {

        int mSelectedPosition;

        SavedState(Parcel in) {
            super(in);
            mSelectedPosition = in.readInt();
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(mSelectedPosition);
        }

        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        if (DEBUG) Log.v(TAG, "onSaveInstanceState");
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.mSelectedPosition = mSelectedPosition;
        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        mSelectedPosition = ((SavedState) state).mSelectedPosition;
        if (DEBUG) Log.v(TAG, "onRestoreInstanceState mSelectedPosition " + mSelectedPosition);
        super.onRestoreInstanceState(savedState.getSuperState());
    }
}
