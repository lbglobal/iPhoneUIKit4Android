package com.wordplat.uikit.picker.wheel.lib;

import android.view.Gravity;

import com.wordplat.uikit.picker.R;

/**
 * Created by Sai on 15/8/9.
 */
public class PickerViewAnimateUtil {
    private static final int INVALID = -1;
    /**
     * Get default animation resource when not defined by the user
     *
     * @param gravity       the gravity of the dialog
     * @param isInAnimation determine if is in or out animation. true when is is
     * @return the id of the animation resource
     */
    public static int getAnimationResource(int gravity, boolean isInAnimation) {
        switch (gravity) {
            case Gravity.BOTTOM:
                return isInAnimation ? R.anim.wheelpicker_slide_in : R.anim.wheelpicker_slide_out;
        }
        return INVALID;
    }
}
