package one.two.three.kto.main_activity_extentions

import android.util.TypedValue
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import one.two.three.kto.MainActivity
import one.two.three.kto.R


fun MainActivity.loadingView() {
    findViewById<ConstraintLayout>(R.id.main_activity).apply {
        setBackgroundResource(R.drawable.start_bg)
        currentView.value = MainActivity.CurrentView.LOADING

        loadingLogoImage = ImageView(context)
        loadingText = TextView(context)

        loadingLogoImage.setImageResource(R.drawable.logo)
        loadingLogoImage.layoutParams = ConstraintLayout.LayoutParams(166.dp, 39.dp).apply {
            setMargins(0, 0, 0, 40.dp)
            bottomToTop = R.id.loading_text
            startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        }

        loadingText.id = R.id.loading_text
        loadingText.text = getString(R.string.loading)
        loadingText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        loadingText.layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT).apply {
            setMargins(0, 0, 0, 50.dp)
            bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
            startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        }
        loadingText.typeface = ResourcesCompat.getFont(context, R.font.roboto_medium)

        this as ViewGroup

        addView(loadingText)
        addView(loadingLogoImage)
    }
}

fun MainActivity.clearLoadingView() {
    findViewById<ConstraintLayout>(R.id.main_activity).apply {
        removeView(loadingText)
        removeView(loadingLogoImage)
    }
}

/*
    <ImageView
        android:layout_width="166dp"
        android:layout_height="39dp"
        android:layout_marginBottom="40dp"
        android:contentDescription="@string/logo_description"
        app:layout_constraintBottom_toTopOf="@+id/textView"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:srcCompat="@drawable/logo" />

    <TextView
        android:id="@+id/textView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginBottom="50dp"
        android:fontFamily="@font/roboto_medium"
        android:text="@string/loading"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent" />
 */