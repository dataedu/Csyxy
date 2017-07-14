package com.dk.mp.core.view.edittext;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.dk.mp.core.util.DeviceUtil;

/**
 * Created by cobb on 2017/7/10.
 */

public class AutoAjustSizeTextView extends android.support.v7.widget.AppCompatTextView {
    private static float DEFAULT_MIN_TEXT_SIZE = 12;
    private static float DEFAULT_MAX_TEXT_SIZE = 20;
    private Context mContext;

    // Attributes
    private Paint testPaint;
    private float minTextSize, maxTextSize;

    public AutoAjustSizeTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.mContext = context;
        initialise();
    }

    private void initialise()
    {
        testPaint = new Paint();
        testPaint.set(this.getPaint());


        maxTextSize = this.getTextSize();

        if (maxTextSize <= DEFAULT_MIN_TEXT_SIZE)
        {
            maxTextSize = DEFAULT_MAX_TEXT_SIZE;
        }

        minTextSize = DEFAULT_MIN_TEXT_SIZE;
    };

    /**
     * Re size the font so the specified text fits in the text box * assuming
     * the text box is the specified width.
     */
    private void refitText(String text, int textWidth) {
        if (textWidth > 0) {
            int availableWidth = textWidth - this.getPaddingLeft() - this.getPaddingRight();
            float trySize =maxTextSize;
            testPaint.setTextSize(DeviceUtil.dip2px(mContext,trySize));
            while ((trySize > minTextSize) && (testPaint.measureText(text) > availableWidth)) {
                trySize -= 1;
                if (trySize <= minTextSize) {
                    trySize = minTextSize;
                    break;
                }
                testPaint.setTextSize(DeviceUtil.dip2px(mContext,trySize));

            }
            this.setTextSize(trySize);
        }
    };

    @Override
    protected void onTextChanged(CharSequence text, int start, int before, int after)
    {
        super.onTextChanged(text, start, before, after);
        refitText(text.toString(), this.getWidth());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        if (w != oldw)
        {
            refitText(this.getText().toString(), w);
        }
    }
}