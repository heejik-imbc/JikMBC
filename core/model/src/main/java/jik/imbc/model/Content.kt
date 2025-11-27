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
)

enum class ContentCategory {
    ENTERTAINMENT,
    DRAMA
}
