package com.mdgd.commons.ui.customs

import android.content.Context
import android.util.AttributeSet
import android.widget.ToggleButton

/**
 * Created by dan
 * on 21/03/2016.
 */

class SquareToggleButton : ToggleButton {

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context) : super(context)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measuredWidth
        val height = measuredHeight
        if (width < height) setMeasuredDimension(width, width)
        else setMeasuredDimension(height, height)
    }
}
