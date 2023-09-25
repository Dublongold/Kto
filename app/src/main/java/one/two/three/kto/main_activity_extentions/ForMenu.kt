package one.two.three.kto.main_activity_extentions

import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import one.two.three.kto.MainApplication
import one.two.three.kto.R

fun MainApplication.menuView() {
    findViewById<ConstraintLayout>(R.id.main_activity).apply {
        setBackgroundResource(R.drawable.start_bg)
        currentView.value = MainApplication.CurrentView.MENU

        fun createButton(buttonId: Int, bottomTo: Int, text: Int, action: (View) -> Unit): AppCompatButton {
            val resultButton = AppCompatButton(this@menuView)
            resultButton.id = buttonId
            resultButton.setBackgroundResource(R.drawable.button_selector)
            resultButton.isAllCaps = false
            resultButton.layoutParams = ConstraintLayout.LayoutParams(320.dp, 60.dp).apply {
                setMargins(0, 0, 0, if(bottomTo != -1) 16.dp else 80.dp)
                startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                if(bottomTo != -1) {
                    bottomToTop = bottomTo
                }
                else {
                    bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                }
            }
            resultButton.text = getString(text)
            resultButton.typeface = ResourcesCompat.getFont(this@menuView, R.font.roboto_medium)
            resultButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            resultButton.setOnClickListener(action)
            return resultButton
        }
        manuPlayQuizButton = createButton(R.id.play_quiz_button, R.id.daily_reward_button, R.string.play_quiz) {
            clearMenuView()
            selectQuizView()
        }
        menuDailyRewardButton = createButton(R.id.daily_reward_button, R.id.privacy_policy_button, R.string.daily_reward) {
            clearMenuView()
            dailyRewardView()
        }
        menuPrivacyPolicyButton = createButton(R.id.privacy_policy_button, -1, R.string.privacy_policy) {
            clearMenuView()
            privacyPolicyView()
        }

        this as ViewGroup

        addView(menuPrivacyPolicyButton)
        addView(menuDailyRewardButton)
        addView(manuPlayQuizButton)
    }
}

fun MainApplication.clearMenuView() {
    findViewById<ConstraintLayout>(R.id.main_activity).apply {
        removeView(menuPrivacyPolicyButton)
        removeView(menuDailyRewardButton)
        removeView(manuPlayQuizButton)
    }
}

/*
    <androidx.appcompat.widget.AppCompatButton
        android:layout_width="320dp"
        android:layout_height="60dp"
        android:fontFamily="@font/roboto_medium"
        android:background="@drawable/button_selector"
        android:text="@string/play_quiz"
        android:textAllCaps="false"
        android:textSize="16sp"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
 */