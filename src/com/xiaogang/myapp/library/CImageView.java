package com.xiaogang.myapp.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.xiaogang.myapp.R;


/**
 *
 * @author Szugyi
 * Custom ImageView for the CircleLayout class.
 * Makes it possible for the image to have an angle, position and a name.
 * Angle is used for the positioning in the circle menu.
 */
public class CImageView extends LinearLayout {

    private float angle = 0;
    private int position = 0;
    private String name;

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    /**
     * @param context
     */
    public CImageView(Context context) {
        this(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public CImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    @SuppressLint("NewApi")
    public CImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs,
                    R.styleable.CImageView);


        }
    }

}
