package jik.imbc.data.mock

import jik.imbc.model.Content

internal val MockEntertainment = listOf(
    Content(
        id = 1,
        title = "Funny Moments Compilation",
        description = "A compilation of the funniest moments from various shows.",
        thumbnailUrl = "https://example.com/thumbnails/funny_moments.jpg",
        category = jik.imbc.model.ContentCategory.ENTERTAINMENT,
        rating = "3.2",
        releaseDate = "2022-05-10",
    ),
    Content(
        id = 2,
        title = "Top 10 Viral Videos",
        description = "A countdown of the top 10 viral videos that took the internet by storm.",
        thumbnailUrl = "https://example.com/thumbnails/viral_videos.jpg",
        category = jik.imbc.model.ContentCategory.ENTERTAINMENT,
        rating = "4.8",
        releaseDate = "2021-11-22",
    ),
    Content(
        id = 3,
        title = "Celebrity Interviews",
        description = "Exclusive interviews with your favorite celebrities.",
        thumbnailUrl = "https://example.com/thumbnails/celebrity_interviews.jpg",
        category = jik.imbc.model.ContentCategory.ENTERTAINMENT,
        rating = "2.7",
        releaseDate = "2020-08-15",
    ),
)