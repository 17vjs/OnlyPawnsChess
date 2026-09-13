import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.onlypawnchess.GameViewModel
import com.example.onlypawnchess.data.repository.GameRepositoryInterface

class GameViewModelFactory(
    private val repository: GameRepositoryInterface
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (GameViewModel::class.java.isAssignableFrom(modelClass)) {
            @Suppress("UNCHECKED_CAST")
            return GameViewModel(repository) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}