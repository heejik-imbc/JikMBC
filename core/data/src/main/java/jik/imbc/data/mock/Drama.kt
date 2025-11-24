package jik.imbc.data.mock

import jik.imbc.model.Content


internal val MockDramas = listOf(
    Content(
        id = "drama1",
        title = "Romantic Saga",
        description = "A touching romantic drama that explores the complexities of love and relationships.",
        thumbnailUrl = "https://example.com/thumbnails/romantic_saga.jpg",
        category = jik.imbc.model.ContentCategory.DRAMA,
        rating = "4.5",
        releaseDate = "2022-05-10",
    ),
    Content(
        id = "drama2",
        title = "Mystery of the Lost City",
        description = "An adventurous drama that unravels the secrets of an ancient lost city.",
        thumbnailUrl = "https://example.com/thumbnails/lost_city.jpg",
        category = jik.imbc.model.ContentCategory.DRAMA,
        rating = "3.6",
        releaseDate = "2021-11-22",
    ),
    Content(
        id = "drama3",
        title = "Family Ties",
        description = "A heartfelt drama focusing on the dynamics and challenges within a family.",
        thumbnailUrl = "https://example.com/thumbnails/family_ties.jpg",
        category = jik.imbc.model.ContentCategory.DRAMA,
        rating = "2.1",
        releaseDate = "2020-08-15",
    ),
)