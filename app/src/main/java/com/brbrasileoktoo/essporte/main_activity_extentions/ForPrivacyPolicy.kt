package com.brbrasileoktoo.essporte.main_activity_extentions

import android.util.TypedValue
import android.widget.FrameLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.setPadding
import com.brbrasileoktoo.essporte.MainApplication
import com.brbrasileoktoo.essporte.R

fun MainApplication.privacyPolicyView() {
    findViewById<ConstraintLayout>(R.id.main_activity).apply {
        setBackgroundResource(R.drawable.main_bg)
        currentView.value = MainApplication.CurrentView.PRIVACY_POLICY

        setBackButton {
            clearPrivacyPolicyView()
            menuView()
        }
        setTitleText(R.string.privacy_policy)

        privacyPolicyTextScrollView = ScrollView(context)
        privacyPolicyTextScrollView.layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT, 0.dp).apply {
            setMargins(20.dp, 24.dp, 20.dp, 50.dp)
            bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
            startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            topToBottom = R.id.back_button
        }
        privacyPolicyTextScrollView.setPadding(16.dp)
        privacyPolicyTextScrollView.setBackgroundResource(R.drawable.privacy_policy_text_bg)
        privacyPolicyText = TextView(context)
        privacyPolicyText.text = getString(R.string.privacy_policy_text)
        privacyPolicyText.typeface = ResourcesCompat.getFont(context, R.font.roboto_medium)
        privacyPolicyText.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT)
        privacyPolicyText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)

        privacyPolicyTextScrollView.addView(privacyPolicyText)

        addView(backButton)
        addView(titleText)
        addView(privacyPolicyTextScrollView)
    }
}

fun MainApplication.clearPrivacyPolicyView() {
    findViewById<ConstraintLayout>(R.id.main_activity).apply {
        removeView(backButton)
        removeView(titleText)
        removeView(privacyPolicyTextScrollView)
    }
}

/*
<ImageButton
    android:id="@+id/imageButton"
    android:layout_width="40dp"
    android:layout_height="40dp"
    android:layout_marginStart="20dp"
    android:layout_marginTop="64dp"
    android:background="@drawable/button_selector"
    android:scaleType="centerCrop"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toTopOf="parent"
    app:srcCompat="@drawable/back_icon" />

<TextView
    android:id="@+id/textView"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:fontFamily="@font/roboto_bold"
    android:text="@string/privacy_policy"
    android:textSize="24sp"
    app:layout_constraintBottom_toBottomOf="@+id/imageButton"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toTopOf="@+id/imageButton" />

<ScrollView
    android:layout_width="match_parent"
    android:layout_height="0dp"
    android:layout_marginHorizontal="20dp"
    android:layout_marginTop="24dp"
    android:layout_marginBottom="50dp"
    android:padding="16dp"
    android:background="@drawable/privacy_policy_text_bg"
    android:minHeight="100dp"
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toBottomOf="@+id/imageButton">

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="@string/privacy_policy_text"
        android:textSize="16sp"
        android:fontFamily="@font/roboto_medium"/>
</ScrollView>

 */