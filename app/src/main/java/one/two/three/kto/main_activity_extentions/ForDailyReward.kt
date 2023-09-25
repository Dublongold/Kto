package one.two.three.kto.main_activity_extentions

import android.util.TypedValue
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import one.two.three.kto.MainActivity
import one.two.three.kto.R
import java.util.Calendar

fun MainActivity.dailyRewardView() {
    findViewById<ConstraintLayout>(R.id.main_activity).apply {
        setBackgroundResource(R.drawable.main_bg)
        currentView.value = MainActivity.CurrentView.DAILY_REWARD

        setBackButton {
            clearDailyRewardView()
            menuView()
        }
        setTitleText(R.string.daily_reward)

        dailyRewardText = TextView(context)
        dailyRewardText.id = R.id.daily_reward_text
        dailyRewardText.text = getString(R.string.daily_reward_first_open)
        dailyRewardText.typeface = ResourcesCompat.getFont(context, R.font.roboto_medium)
        dailyRewardText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        dailyRewardText.layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(0, 24.dp, 0, 0)
            endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            topToBottom = R.id.back_button
        }

        dailyRewardImage = ImageView(context)
        dailyRewardImage.id = R.id.daily_reward_image
        dailyRewardImage.setImageResource(if(dailyRewardTime.value > 0) R.drawable.gold else R.drawable.box)
        dailyRewardImage.layoutParams = ConstraintLayout.LayoutParams(
            268.dp, 188.dp
        ).apply {
            bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
            topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        }

        dailyRewardTimer = TextView(context)
        dailyRewardTimer.id = R.id.daily_reward_timer
        dailyRewardTimer.typeface = ResourcesCompat.getFont(context, R.font.roboto_medium)
        dailyRewardTimer.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        dailyRewardTimer.layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(0, 0, 0, 24.dp)
            endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            bottomToTop = R.id.daily_reward_button
        }

        dailyRewardButton = AppCompatButton(context)
        dailyRewardButton.id = R.id.daily_reward_button
        dailyRewardButton.layoutParams = ConstraintLayout.LayoutParams(
            320.dp, 60.dp
        ).apply {
            setMargins(0, 0, 0, 56.dp)
            bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
            endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            startToStart = ConstraintLayout.LayoutParams.PARENT_ID
        }
        dailyRewardButton.setBackgroundResource(R.drawable.button_selector)
        dailyRewardButton.typeface = ResourcesCompat.getFont(context, R.font.roboto_medium)
        dailyRewardButton.text = getString(R.string.get_reward)
        dailyRewardButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        dailyRewardButton.setOnClickListener {
            dailyRewardTime.value = 86_400
            getSharedPreferences("daily_reward", AppCompatActivity.MODE_PRIVATE)
                .edit().putLong("time", Calendar.getInstance().time.time + 86_400_000).apply()
        }

        dailyRewardTimerJob = lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                dailyRewardTime.collect {
                    if(it > 0) {
                        dailyRewardButton.isEnabled = false
                        dailyRewardTimer.text = getString(
                            R.string.timer,
                            it / 3600,
                            it / 60 % 60,
                            it % 60
                        )
                        if(it == 86400L) {
                            dailyRewardImage.setImageResource(R.drawable.gold)
                            dailyRewardText.text = getString(R.string.daily_reward_open_box)
                        }
                    }
                    else {
                        dailyRewardTimer.text = ""
                        dailyRewardButton.isEnabled = true
                        dailyRewardImage.setImageResource(R.drawable.box)
                        dailyRewardText.text = getString(R.string.daily_reward_first_open)
                    }
                }
            }
        }

        addView(backButton)
        addView(titleText)
        addView(dailyRewardText)
        addView(dailyRewardImage)
        addView(dailyRewardTimer)
        addView(dailyRewardButton)
    }
}

fun MainActivity.clearDailyRewardView() {
    findViewById<ConstraintLayout>(R.id.main_activity).apply {
        removeView(backButton)
        removeView(titleText)
        removeView(dailyRewardText)
        removeView(dailyRewardImage)
        removeView(dailyRewardTimer)
        removeView(dailyRewardButton)
        dailyRewardTimerJob.cancel()
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
        android:text="@string/daily_reward"
        android:textSize="24sp"
        app:layout_constraintBottom_toBottomOf="@+id/imageButton"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="@+id/imageButton" />

    <TextView
        android:id="@+id/textView2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="24dp"
        android:fontFamily="@font/roboto_medium"
        android:text="@string/daily_reward_first_open"
        android:textSize="16sp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/imageButton" />

    <ImageView
        android:layout_width="268dp"
        android:layout_height="188dp"
        app:srcCompat="@drawable/box"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginBottom="24dp"
        android:fontFamily="@font/roboto_medium"
        android:text="@string/timer"
        android:textSize="16sp"
        app:layout_constraintBottom_toTopOf="@+id/appCompatButton2"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"/>

    <androidx.appcompat.widget.AppCompatButton
        android:id="@+id/appCompatButton2"
        android:layout_width="320dp"
        android:layout_height="60dp"
        android:layout_marginBottom="56dp"
        android:background="@drawable/button_selector"
        android:fontFamily="@font/roboto_medium"
        android:text="@string/get_reward"
        android:textSize="16sp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent" />
 */