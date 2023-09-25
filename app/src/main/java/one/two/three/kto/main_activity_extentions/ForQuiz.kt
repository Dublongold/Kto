package one.two.three.kto.main_activity_extentions

import android.util.TypedValue
import android.view.Gravity
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Space
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import one.two.three.kto.MainApplication
import one.two.three.kto.R
import one.two.three.kto.helpfull.checkIfAnswerCorrect
import kotlin.random.Random

fun MainApplication.quizView() {
    val mixedRandomNumber = Random.nextInt(0, 2)
    findViewById<ConstraintLayout>(R.id.main_activity).apply {
        setBackgroundResource(R.drawable.main_bg)
        quizTime = TextView(context).also { textView ->
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
        currentView.value = MainApplication.CurrentView.QUIZ

        quizPauseButton = ImageButton(context)
        quizPauseButton.id = R.id.pause_button
        quizPauseButton.setImageResource(R.drawable.pause_icon)
        quizPauseButton.setBackgroundResource(R.drawable.button_selector)
        quizPauseButton.layoutParams = ConstraintLayout.LayoutParams(40.dp, 40.dp).apply {
            setMargins(20.dp, 64.dp, 0, 0)
            startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        }
        quizPauseButton.setOnClickListener {
            clearQuizView()
            quizResultView(QUIZ_PAUSED)
        }

        setTitleText(
            when(selectedQuiz.value) {
                QuizType.FOOTBALL -> R.string.football_quiz
                QuizType.BASKETBALL -> R.string.basketball_quiz
                QuizType.MIXED -> R.string.mixed_sports_quiz
            },
            R.id.pause_button
        )

        quizInfoButton = ImageButton(context)
        quizInfoButton.id = R.id.info_button
        quizInfoButton.setImageResource(R.drawable.info_icon)
        quizInfoButton.setBackgroundResource(R.drawable.button_selector)
        quizInfoButton.layoutParams = ConstraintLayout.LayoutParams(40.dp, 40.dp).apply {
            setMargins(0, 64.dp, 20.dp, 0)
            endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        }
        quizInfoButton.setOnClickListener {
            clearQuizView()
            quizRulesView()
        }

        quizInfoContainer = LinearLayoutCompat(context)
        quizInfoContainer.id =  R.id.quiz_info_container
        quizInfoContainer.layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            48.dp
        ).apply {
            setMargins(0, 21.dp, 0, 0)
            topToBottom = R.id.pause_button
            startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        }
        quizInfoContainer.gravity = Gravity.CENTER
        quizInfoContainer.weightSum = 1f
        quizInfoContainer.addView(
            LinearLayoutCompat(context).also {
                it.layoutParams = LinearLayoutCompat.LayoutParams(
                    0, LinearLayoutCompat.LayoutParams.MATCH_PARENT
                ).also { linearLayout ->
                    linearLayout.weight = 0.4f
                }
                it.gravity = Gravity.CENTER
                it.setBackgroundResource(R.drawable.quiz_info_bg)
                it.addView(
                    ImageView(context).also { imageView ->
                        imageView.layoutParams = LinearLayoutCompat.LayoutParams(
                            24.dp, 24.dp
                        )
                        imageView.setImageResource(R.drawable.quiz_icon)
                    }
                )
                it.addView(
                    TextView(context).also { textView ->
                        textView.id = R.id.completed_quizzes
                        textView.typeface = ResourcesCompat.getFont(context, R.font.roboto_medium)
                        textView.text = getString(R.string.completed_quizzes, currentQuestion.value)
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
            Space(context).also {
                it.layoutParams = LinearLayoutCompat.LayoutParams(
                    0.dp, LinearLayoutCompat.LayoutParams.MATCH_PARENT
                ).also {space ->
                    space.weight = 0.05f
                }
            }
        )
        quizInfoContainer.addView(
            LinearLayoutCompat(context).also {
                it.layoutParams = LinearLayoutCompat.LayoutParams(
                    0, LinearLayoutCompat.LayoutParams.MATCH_PARENT
                ).also { linearLayout ->
                    linearLayout.weight = 0.4f
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
                it.addView(quizTime)
            }
        )

        quizQuestion = TextView(context)
        quizQuestion.id = R.id.quiz_question
        quizQuestion.layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(0.dp, 32.dp, 0.dp, 0.dp)
            endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            topToBottom = R.id.quiz_info_container
        }
        quizQuestion.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
        quizQuestion.setPadding(20.dp, 0, 20.dp, 0)
        quizQuestion.typeface = ResourcesCompat.getFont(context, R.font.roboto_bold)
        quizQuestion.gravity = Gravity.CENTER
        quizQuestion.text = when(selectedQuiz.value) {
            QuizType.FOOTBALL -> {
                resources.getStringArray(R.array.football_questions)[currentQuestion.value - 1]
            }
            QuizType.BASKETBALL -> {
                resources.getStringArray(R.array.basketball_questions)[currentQuestion.value - 1]
            }
            QuizType.MIXED -> {
                listOf(
                    resources.getStringArray(R.array.football_questions),
                    resources.getStringArray(R.array.basketball_questions)
                )[mixedRandomNumber][currentQuestion.value - 1]
            }
        }

        quizAnswers = LinearLayoutCompat(context)
        quizAnswers.layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(20.dp, 24.dp, 20.dp, 0)
            endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            topToBottom = R.id.quiz_question
        }
        quizAnswers.orientation = LinearLayoutCompat.VERTICAL
        val answers = mutableListOf<AppCompatButton>()
        fun createAnswer(answerNumber: Int): AppCompatButton {
            val answer = AppCompatButton(context)
            answers.add(answer)
            answer.gravity = Gravity.START or Gravity.CENTER_VERTICAL
            answer.setPadding(16.dp, 8.dp, 16.dp, 8.dp)
            answer.layoutParams = LinearLayoutCompat.LayoutParams(
                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 16.dp)
            }
            answer.isAllCaps = false
            answer.includeFontPadding = false
            answer.typeface = ResourcesCompat.getFont(context, R.font.roboto_medium)
            answer.setBackgroundResource(R.drawable.quiz_answer_bg)
            answer.text = resources.getStringArray(
                when(selectedQuiz.value) {
                    QuizType.FOOTBALL -> R.array.football_questions_answers
                    QuizType.BASKETBALL -> R.array.basketball_questions_answers
                    QuizType.MIXED -> listOf(
                        R.array.football_questions_answers,
                        R.array.basketball_questions_answers
                    )[mixedRandomNumber]

                }
            )[(currentQuestion.value - 1) * (if(selectedQuiz.value == QuizType.FOOTBALL
                || (selectedQuiz.value == QuizType.MIXED && mixedRandomNumber == 0)) 4 else 3) + answerNumber]
            //
            //                  ACTION!!!
            //
            answer.setOnClickListener {
                val isFootball = selectedQuiz.value == QuizType.FOOTBALL
                        || (selectedQuiz.value == QuizType.MIXED && mixedRandomNumber == 0)
                val isCorrect = checkIfAnswerCorrect(isFootball,
                    currentQuestion.value, answerNumber + 1)
                if(isCorrect) {
                    currentQuestion.value += 1
                    if(currentQuestion.value >= 16) {
                        clearQuizView()
                        quizResultView(QUIZ_WIN)
                    }
                    else {
                        it.setBackgroundResource(R.drawable.button_shape)
                        answers.forEach { a ->
                            a.isEnabled = false
                            quizPauseButton.isEnabled = false
                            quizInfoButton.isEnabled = false
                        }
                        lifecycleScope.launch {
                            delay(500)
                            if(currentView.value == MainApplication.CurrentView.QUIZ) {
                                clearQuizView()
                                quizView()
                            }
                        }
                    }
                }
                else {
                    clearQuizView()
                    quizResultView(QUIZ_LOSE)
                    time.value = 0
                }
            }
            return answer
        }
        for(i in 0..(
                if(selectedQuiz.value == QuizType.FOOTBALL
                    || (selectedQuiz.value == QuizType.MIXED && mixedRandomNumber == 0)) 3 else 2 )
        ) {
            quizAnswers.addView(createAnswer(i))
        }

        addView(quizPauseButton)
        addView(titleText)
        addView(quizInfoButton)

        addView(quizInfoContainer)

        addView(quizQuestion)
        addView(quizAnswers)

    }
}

fun MainApplication.clearQuizView() {
    findViewById<ConstraintLayout>(R.id.main_activity).apply {
        removeView(quizPauseButton)
        removeView(titleText)
        removeView(quizInfoButton)
        removeView(quizInfoContainer)
        removeView(quizQuestion)
        removeView(quizAnswers)
    }
}

enum class QuizType() {
    FOOTBALL,
    BASKETBALL,
    MIXED
}

/*

    <ImageButton
        android:id="@id/pause_button"
        android:layout_width="40dp"
        android:layout_height="40dp"
        android:layout_marginStart="20dp"
        android:layout_marginTop="64dp"
        android:background="@drawable/button_selector"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:srcCompat="@drawable/pause_icon" />

    <TextView
        android:id="@+id/textView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:fontFamily="@font/roboto_bold"
        android:text="@string/football_quiz"
        android:textSize="24sp"
        app:layout_constraintBottom_toBottomOf="@+id/pause_button"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="@+id/pause_button" />


    <ImageButton
        android:id="@id/info_button"
        android:layout_width="40dp"
        android:layout_height="40dp"
        android:layout_marginEnd="20dp"
        android:layout_marginTop="64dp"
        android:background="@drawable/button_selector"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:srcCompat="@drawable/info_icon" />

        <androidx.appcompat.widget.LinearLayoutCompat
        android:id="@id/quiz_info_container"
        android:layout_width="match_parent"
        android:layout_height="48dp"
        android:layout_marginTop="21dp"
        android:gravity="center"
        android:weightSum="1"
        app:layout_constraintTop_toBottomOf="@+id/pause_button">

        <androidx.appcompat.widget.LinearLayoutCompat
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="0.4"
            android:background="@drawable/quiz_info_bg"
            android:gravity="center">

            <ImageView
                android:layout_width="24dp"
                android:layout_height="24dp"
                app:srcCompat="@drawable/quiz_icon" />

            <TextView
                android:id="@id/completed_quizzes"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:fontFamily="@font/roboto_medium"
                android:text="@string/completed_quizzes" />

        </androidx.appcompat.widget.LinearLayoutCompat>

        <Space
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="0.05" />

        <androidx.appcompat.widget.LinearLayoutCompat
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="0.4"
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
                android:fontFamily="@font/roboto_medium"
                android:text="@string/smaller_timer" />

        </androidx.appcompat.widget.LinearLayoutCompat>
    </androidx.appcompat.widget.LinearLayoutCompat>

    <TextView
        android:id="@id/quiz_question"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="32dp"
        android:paddingHorizontal="20dp"
        android:textSize="24sp"
        android:fontFamily="@font/roboto_bold"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/quiz_info_container"
        tools:text="What is basketball?" />


    <androidx.appcompat.widget.LinearLayoutCompat
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:layout_marginHorizontal="20dp"
        android:layout_marginTop="24dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/quiz_question">
        <androidx.appcompat.widget.AppCompatButton
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:gravity="start"
            android:textAllCaps="false"
            android:layout_marginBottom="16dp"
            android:paddingHorizontal="16dp"
            android:paddingVertical="8dp"
            android:fontFamily="@font/roboto_medium"
            android:background="@drawable/quiz_answer_shape"
            tools:text="a) is a sports game, the goal of which is to score the ball into the basket."/>
        <androidx.appcompat.widget.AppCompatButton
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:gravity="start"
            android:textAllCaps="false"
            android:layout_marginBottom="16dp"
            android:paddingHorizontal="16dp"
            android:paddingVertical="8dp"
            android:fontFamily="@font/roboto_medium"
            android:background="@drawable/quiz_answer_shape"
            tools:text="a) is a sports game, the goal of which is to score the ball into the basket."/>
        <androidx.appcompat.widget.AppCompatButton
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:gravity="start"
            android:textAllCaps="false"
            android:layout_marginBottom="16dp"
            android:paddingHorizontal="16dp"
            android:paddingVertical="8dp"
            android:fontFamily="@font/roboto_medium"
            android:background="@drawable/quiz_answer_shape"
            tools:text="c) is a sports game, the goal of which is to score as many balls as possible into the opponent's basket"/>
    </androidx.appcompat.widget.LinearLayoutCompat>
 */