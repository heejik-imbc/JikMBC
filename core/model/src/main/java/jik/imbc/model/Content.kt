package jik.imbc.model

data class Content(
    val id: String,
    val title: String,
    val description: String,
    val thumbnailUrl: String,
    val category: ContentCategory,
    val rating: String,
    val releaseDate: String,
)

enum class ContentCategory {
    ENTERTAINMENT,
    DRAMA
}
