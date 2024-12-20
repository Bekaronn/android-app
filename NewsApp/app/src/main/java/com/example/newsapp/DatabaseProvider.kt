import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    private var instance: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return instance ?: synchronized(this) {
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "news_database"
            ).build().also { instance = it }
        }
    }
}
