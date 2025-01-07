import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.shop_app_project.data.models.product.ProductModel

class FavoritesViewModel : ViewModel() {
    private val _favorites = mutableStateListOf<ProductModel>()
    val favorites: List<ProductModel> get() = _favorites

    fun addFavorite(product: ProductModel) {
        if (!_favorites.contains(product)) {
            _favorites.add(product)
        }
    }

    fun removeFavorite(product: ProductModel) {
        _favorites.remove(product)
    }
}
