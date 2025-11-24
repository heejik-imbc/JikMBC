package jik.imbc.data.repository

import jik.imbc.data.mock.MockDramas
import jik.imbc.data.mock.MockEntertainment
import jik.imbc.model.Content

class ContentRepositoryImpl : ContentRepository {
    override suspend fun getEntertainmentContents(): Result<List<Content>> {
        return Result.success(MockEntertainment)
    }

    override suspend fun getDramaContents(): Result<List<Content>> {
        return Result.success(MockDramas)
    }
}
