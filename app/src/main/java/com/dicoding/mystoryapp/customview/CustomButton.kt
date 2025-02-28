package com.dicoding.mystoryapp.customview

import android.app.ActionBar
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.dicoding.mystoryapp.R
import com.dicoding.mystoryapp.Utils

class CustomButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

    init {
        backgroundTintList = context.getColorStateList(R.color.navy)

        text = resources.getString(R.string.logout)
        setTextColor(Color.WHITE)
        textSize = 16f
        paint.isFakeBoldText = true
        layoutParams = ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)

        val paddingVertical = resources.getDimensionPixelSize(R.dimen.button_padding_vertical)
        val paddingHorizontal = resources.getDimensionPixelSize(R.dimen.button_padding_horizontal)
        setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical)

        val cornerRadius = resources.getDimension(R.dimen.button_corner_radius)
        background = Utils.createRoundRectDrawable(cornerRadius, ContextCompat.getColor(context, R.color.navy))

        val drawableStart = ContextCompat.getDrawable(context, R.drawable.ic_logout)
        drawableStart?.setBounds(0, 0, drawableStart.intrinsicWidth, drawableStart.intrinsicHeight)
        setCompoundDrawables(drawableStart, null, null, null)
        compoundDrawablePadding = resources.getDimensionPixelSize(R.dimen.icon_text_spacing)
    }
}