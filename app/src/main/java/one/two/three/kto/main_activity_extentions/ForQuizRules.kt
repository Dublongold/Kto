package one.two.three.kto.main_activity_extentions

import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import one.two.three.kto.MainActivity
import one.two.three.kto.R

fun MainActivity.quizRulesView() {

    findViewById<ConstraintLayout>(R.id.main_activity).apply {
        setBackgroundResource(R.drawable.main_bg)
        currentView.value = MainActivity.CurrentView.QUIZ_RULES

        titleText = TextView(context)
        titleText.id = R.id.title_text
        titleText.text = getString(R.string.quiz_rules)
        titleText.layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT).apply {
            setMargins(0, 70.dp, 0, 0)
            topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        }
        titleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
        titleText.typeface = ResourcesCompat.getFont(context, R.font.roboto_bold)

        quizRulesText = TextView(context)
        quizRulesText.id = R.id.quiz_rules_text
        quizRulesText.text = getString(R.string.quiz_rules_text)
        quizRulesText.gravity = Gravity.CENTER
        quizRulesText.layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT).apply {
            setMargins(20.dp, 32.dp, 20.dp, 0)
            topToBottom = R.id.title_text
            startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        }
        quizRulesText.setPadding(20.dp, 0, 20.dp, 0)
        quizRulesText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        quizRulesText.typeface = ResourcesCompat.getFont(context, R.font.roboto_medium)

        quizRulesButton = AppCompatButton(context)
        quizRulesButton.layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            60.dp
        ).apply {
            setMargins(20.dp, 40.dp, 20.dp, 0)
            startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            topToBottom = R.id.quiz_rules_text
        }
        quizRulesButton.setBackgroundResource(R.drawable.button_selector)
        quizRulesButton.typeface = ResourcesCompat.getFont(context, R.font.roboto_medium)
        quizRulesButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        quizRulesButton.text = getString(R.string.resume_game)
        quizRulesButton.setOnClickListener {
            clearQuizRulesView()
            quizView()
        }

        addView(titleText)
        addView(quizRulesText)
        addView(quizRulesButton)
    }
}

fun MainActivity.clearQuizRulesView() {
    findViewById<ConstraintLayout>(R.id.main_activity).apply {
        removeView(titleText)
        removeView(quizRulesText)
        removeView(quizRulesButton)
    }
}

/*
    <TextView
        android:id="@+id/textView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="70dp"
        android:fontFamily="@font/roboto_bold"
        android:text="@string/privacy_policy"
        android:textSize="24sp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <TextView
        android:id="@+id/textView2"
        android:layout_marginTop="32dp"
        android:layout_marginHorizontal="20dp"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:gravity="center"
        android:fontFamily="@font/roboto_medium"
        android:text="@string/quiz_rules_text"
        android:textSize="16sp"
        app:layout_constraintTop_toBottomOf="@+id/textView" />

    <androidx.appcompat.widget.AppCompatButton
        android:layout_marginTop="40dp"
        android:layout_width="match_parent"
        android:layout_height="60dp"
        android:layout_marginHorizontal="20dp"
        android:background="@drawable/button_selector"
        android:fontFamily="@font/roboto_medium"
        android:text="@string/resume_game"
        android:textSize="16sp"
        android:textAllCaps="false"
        app:layout_constraintTop_toBottomOf="@+id/textView2" />
 */