package com.example.project1729.domain

interface ThemeSwitcherRepository {
    fun switchTheme()
    fun getTheme(): Boolean
    fun setTheme()
}