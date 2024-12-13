# NewsApp

NewsApp is an Android application that allows users to search for news articles from various sources using keywords. Users can filter and sort articles by date, relevance, popularity, country, language, and more. Future updates will include features such as saving articles to tabs and customizing news settings.

---

## Features
- Search for news articles using keywords.
- Sort articles by:
  - **Relevancy**: Articles closely related to the query appear first.
  - **Popularity**: Articles from popular sources and publishers appear first.
  - **Published Date**: Newest articles appear first (default).
- Filter articles by:
  - Country
  - Language
  - Date

---

## Project Structure
```
NewsApp
├── app
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── com.example.newsapp
│   │   │   │       ├── api
│   │   │   │       │   ├── NewsApi.kt
│   │   │   │       │   └── ApiSource.kt
│   │   │   │       ├── model
│   │   │   │       └── ui
│   │   │   └── res
├── build.gradle
└── README.md
```

---

## Setup Instructions
1. Clone the repository:
   ```bash
   git clone https://github.com/username/NewsApp.git
   cd NewsApp
   ```

2. Open the project in Android Studio.

3. Add your API key for [NewsAPI](https://newsapi.org/) in the `fetchNewsList` function in `NewsApi.kt`.
   ```kotlin
   @GET("v2/everything")
   fun fetchNewsList(
       @Query("q") query: String,
       @Query("apiKey") apiKey: String
   ): Call<NewsResponse>
   ```

4. Build and run the application.

---

## API Integration

### Endpoints Used
**Base URL:** `https://newsapi.org/`

#### Fetch News List
- **Endpoint:** `v2/everything`
- **Parameters:**
  - `q` (String): Query string to search articles.
  - `apiKey` (String): Your API key.
- **Response:**
  - JSON object containing news articles.

---

## Libraries Used
- [Retrofit](https://square.github.io/retrofit/): For networking.
- [Gson](https://github.com/google/gson): For JSON serialization/deserialization.
- [OkHttp](https://square.github.io/okhttp/): For HTTP client.
- [HttpLoggingInterceptor](https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor): For logging HTTP requests and responses.
- [Glide](https://github.com/bumptech/glide): For image loading and caching.
- [AndroidX ConstraintLayout](https://developer.android.com/reference/androidx/constraintlayout/widget/ConstraintLayout): For flexible UI layouts.
- [Material Components](https://material.io/components): For modern UI design.
- [Lifecycle ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel): For managing UI-related data lifecycle.

---

## Future Features
- Save articles to favorites or tabs.
- Additional filters for advanced search.
- Notification for trending news.
- User preferences for news settings.

---

## Prototype
### Single Page Design
A prototype of the current news search page includes:
- **Search bar** for inputting queries.
- **RecyclerView** to display articles.
- **Filters and Sort Options** at the top.

---

## Photos
![Main screen and search function](/assets/images/studio64_3xUn9vZiqA.png)
![second photo](/assets/images/studio64_ZxLsShlVQp.png)
