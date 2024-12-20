import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newsapp.model.Source

@Entity(tableName = "articles")
data class Article(
    @PrimaryKey(autoGenerate = false) val id: String,
    val source: Source,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?,
    val language: String?,
    val sourceCountry: String?
)
