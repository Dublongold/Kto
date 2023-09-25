package com.brbrasileoktoo.essporte.main_activity_extentions

import android.util.TypedValue
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.brbrasileoktoo.essporte.MainApplication
import com.brbrasileoktoo.essporte.R

fun MainApplication.setBackButton(action: (View) -> Unit = {}) {
    backButton = ImageButton(this)
    backButton.id = R.id.back_button
    backButton.setImageResource(R.drawable.back_icon)
    backButton.setBackgroundResource(R.drawable.button_selector)
    backButton.layoutParams = ConstraintLayout.LayoutParams(40.dp, 40.dp).apply {
        setMargins(20.dp, 64.dp, 0, 0)
        startToStart = ConstraintLayout.LayoutParams.PARENT_ID
        topToTop = ConstraintLayout.LayoutParams.PARENT_ID
    }
    backButton.setOnClickListener(action)
    backButton.scaleType = ImageView.ScaleType.CENTER_CROP
}

fun MainApplication.setTitleText(text: Int, toView: Int = R.id.back_button) {
    titleText = TextView(this)
    titleText.text = getString(text)
    titleText.layoutParams = ConstraintLayout.LayoutParams(
        ConstraintLayout.LayoutParams.WRAP_CONTENT,
        ConstraintLayout.LayoutParams.WRAP_CONTENT).apply {
        bottomToBottom = toView
        topToTop = toView
        startToStart = ConstraintLayout.LayoutParams.PARENT_ID
        endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
    }
    titleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
    titleText.typeface = ResourcesCompat.getFont(this, R.font.roboto_bold)
}