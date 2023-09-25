package one.two.three.kto.helpfull

import one.two.three.kto.main_activity_extentions.QuizType

fun checkIfAnswerCorrect(isFootball: Boolean, questionNumber: Int, answerNumber: Int): Boolean {
    return if(isFootball) {
        when {
            questionNumber == 1 && answerNumber == 4 -> true
            questionNumber == 2 && answerNumber == 3 -> true
            questionNumber == 3 && answerNumber == 1 -> true
            questionNumber == 4 && answerNumber == 2 -> true
            questionNumber == 5 && answerNumber == 2 -> true
            questionNumber == 6 && answerNumber == 1 -> true
            questionNumber == 7 && answerNumber == 3 -> true
            questionNumber == 8 && answerNumber == 4 -> true
            questionNumber == 9 && answerNumber == 1 -> true
            questionNumber == 10 && answerNumber == 4 -> true
            questionNumber == 11 && answerNumber == 2 -> true
            questionNumber == 12 && answerNumber == 3 -> true
            questionNumber == 13 && answerNumber == 1 -> true
            questionNumber == 14 && answerNumber == 2 -> true
            questionNumber == 15 && answerNumber == 1 -> true
            else -> false
        }
    }
    else {
        when {
            questionNumber == 1 && answerNumber == 3 -> true
            questionNumber == 2 && answerNumber == 3 -> true
            questionNumber == 3 && answerNumber == 3 -> true
            questionNumber == 4 && answerNumber == 1 -> true
            questionNumber == 5 && answerNumber == 1 -> true
            questionNumber == 6 && answerNumber == 2 -> true
            questionNumber == 7 && answerNumber == 3 -> true
            questionNumber == 8 && answerNumber == 1 -> true
            questionNumber == 9 && answerNumber == 2 -> true
            questionNumber == 10 && answerNumber == 2 -> true
            questionNumber == 11 && answerNumber == 3 -> true
            questionNumber == 12 && answerNumber == 3 -> true
            questionNumber == 13 && answerNumber == 2 -> true
            questionNumber == 14 && answerNumber == 1 -> true
            questionNumber == 15 && answerNumber == 2 -> true
            else -> false
        }
    }
}