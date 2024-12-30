import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shop_app_project.data.api.API
import com.example.shop_app_project.data.models.product.ProductModel
import com.example.shop_app_project.data.utils.UtilsRetrofit
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response

class FavoritesViewModel : ViewModel() {

    private val _favorites = mutableStateListOf<ProductModel>()
    val favorites: List<ProductModel> get() = _favorites

    private val api: API = UtilsRetrofit.api

    fun addFavorite(product: ProductModel) {
        viewModelScope.launch {
            val response = api.addFavorite(product.id)
            if (response.isSuccessful) {
                _favorites.add(product)
            } else {
                println("Failed to add favorite: ${response.errorBody()?.string()}")
            }
        }
    }


    fun removeFavorite(product: ProductModel) {
        viewModelScope.launch {
            val response = api.removeFavorite(product.id)
            if (response.isSuccessful) {
                _favorites.remove(product)
            } else {
                println("Failed to remove favorite: ${response.errorBody()?.string()}")
            }
        }
    }

    fun loadFavorites() {
        viewModelScope.launch {
            val response = api.getFavorites()
            if (response.isSuccessful) {
                response.body()?.let { products ->
                    _favorites.clear()
                    _favorites.addAll(products)
                }
            } else {
                println("Failed to load favorites: ${response.errorBody()?.string()}")
            }
        }
    }
}
