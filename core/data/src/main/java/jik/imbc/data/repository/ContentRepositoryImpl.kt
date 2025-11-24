package jik.imbc.data.repository

import jik.imbc.data.mock.MockDramas
import jik.imbc.data.mock.MockEntertainment
import jik.imbc.model.Content


private const val BASE_URL_THUMBNAIL = "https://image.tmdb.org/t/p/w500"

class ContentRepositoryImpl : ContentRepository {
    override fun getEntertainmentContents(): Result<List<Content>> {
        return Result.success(MockEntertainment.map { it.copy(thumbnailUrl = "$BASE_URL_THUMBNAIL/rIwxNjhl0ndF0V5zrYGdwplAJAU.jpg") })
    }

    override fun getDramaContents(): Result<List<Content>> {
        return Result.success(MockDramas.map { it.copy(thumbnailUrl = "$BASE_URL_THUMBNAIL/uhXxW7EmzZIWFVpbF42r6L2g7Oh.jpg") })
    }
}
