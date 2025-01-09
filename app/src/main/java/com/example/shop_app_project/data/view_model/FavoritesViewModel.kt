import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shop_app_project.Home_page.Main.SharedPreferencesManager.SharedPreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.shop_app_project.data.models.product.ProductModel

class FavoritesViewModel : ViewModel() {
    private val _favorites = MutableStateFlow<List<ProductModel>>(emptyList())
    val favorites: StateFlow<List<ProductModel>> = _favorites

    fun addFavorite(product: ProductModel) {
        _favorites.value = _favorites.value + product
    }

    fun removeFavorite(product: ProductModel) {
        _favorites.value = _favorites.value.filter { it.id != product.id }
    }
}

