package com.example.calendar.presentation.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {
    private val _isSwitchOn: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isSwitchOn = _isSwitchOn.asStateFlow()

    private val _textPreference: MutableStateFlow<String> = MutableStateFlow("")
    var textPreference = _textPreference.asStateFlow()

    private val _intPreference: MutableStateFlow<Int> = MutableStateFlow(0)
    var intPreference = _intPreference.asStateFlow()

    fun toggleSwitch() {
        _isSwitchOn.value = _isSwitchOn.value.not()
//        Here we can save the value to the data store
//        dataStore.edit { preferences ->
//            preferences[IS_SWITCH_ON] = _isSwitchOn.value
//        }
    }

    fun saveText(finalText: String) {
        _textPreference.value = finalText
    }

    fun checkTextInput(text: String): Boolean {
        return text.isNotEmpty()
    }

    companion object {
        const val TAG = "SettingsViewModel"
    }
}