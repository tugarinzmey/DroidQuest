package com.vnukov.droidquest

data class Question(val textResId: Int,
                    val answerTrue: Boolean,
                    var deceited: Boolean = false){
}