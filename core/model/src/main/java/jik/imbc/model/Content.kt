package jik.imbc.model

data class Content(
    val id: Int,
    val title: String,
    val description: String,
    var thumbnailUrl: String = "",
    var posterUrl: String = "",
    val category: ContentCategory,
    val rating: String,
    val releaseDate: String,
    val ratingCount: Int = 0,
    var userRating: Float? = null
) {
    private val baseUrl = "https://image.tmdb.org/t/p/w500"
    val releaseYear: String
        get() = releaseDate.take(4)

    val getPosterUrl: String
        get() = "$baseUrl/$posterUrl"

    val getThumbnailUrl: String
        get() = "$baseUrl/$thumbnailUrl"


    companion object {
        val EMPTY = Content(
            id = 0,
            title = "",
            description = "",
            category = ContentCategory.ENTERTAINMENT,
            rating = "",
            releaseDate = "",
        )
    }
}

enum class ContentCategory {
    ENTERTAINMENT,
    DRAMA
}
