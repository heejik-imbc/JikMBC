package jik.imbc.data.repository

import jik.imbc.data.mock.MockDramas
import jik.imbc.data.mock.MockEntertainment
import jik.imbc.model.Content


private const val BASE_URL_THUMBNAIL = "https://image.tmdb.org/t/p/w500"

class ContentRepositoryImpl : ContentRepository {
    override fun getEntertainmentContents(): Result<List<Content>> {
        return Result.success(
            MockEntertainment.map {
                it.copy(
                    thumbnailUrl = "$BASE_URL_THUMBNAIL/l8pwO23MCvqYumzozpxynCNfck1.jpg",
                    posterUrl = "$BASE_URL_THUMBNAIL/si9tolnefLSUKaqQEGz1bWArOaL.jpg"
                )
            }
        )
    }

    override fun getDramaContents(): Result<List<Content>> {
        return Result.success(MockDramas.map {
            it.copy(
                thumbnailUrl = "$BASE_URL_THUMBNAIL/5h2EsPKNDdB3MAtOk9MB9Ycg9Rz.jpg",
                posterUrl = "$BASE_URL_THUMBNAIL/oJ7g2CifqpStmoYQyaLQgEU32qO.jpg"
            )
        })
    }

    override fun getContentById(contentId: Int): Result<Content> {
        val allContents =
            getEntertainmentContents().getOrNull().orEmpty() +
                    getDramaContents().getOrNull().orEmpty()

        val content = allContents.find { it.id == contentId }
            ?: throw IllegalArgumentException("Content not found")

        return Result.success(content)
    }
}
