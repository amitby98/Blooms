import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.blooms.R
import com.example.blooms.general.ErrorDialog
import com.example.blooms.mainApp.settings.settingsViewModel.QuoteState
import com.example.blooms.mainApp.settings.settingsViewModel.SettingsViewModel

class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var quoteTextView: AppCompatTextView
    private lateinit var authorTextView: AppCompatTextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        quoteTextView = view.findViewById(R.id.quoteTextView)
        authorTextView = view.findViewById(R.id.authorTextView)
        setupObservers()
        viewModel.fetchQuote()

        return view
    }

    private fun setupObservers() {
        viewModel.quoteState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is QuoteState.Loading -> {
                    quoteTextView.text = "Loading quote..."

                }
                is QuoteState.QuoteDataSuccess -> {
                    val quote = state.quote
                    quoteTextView.text = "\"${quote?.getQuoteText()}\""
                    authorTextView.text = "- ${quote?.getAuthorName()}"
                }
                is QuoteState.QuoteDataError -> {
                    val errorDialog = ErrorDialog(requireActivity())
                    errorDialog.show(
                        "Oops",
                        state.error?:"",
                        "close"
                    )
                }

                else -> {}
            }
        }
    }
}
