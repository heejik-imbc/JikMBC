package jik.imbc.data.repository

import jik.imbc.model.Content

interface ContentRepository {
    fun getEntertainmentContents(): Result<List<Content>>

    fun getDramaContents(): Result<List<Content>>

    fun getRelatedContents(contentId: Int): Result<List<Content>>

    fun getContentById(contentId: Int): Result<Content>

    fun leaveRating(contentId: Int, rating: Float): Result<Unit>
}
