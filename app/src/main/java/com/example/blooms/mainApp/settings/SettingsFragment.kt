package com.example.blooms.mainApp.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.blooms.R
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import java.io.IOException
import java.util.concurrent.TimeUnit

// Assuming one of these fields will contain the quote text
data class Quote(
    @SerializedName("q") val q: String? = null,
    @SerializedName("quote") val quote: String? = null,
    @SerializedName("text") val text: String? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("content") val content: String? = null,
    @SerializedName("author") val author: String? = null
) {
    // Get the quote regardless of which field contains it
    fun getQuoteText(): String {
        return q ?: quote ?: text ?: message ?: content ?: "No quote available"
    }

    // Get the author with a fallback
    fun getAuthorName(): String {
        return author ?: "Unknown Author"
    }
}

interface QuotesApiService {
    @GET("quote")
    suspend fun getQuote(
        @Query("token") token: String = "ipworld.info",
        @Header("X-RapidAPI-Key") apiKey: String,
        @Header("X-RapidAPI-Host") host: String
    ): Quote
}

class SettingsFragment : Fragment() {
    private var quoteTextView: TextView? = null
    private var authorTextView: TextView? = null
    private val TAG = "SettingsFragment"

    private val API_KEY = "969afc3218msh9e68b4938555c7bp1400c2jsnea1d1be89547"
    private val API_HOST = "quotes-inspirational-quotes-motivational-quotes.p.rapidapi.com"
    private val BASE_URL = "https://quotes-inspirational-quotes-motivational-quotes.p.rapidapi.com/"

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        quoteTextView = view.findViewById(R.id.quoteTextView)
        authorTextView = view.findViewById(R.id.authorTextView)

        quoteTextView?.text = getString(R.string.loading_quote)

        // Use both approaches - Retrofit and OkHttp as fallback
        fetchQuoteWithRetrofit()

        return view
    }

    private fun fetchQuoteWithRetrofit() {
        Log.d(TAG, "Fetching quote with Retrofit...")

        // Create OkHttpClient with longer timeouts
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(QuotesApiService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val quote = apiService.getQuote(
                    apiKey = API_KEY,
                    host = API_HOST
                )

                Log.d(TAG, "Quote received via Retrofit: ${quote.q}, ${quote.quote}, ${quote.text}, ${quote.author}")

                withContext(Dispatchers.Main) {
                    val quoteText = quote.getQuoteText()
                    val author = quote.getAuthorName()

                    if (quoteText == "No quote available") {
                        // Retrofit approach failed, try with OkHttp
                        fetchQuoteWithOkHttp()
                    } else {
                        displayQuote(quoteText, author)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching quote with Retrofit: ${e.message}", e)

                // Try the OkHttp approach as fallback
                withContext(Dispatchers.Main) {
                    fetchQuoteWithOkHttp()
                }
            }
        }
    }

    private fun fetchQuoteWithOkHttp() {
        Log.d(TAG, "Fetching quote with OkHttp...")

        CoroutineScope(Dispatchers.IO).launch {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url("${BASE_URL}quote?token=ipworld.info")
                .get()
                .addHeader("X-RapidAPI-Key", API_KEY)
                .addHeader("X-RapidAPI-Host", API_HOST)
                .build()

            try {
                val response: Response = client.newCall(request).execute()
                val responseBody = response.body()?.string()

                Log.d(TAG, "Raw OkHttp response: $responseBody")

                if (responseBody != null) {
                    // Parse the JSON manually to be more flexible
                    val jsonObject = Gson().fromJson(responseBody, JsonObject::class.java)

                    // Try to find the quote in various possible fields
                    var quoteText: String? = null
                    var author: String? = null

                    if (jsonObject.has("q")) {
                        quoteText = jsonObject.get("q").asString
                    } else if (jsonObject.has("quote")) {
                        quoteText = jsonObject.get("quote").asString
                    } else if (jsonObject.has("text")) {
                        quoteText = jsonObject.get("text").asString
                    } else if (jsonObject.has("message")) {
                        quoteText = jsonObject.get("message").asString
                    } else if (jsonObject.has("content")) {
                        quoteText = jsonObject.get("content").asString
                    }

                    if (jsonObject.has("author")) {
                        author = jsonObject.get("author").asString
                    }

                    withContext(Dispatchers.Main) {
                        if (quoteText != null) {
                            displayQuote(quoteText, author ?: "Unknown Author")
                        } else {
                            showError("ניתן לנסות שוב מאוחר יותר")
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        showError("אין תגובה מהשרת")
                    }
                }
            } catch (e: IOException) {
                Log.e(TAG, "Error fetching quote with OkHttp: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    showError("שגיאת חיבור: ${e.message}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected error: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    showError("שגיאה לא צפויה: ${e.message}")
                }
            }
        }
    }

    private fun displayQuote(quoteText: String, author: String) {
        quoteTextView?.text = "\"$quoteText\""
        authorTextView?.text = "- $author"
    }

    private fun showError(message: String) {
        Toast.makeText(context, "שגיאה: $message", Toast.LENGTH_SHORT).show()
        quoteTextView?.text = getString(R.string.error_loading_quote)
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}