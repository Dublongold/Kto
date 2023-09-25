package one.two.three.kto.main_activity_extentions

import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.allViews
import one.two.three.kto.MainApplication
import one.two.three.kto.R

fun MainApplication.quizResultView(resultCode: Int) {
    findViewById<ConstraintLayout>(R.id.main_activity).apply {
        setBackgroundResource(R.drawable.main_bg)
        currentView.value = MainApplication.CurrentView.QUIZ_RESULT

        titleText = TextView(context)
        titleText.id = R.id.title_text
        titleText.text = getString(when(resultCode) {
            QUIZ_WIN, QUIZ_LOSE -> R.string.quiz_over
            else -> R.string.quiz_paused
        })
        titleText.layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(0, 70.dp, 0, 0)
            topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        }
        titleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
        titleText.typeface = ResourcesCompat.getFont(context, R.font.roboto_bold)

        if(resultCode == QUIZ_WIN || resultCode == QUIZ_LOSE) {
            quizResultText = TextView(context)
            quizResultText.layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ).apply {
                setMargins(0, 40.dp, 0, 0)
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                topToBottom = R.id.title_text
            }
            quizResultText.text = getString(if(resultCode == QUIZ_WIN) R.string.good_result else R.string.bad_result)
            quizResultText.setTextColor((0xFF7D7E7D).toInt())
            quizResultText.typeface = ResourcesCompat.getFont(context, R.font.roboto_bold)
            quizResultText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
        }

        quizInfoContainer = LinearLayoutCompat(context)
        quizInfoContainer.id = R.id.quiz_info_container
        quizInfoContainer.layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(20.dp, 92.dp, 20.dp, 0)
            startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            topToBottom = R.id.title_text
        }
        quizInfoContainer.gravity = Gravity.CENTER
        quizInfoContainer.orientation = LinearLayoutCompat.VERTICAL
        quizInfoContainer.addView(
            LinearLayoutCompat(context).also {
                it.layoutParams = LinearLayoutCompat.LayoutParams(
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT, 48.dp
                ).apply {
                    setMargins(0, 0, 0, 16.dp)
                }
                it.gravity = Gravity.CENTER
                it.setBackgroundResource(R.drawable.quiz_info_bg)
                it.addView(
                    ImageView(context).also { imageView ->
                        imageView.layoutParams = LinearLayoutCompat.LayoutParams(
                            24.dp, 24.dp
                        )
                        imageView.setImageResource(R.drawable.timer_icon)
                    }
                )
                it.addView(
                    TextView(context).also { textView ->
                        textView.id = R.id.smaller_timer
                        textView.typeface = ResourcesCompat.getFont(context, R.font.roboto_medium)
                        textView.text = getString(R.string.smaller_timer, time.value / 60, time.value % 60)
                        textView.layoutParams = LinearLayoutCompat.LayoutParams(
                            LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                            LinearLayoutCompat.LayoutParams.WRAP_CONTENT
                        ).also {forMargin ->
                            forMargin.setMargins(8.dp, 0, 0, 0)
                        }
                    }
                )
            }
        )
        quizInfoContainer.addView(
            LinearLayoutCompat(context).also {
                it.layoutParams = LinearLayoutCompat.LayoutParams(
                    LinearLayoutCompat.LayoutParams.MATCH_PARENT, 48.dp
                )
                it.gravity = Gravity.CENTER
                it.setBackgroundResource(R.drawable.quiz_info_bg)
                it.addView(
                    ImageView(context).also { imageView ->
                        imageView.layoutParams = LinearLayoutCompat.LayoutParams(
                            24.dp, 24.dp
                        )
                        imageView.setImageResource(R.drawable.right_answer_icon)
                    }
                )
                it.addView(
                    TextView(context).also { textView ->
                        textView.id = R.id.right_answers
                        textView.typeface = ResourcesCompat.getFont(context, R.font.roboto_medium)
                        textView.text = getString(R.string.right_answers, currentQuestion.value - 1)
                        textView.layoutParams = LinearLayoutCompat.LayoutParams(
                            LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                            LinearLayoutCompat.LayoutParams.WRAP_CONTENT
                        ).also { forMargin ->
                            forMargin.setMargins(8.dp, 0, 0, 0)
                        }
                    }
                )
            }
        )

        fun createButton(type: Int): AppCompatButton {
            val resultButton = AppCompatButton(context)
            resultButton.setBackgroundResource(R.drawable.button_selector)
            resultButton.isAllCaps = false
            resultButton.layoutParams = LinearLayoutCompat.LayoutParams(
                LinearLayoutCompat.LayoutParams.MATCH_PARENT, 60.dp
            ).apply {
                setMargins(0, 0, 0, 16.dp)
            }
            resultButton.text = getString(when(type) {
                BUTTON_PLAY_AGAIN -> R.string.play_again
                BUTTON_TRY_AGAIN -> R.string.try_again
                BUTTON_RESUME -> R.string.resume
                BUTTON_RESTART_QUIZ -> R.string.restart_quiz
                else -> R.string.main_menu
            })
            resultButton.setOnClickListener(
                when(type) {
                    BUTTON_PLAY_AGAIN, BUTTON_TRY_AGAIN, BUTTON_RESTART_QUIZ -> { _: View ->
                        time.value = 300
                        currentQuestion.value = 1
                        clearQuizResultView()
                        quizView()
                    }
                    BUTTON_RESUME -> { _: View ->
                        clearQuizResultView()
                        quizView()
                    }
                    else -> { _: View ->
                        clearQuizResultView()
                        menuView()
                    }
                }
            )
            resultButton.typeface = ResourcesCompat.getFont(context, R.font.roboto_medium)
            resultButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)

            return resultButton
        }

        quizResultButtons = LinearLayoutCompat(context)
        quizResultButtons.layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(20.dp, 40.dp, 20.dp, 0)
            endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            topToBottom = R.id.quiz_info_container
        }
        quizResultButtons.orientation = LinearLayoutCompat.VERTICAL
        quizResultButtons.addView(
            createButton(when(resultCode) {
                QUIZ_WIN -> BUTTON_PLAY_AGAIN
                QUIZ_LOSE -> BUTTON_TRY_AGAIN
                else -> BUTTON_RESUME
            })
        )
        if(resultCode == QUIZ_PAUSED) {
            quizResultButtons.addView(
                createButton(BUTTON_RESTART_QUIZ)
            )
        }

        quizResultButtons.addView(
            createButton(BUTTON_MAIN_MENU)
        )

        addView(titleText)
        addView(quizInfoContainer)
        if(resultCode == QUIZ_WIN || resultCode == QUIZ_LOSE) {
            addView(quizResultText)
        }
        addView(quizResultButtons)

    }
}

