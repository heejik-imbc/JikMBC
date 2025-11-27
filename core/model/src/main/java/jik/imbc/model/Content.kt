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
)

enum class ContentCategory {
    ENTERTAINMENT,
    DRAMA
}
