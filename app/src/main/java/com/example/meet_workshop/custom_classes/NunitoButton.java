package com.example.meet_workshop.custom_classes;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

public class NunitoButton extends AppCompatButton {
    public NunitoButton(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public NunitoButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public NunitoButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/nunito_normal.ttf");
        setTypeface(customFont);
    }
}
