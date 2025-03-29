import com.google.gson.annotations.SerializedName

data class Quote(
    @SerializedName("q") val q: String? = null,
    @SerializedName("quote") val quote: String? = null,
    @SerializedName("text") val text: String? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("content") val content: String? = null,
    @SerializedName("author") val author: String? = null
) {
    fun getQuoteText(): String {
        return q ?: quote ?: text ?: message ?: content ?: "No quote available"
    }

    fun getAuthorName(): String {
        return author ?: "Unknown Author"
    }
}
