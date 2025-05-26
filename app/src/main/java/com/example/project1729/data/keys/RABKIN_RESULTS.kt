package com.example.project1729.data.keys

import com.example.project1729.domain.model.RabkinTest

object RABKIN_RESULTS {

    var RABKIN_RIGHT_ANSWERS = 0
    var RABKIN_WRONG_ANSWERS = 0
    var RABKIN_PROTANOPY_ANSWERS = 0
    var RABKIN_DEITERANOPY_ANSWERS = 0
    var RABKIN_SHOW_ONLY_FROM_BASE = false

    var RABKIN_USED_TESTS = mutableListOf<RabkinTest>()

    const val RABKIN_MAX_RIGHT_ANSWERS = 10
    const val RABKIN_MAX_WRONG_ANSWERS = 3

    var SHOW_QUESTION = true


    var CURRENT_MEASURE = "-"
}