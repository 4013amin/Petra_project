import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.shop_app_project.data.models.product.ProductModel

class FavoritesViewModel : ViewModel() {

    private val _favorites = mutableStateListOf<ProductModel>()
    val favorites: List<ProductModel> get() = _favorites


    fun addFavorites(productModel: ProductModel) {
        if (!favorites.contains(productModel)) {
            _favorites.add(productModel)
        }
    }

    fun deleteFavorites(productModel: ProductModel) {
        _favorites.remove(productModel)
    }
}
