package com.example.project1729.voiceAssistent

interface VoiceCommandHandler{
    fun updateLoginField(login: String)
    fun updatePasswordField(password: String)
    fun updateDiagnosisField(diagnosis: String)
    fun updateYearField(year: String)
    fun updateFioField(fio: String)
}
