package com.example.blooms.mainApp.settings.settingsViewModel

import QuotesRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blooms.mainApp.settings.networkManager.NetworkManager
import kotlinx.coroutines.launch

private val API_KEY = "969afc3218msh9e68b4938555c7bp1400c2jsnea1d1be89547"
private val API_HOST = "quotes-inspirational-quotes-motivational-quotes.p.rapidapi.com"

class SettingsViewModel() : ViewModel() {
    private val repository = QuotesRepository(NetworkManager.apiService)

    private val _quoteState = MutableLiveData<QuoteState>()
    val quoteState: LiveData<QuoteState> = _quoteState

    fun fetchQuote() {
        _quoteState.value = QuoteState.Loading
        viewModelScope.launch {
            repository.fetchQuote(API_KEY, API_HOST)
                .onSuccess { quote ->
                    _quoteState.value = QuoteState.QuoteDataSuccess(quote = quote)
                }
                .onFailure { exception ->
                    _quoteState.value = QuoteState.QuoteDataError(error = exception.message)
                }
        }
    }
}
