import androidx.room.*
import com.example.newsapp.model.Article

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: Article)

    @Query("SELECT * FROM articles")
    suspend fun getAllArticles(): List<Article>

    @Delete
    suspend fun deleteArticle(article: Article)
}
