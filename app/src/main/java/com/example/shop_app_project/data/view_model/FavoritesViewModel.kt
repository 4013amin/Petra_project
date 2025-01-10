import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shop_app_project.Home_page.Main.SharedPreferencesManager.SharedPreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.shop_app_project.data.models.product.ProductModel

data class FavoriteModel(
    val id: Int,
    val product: ProductModel,
    val created_at: String
)

data class SimpleResponse(
    val message: String
)

