package com.example.project1729.domain.repository

interface ThemeSwitcherRepository {
    fun switchTheme()
    fun getTheme(): Boolean
    fun setTheme()
}