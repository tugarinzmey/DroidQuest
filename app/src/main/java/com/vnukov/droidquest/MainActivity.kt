package com.vnukov.droidquest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    companion object {
        private val TAG = "QuestActivity"
        private val KEY_INDEX = "index"
        private val REQUEST_CODE_DECEIT = 0
        private val DECEITER = "deceit"
    }


    private lateinit var mTrueButton: Button
    private lateinit var mFalseButton: Button
    private lateinit var mNextButton: ImageButton
    private lateinit var mPreviousButton: ImageButton
    private lateinit var mQuestionTextView: TextView
    private lateinit var mDeceitButton: Button
    private val mQuestionBank = listOf(
        Question(R.string.question_android, true, ),
        Question(R.string.question_linear, false),
        Question(R.string.question_service, false),
        Question(R.string.question_res, true),
        Question(R.string.question_manifest, true),
        Question(R.string.question_creator, false),
        Question(R.string.question_popularity, true),
        Question(R.string.question_maps, true),
        Question(R.string.question_volumes, true),
        Question(R.string.question_logo, true),
    )
    private var mCurrentIndex = 0
    private var mIsDeceiter = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate вызван")

        if (savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0)
            mIsDeceiter = savedInstanceState.getBoolean(DECEITER, false)
        }

        mTrueButton = findViewById(R.id.true_button)
        mTrueButton.setOnClickListener{
            checkAnswer(true)
        }

        mFalseButton = findViewById(R.id.false_button)
        mFalseButton.setOnClickListener{
            checkAnswer(false)
        }

        mQuestionTextView = findViewById(R.id.question_text_view)

        mNextButton = findViewById(R.id.next_button)
        mNextButton.setOnClickListener {
            mIsDeceiter = false
            switchQuestion(true)
        }

        mPreviousButton = findViewById(R.id.previous_button)
        mPreviousButton.setOnClickListener {
            switchQuestion(false)
        }

        mDeceitButton = findViewById(R.id.deceit_button)
        mDeceitButton.setOnClickListener {
            val answerIsTrue = mQuestionBank[mCurrentIndex].answerTrue
            mQuestionBank[mCurrentIndex].deceited = true
            val intent = DeceitActivity.newIntent(this, answerIsTrue)
            if (intent != null) {
                startActivityForResult(intent, REQUEST_CODE_DECEIT)
            }
        }
        updateQuestion()
    }

    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: Intent?) {
        if (resultCode != RESULT_OK){
            return
        }
        if (requestCode == REQUEST_CODE_DECEIT){
            if (data == null){
                return
            }
            mIsDeceiter = DeceitActivity.wasAnswerShown(result = data)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart вызван")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause вызван")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume вызван")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop вызван")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy вызван")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState вызван")
        outState!!.putInt(KEY_INDEX, mCurrentIndex)
        outState!!.putBoolean(DECEITER, mIsDeceiter)
    }

    private fun updateQuestion() {
        val question = mQuestionBank[mCurrentIndex].textResId
        mQuestionTextView.setText(question)
    }
    private fun checkAnswer(userPressedTrue: Boolean)  {
        val answerIsTrue = mQuestionBank[mCurrentIndex].answerTrue

        val messageResId = if (mIsDeceiter || mQuestionBank[mCurrentIndex].deceited) R.string.judgment_toast
        else if (userPressedTrue == answerIsTrue) {
            R.string.correct_toast
        }
        else {
            R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

    private fun switchQuestion(next: Boolean) {
        if (next){
            mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.size
        } else {
            if (mCurrentIndex > 0)
            mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.size
        }
        updateQuestion()
    }
}