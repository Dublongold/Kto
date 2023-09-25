package one.two.three.kto.main_activity_extentions

import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import one.two.three.kto.MainApplication
import one.two.three.kto.R

fun MainApplication.selectQuizView() {
    findViewById<ConstraintLayout>(R.id.main_activity).apply {
        setBackgroundResource(R.drawable.main_bg)
        currentView.value = MainApplication.CurrentView.SELECT_QUIZ

        setBackButton {
            clearSelectQuizView()
            menuView()
        }
        setTitleText(R.string.select_quiz)

        selectQuizButtonsContainer = LinearLayoutCompat(context)
        selectQuizButtonsContainer.gravity = Gravity.CENTER
        selectQuizButtonsContainer.orientation = LinearLayoutCompat.VERTICAL
        selectQuizButtonsContainer.layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(20.dp, 24.dp, 20.dp, 0)
            endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            topToBottom = R.id.back_button
        }

        fun createQuizButton(buttonIcon: Int, buttonText: Int, action: (View) -> Unit): LinearLayoutCompat {
            val quizButton = LinearLayoutCompat(context)
            quizButton.setBackgroundResource(R.drawable.button_selector)
            quizButton.layoutParams = LinearLayoutCompat.LayoutParams(
                LinearLayoutCompat.LayoutParams.MATCH_PARENT, 76.dp
            ).apply {
                setMargins(0, 0, 0, 16.dp)
            }
            quizButton.setPadding(16.dp, 0, 0, 0)
            quizButton.isClickable = true
            quizButton.isFocusable = true
            quizButton.gravity = Gravity.CENTER_VERTICAL
            quizButton.orientation = LinearLayoutCompat.HORIZONTAL
            quizButton.setOnClickListener(action)
            
            val icon = ImageView(context)
            icon.setImageResource(buttonIcon)
            icon.layoutParams = LinearLayoutCompat.LayoutParams(60.dp, 60.dp)
            
            val text = TextView(context)
            text.layoutParams = LinearLayoutCompat.LayoutParams(
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(16.dp, 0, 0, 0)
            }
            text.typeface = ResourcesCompat.getFont(context, R.font.roboto_medium)
            text.text = getString(buttonText)
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)

            quizButton.addView(icon)
            quizButton.addView(text)

            return quizButton
        }
        selectQuizButtonsContainer.addView(
            createQuizButton(R.drawable.football_icon, R.string.football) {
                clearSelectQuizView()
                currentQuestion.value = 1
                time.value = 300
                selectedQuiz.value = QuizType.FOOTBALL
                quizView()
            }
        )
        selectQuizButtonsContainer.addView(
            createQuizButton(R.drawable.basketball_icon, R.string.basketball) {
                clearSelectQuizView()
                currentQuestion.value = 1
                time.value = 300
                selectedQuiz.value = QuizType.BASKETBALL
                quizView()
            }
        )
        selectQuizButtonsContainer.addView(
            createQuizButton(R.drawable.mixed_icon, R.string.mixed) {
                clearSelectQuizView()
                currentQuestion.value = 1
                time.value = 300
                selectedQuiz.value = QuizType.MIXED
                quizView()
            }
        )

        this as ViewGroup

        addView(backButton)
        addView(titleText)
        addView(selectQuizButtonsContainer)
    }
}

fun MainApplication.clearSelectQuizView() {
    findViewById<ConstraintLayout>(R.id.main_activity).apply {
        removeView(backButton)
        removeView(titleText)
        removeView(selectQuizButtonsContainer)
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

    <androidx.appcompat.widget.LinearLayoutCompat
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="24dp"
        android:layout_marginHorizontal="20dp"
        android:gravity="center"
        android:orientation="vertical"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/imageButton">

        <androidx.appcompat.widget.LinearLayoutCompat
            android:layout_width="match_parent"
            android:layout_height="76dp"
            android:layout_marginBottom="16dp"
            android:background="@drawable/button_selector"
            android:clickable="true"
            android:focusable="true"
            android:gravity="center_vertical|start"
            android:orientation="horizontal"
            android:paddingHorizontal="16dp">

            <ImageView
                android:layout_width="60dp"
                android:layout_height="60dp"
                app:srcCompat="@drawable/football_icon" />

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginStart="16dp"
                android:fontFamily="@font/roboto_medium"
                android:text="@string/football"
                android:textSize="16sp" />
        </androidx.appcompat.widget.LinearLayoutCompat>

        <androidx.appcompat.widget.LinearLayoutCompat
            android:layout_width="match_parent"
            android:layout_height="76dp"
            android:layout_marginBottom="16dp"
            android:background="@drawable/button_selector"
            android:clickable="true"
            android:focusable="true"
            android:gravity="center_vertical|start"
            android:orientation="horizontal"
            android:paddingHorizontal="16dp">

            <ImageView
                android:layout_width="60dp"
                android:layout_height="60dp"
                app:srcCompat="@drawable/basketball_icon" />

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginStart="16dp"
                android:fontFamily="@font/roboto_medium"
                android:text="@string/basketball"
                android:textSize="16sp" />
        </androidx.appcompat.widget.LinearLayoutCompat>

        <androidx.appcompat.widget.LinearLayoutCompat
            android:layout_width="match_parent"
            android:layout_height="76dp"
            android:layout_marginBottom="16dp"
            android:background="@drawable/button_selector"
            android:clickable="true"
            android:focusable="true"
            android:gravity="center_vertical|start"
            android:orientation="horizontal"
            android:paddingHorizontal="16dp">

            <ImageView
                android:layout_width="60dp"
                android:layout_height="60dp"
                app:srcCompat="@drawable/mixed_icon" />

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginStart="16dp"
                android:fontFamily="@font/roboto_medium"
                android:text="@string/mixed"
                android:textSize="16sp" />
        </androidx.appcompat.widget.LinearLayoutCompat>
    </androidx.appcompat.widget.LinearLayoutCompat>

 */