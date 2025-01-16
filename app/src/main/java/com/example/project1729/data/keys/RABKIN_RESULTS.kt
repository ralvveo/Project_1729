package com.example.project1729.data.keys

import com.example.project1729.domain.model.RabkinTest

object RABKIN_RESULTS {

    var RIGHT_ANSWERS = 0
    var WRONG_ANSWERS = 0
    var PROTANOPY_ANSWERS = 0
    var DEITERANOPY_ANSWERS = 0
    var SHOW_ONLY_FROM_BASE = false

    var USED_TESTS = mutableListOf<RabkinTest>()

    const val MAX_RIGHT_ANSWERS = 10
    const val MAX_WRONG_ANSWERS = 3

    var SHOW_QUESTION = true
}