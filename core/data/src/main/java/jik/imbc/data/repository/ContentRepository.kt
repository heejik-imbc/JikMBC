package jik.imbc.data.repository

import jik.imbc.model.Content

interface ContentRepository {
    suspend fun getEntertainmentContents(): Result<List<Content>>

    suspend fun getDramaContents(): Result<List<Content>>
}
