package one.two.three.kto

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import one.two.three.kto.main_activity_extentions.QUIZ_LOSE
import one.two.three.kto.main_activity_extentions.QUIZ_PAUSED
import one.two.three.kto.main_activity_extentions.QuizType
import one.two.three.kto.main_activity_extentions.clearDailyRewardView
import one.two.three.kto.main_activity_extentions.clearLoadingView
import one.two.three.kto.main_activity_extentions.clearPrivacyPolicyView
import one.two.three.kto.main_activity_extentions.clearQuizResultView
import one.two.three.kto.main_activity_extentions.clearQuizRulesView
import one.two.three.kto.main_activity_extentions.clearQuizView
import one.two.three.kto.main_activity_extentions.clearSelectQuizView
import one.two.three.kto.main_activity_extentions.loadingView
import one.two.three.kto.main_activity_extentions.menuView
import one.two.three.kto.main_activity_extentions.quizResultView
import one.two.three.kto.main_activity_extentions.quizView
import one.two.three.kto.models.FirebaseDataContainer
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    // For loading
    lateinit var loadingLogoImage: ImageView
    lateinit var loadingText: TextView
    // For menu
    lateinit var manuPlayQuizButton: AppCompatButton
    lateinit var menuDailyRewardButton: AppCompatButton
    lateinit var menuPrivacyPolicyButton: AppCompatButton
    // For privacy policy
    lateinit var privacyPolicyTextScrollView: ScrollView
    lateinit var privacyPolicyText: TextView
    // For daily reward
    lateinit var dailyRewardText: TextView
    lateinit var dailyRewardImage: ImageView
    lateinit var dailyRewardTimer: TextView
    lateinit var dailyRewardButton: AppCompatButton
    // For select quiz
    lateinit var selectQuizButtonsContainer: LinearLayoutCompat
    // For quiz
    lateinit var quizPauseButton: ImageButton
    lateinit var quizInfoButton: ImageButton
    lateinit var quizTime: TextView
    lateinit var quizInfoContainer: LinearLayoutCompat
    lateinit var quizQuestion: TextView
    lateinit var quizAnswers: LinearLayoutCompat
    // For quiz rules
    lateinit var quizRulesText: TextView
    lateinit var quizRulesButton: AppCompatButton
    // For quiz result
    lateinit var quizResultText: TextView
    lateinit var quizResultButtons: LinearLayoutCompat
    // For all
    lateinit var titleText: TextView
    lateinit var backButton: ImageButton
    // For quiz process
    val time = MutableStateFlow(0)
    val currentQuestion = MutableStateFlow(1)
    val selectedQuiz = MutableStateFlow(QuizType.FOOTBALL)
    // For daily reward process
    val dailyRewardTime = MutableStateFlow(0L)
    // For navigation
    var currentView = MutableStateFlow(CurrentView.LOADING)
    // For time
    private lateinit var timerJob: Job
    private lateinit var timeDecreasingJob: Job
    // For daily reward time
    lateinit var dailyRewardTimerJob: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        onBackPressedDispatcher.addCallback(MainOnBackPressedCallback())
        loadingView()
        lifecycleScope.launch {
            FirebaseApp.initializeApp(this@MainActivity)

            Firebase.remoteConfig.also {
                it.reset().await()

                it.fetchAndActivate().await()

                val dataContainer = FirebaseDataContainer(
                    link = it.getString("link"),
                    allow = it.getBoolean("allow")
                )

                if(dataContainer.allow && dataContainer.link.isNotEmpty()) {
                    Log.i("Main activity", "Allowed and url is not empty (${dataContainer.link}).")
                    startActivity(Intent(this@MainActivity, WebActivity::class.java).apply {
                        putExtra("data", dataContainer)
                    })
                }
                else {
                    Log.w("Main activity", "Allow is ${dataContainer.allow} and url is ${dataContainer.link}.")
                    val now = Calendar.getInstance().time.time
                    val timeFromFile = getSharedPreferences("daily_reward", MODE_PRIVATE).getLong("time", now)
                    dailyRewardTime.value = if(timeFromFile > Calendar.getInstance().time.time) {
                        (timeFromFile - Calendar.getInstance().time.time) / 1000
                    }
                    else {
                        0
                    }
                    clearLoadingView()
                    menuView()

                    lifecycleScope.launch {
                        while(!isDestroyed) {
                            delay(1000)
                            if(dailyRewardTime.value > 0) {
                                dailyRewardTime.value -= 1
                            }
                            else {
                                while(dailyRewardTime.value == 0L) {
                                    delay(100)
                                }
                            }
                        }
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                currentView.collect {
                    when(it) {
                        CurrentView.QUIZ -> {
                            timerJob = lifecycleScope.launch {
                                repeatOnLifecycle(Lifecycle.State.STARTED) {
                                    time.collect { time ->
                                        quizTime.text =
                                            getString(R.string.smaller_timer,
                                                time / 60, time % 60)
                                    }
                                }
                            }
                            timeDecreasingJob = lifecycleScope.launch {
                                while(time.value > 0) {
                                    delay(1000)
                                    if(!timerJob.isCancelled) {
                                        time.value -= 1
                                    }
                                }
                                if(currentView.value == CurrentView.QUIZ) {
                                    clearQuizView()
                                    quizResultView(QUIZ_LOSE)
                                }
                            }
                        }
                        CurrentView.QUIZ_RESULT, CurrentView.QUIZ_RULES -> {
                            timerJob.cancel()
                            timeDecreasingJob.cancel()
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private val dpFromResources by lazy {
        resources.displayMetrics.density
    }

    fun getTime(): Int {
        return 0
    }

    val Int.dp
        get() = (this * dpFromResources).toInt()

    inner class MainOnBackPressedCallback: OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            when(currentView.value) {
                CurrentView.MENU -> {
                    finish()
                }
                CurrentView.PRIVACY_POLICY -> {
                    clearPrivacyPolicyView()
                    menuView()
                }
                CurrentView.DAILY_REWARD -> {
                    clearDailyRewardView()
                    menuView()
                }
                CurrentView.SELECT_QUIZ -> {
                    clearSelectQuizView()
                    menuView()
                }
                CurrentView.QUIZ -> {
                    clearQuizView()
                    quizResultView(QUIZ_PAUSED)
                }
                CurrentView.QUIZ_RULES -> {
                    clearQuizRulesView()
                    quizView()
                }
                CurrentView.QUIZ_RESULT -> {
                    if(currentQuestion.value == 15 || time.value == 0) {
                        clearQuizResultView()
                        menuView()
                    }
                    else {
                        clearQuizResultView()
                        quizView()
                    }
                }
                CurrentView.LOADING -> {
                    Log.i("Back key", "Try close loading page.")
                }
            }
        }
    }
    enum class CurrentView {
        LOADING,
        MENU,
        DAILY_REWARD,
        PRIVACY_POLICY,
        SELECT_QUIZ,
        QUIZ,
        QUIZ_RULES,
        QUIZ_RESULT
    }
}