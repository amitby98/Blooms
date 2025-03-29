package com.example.blooms.mainApp.settings.settingsViewModel

import Quote
import com.example.blooms.mainApp.profile.profileViewModel.ProfileState
import com.example.blooms.model.User


sealed class QuoteState {
    object Loading : QuoteState()
    data class QuoteDataSuccess(val quote: Quote?) : QuoteState()
    data class QuoteDataError(val error: String?) : QuoteState()
}