fun MainApplication.clearQuizResultView() {
    findViewById<ConstraintLayout>(R.id.main_activity).apply {
        removeView(titleText)
        removeView(quizInfoContainer)
        val goodResult = getString(R.string.good_result)
        val badResult = getString(R.string.bad_result)
        val quizResultText = allViews.firstOrNull {
            it is TextView && (it.text == goodResult || it.text == badResult)
        }
        if(quizResultText != null) {
            removeView(quizResultText)
        }
        removeView(quizResultButtons)
    }
}


const val QUIZ_WIN = 0
const val QUIZ_LOSE = 1
const val QUIZ_PAUSED = 2

const val BUTTON_MAIN_MENU = 0
const val BUTTON_PLAY_AGAIN = 1
const val BUTTON_TRY_AGAIN = 2
const val BUTTON_RESUME = 3
const val BUTTON_RESTART_QUIZ = 4

/*

    <androidx.appcompat.widget.LinearLayoutCompat
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="40dp"
        android:layout_marginHorizontal="20dp"
        android:orientation="vertical"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/quiz_info_container">

        <androidx.appcompat.widget.AppCompatButton
            android:layout_width="match_parent"
            android:layout_height="60dp"
            android:layout_marginBottom="16dp"
            android:background="@drawable/button_selector"
            android:fontFamily="@font/roboto_medium"
            android:textAllCaps="false"
            android:text="@string/resume"/>
        <androidx.appcompat.widget.AppCompatButton
            android:layout_width="match_parent"
            android:layout_height="60dp"
            android:layout_marginBottom="16dp"
            android:background="@drawable/button_selector"
            android:fontFamily="@font/roboto_medium"
            android:textAllCaps="false"
            android:text="@string/restart_quiz"/>
        <androidx.appcompat.widget.AppCompatButton
            android:layout_width="match_parent"
            android:layout_height="60dp"
            android:layout_marginBottom="16dp"
            android:background="@drawable/button_selector"
            android:fontFamily="@font/roboto_medium"
            android:textAllCaps="false"
            android:text="@string/main_menu"/>
    </androidx.appcompat.widget.LinearLayoutCompat>


//////////////////


    <TextView
        android:id="@id/title_text"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="70dp"
        android:fontFamily="@font/roboto_bold"
        android:text="@string/quiz_paused"
        android:textSize="24sp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="40dp"
        android:fontFamily="@font/roboto_bold"
        android:text="@string/bad_result"
        android:textColor="#FF7D7E7D"
        android:textSize="24sp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/title_text" />

    <androidx.appcompat.widget.LinearLayoutCompat
        android:id="@id/quiz_info_container"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="92dp"
        android:layout_marginHorizontal="20dp"
        android:orientation="vertical"
        android:gravity="center"
        app:layout_constraintTop_toBottomOf="@id/title_text">

        <androidx.appcompat.widget.LinearLayoutCompat
            android:layout_width="match_parent"
            android:layout_height="48dp"
            android:layout_marginBottom="16dp"
            android:background="@drawable/quiz_info_bg"
            android:gravity="center">

            <ImageView
                android:layout_width="24dp"
                android:layout_height="24dp"
                app:srcCompat="@drawable/timer_icon" />

            <TextView
                android:id="@id/smaller_timer"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginStart="8dp"
                android:fontFamily="@font/roboto_medium"
                android:text="@string/smaller_timer" />

        </androidx.appcompat.widget.LinearLayoutCompat>
        <androidx.appcompat.widget.LinearLayoutCompat
            android:layout_width="match_parent"
            android:layout_height="48dp"
            android:background="@drawable/quiz_info_bg"
            android:gravity="center">

            <ImageView
                android:layout_width="24dp"
                android:layout_height="24dp"
                app:srcCompat="@drawable/right_answer_icon" />

            <TextView
                android:id="@id/completed_quizzes"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginStart="8dp"
                android:fontFamily="@font/roboto_medium"
                android:text="@string/right_answers" />

        </androidx.appcompat.widget.LinearLayoutCompat>

    </androidx.appcompat.widget.LinearLayoutCompat>
 */