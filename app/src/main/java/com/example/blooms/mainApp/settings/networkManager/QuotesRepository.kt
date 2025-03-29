import com.example.blooms.mainApp.settings.networkManager.QuotesApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class QuotesRepository(private val apiService: QuotesApiService) {
    suspend fun fetchQuote(apiKey: String, apiHost: String): Result<Quote?> =
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                val response = apiService.getQuote( apiKey, apiHost, token = "ipworld.info")
                response
            }
        }
}
