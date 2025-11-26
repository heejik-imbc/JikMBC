package jik.imbc.ui.transition

data class ContentCardSharedElementKey(
    val contentId: Int,
    val type: ContentCardElementOrigin
)

enum class ContentCardElementOrigin {
    LIST_CARD,
    MAIN_CARD,
}