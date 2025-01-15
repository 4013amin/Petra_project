import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shop_app_project.Home_page.Main.SharedPreferencesManager.SharedPreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.shop_app_project.data.models.product.ProductModel

class SavedProductsViewModel : ViewModel() {
    private val _savedProducts = mutableStateListOf<ProductModel>()
    val savedProducts: List<ProductModel> get() = _savedProducts

    fun saveProduct(product: ProductModel) {
        if (!_savedProducts.contains(product)) {
            _savedProducts.add(product)
        }
    }
}


