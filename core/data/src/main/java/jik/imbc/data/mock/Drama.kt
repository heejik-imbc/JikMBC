package jik.imbc.data.mock

import jik.imbc.model.Content


internal val MockDramas = listOf(
    Content(
        id = 1,
        title = "Romantic Saga",
        description = "A touching romantic drama that explores the complexities of love and relationships.",
        category = jik.imbc.model.ContentCategory.DRAMA,
        rating = "4.5",
        releaseDate = "2022-05-10",
    ),
    Content(
        id = 2,
        title = "Mystery of the Lost City",
        description = "An adventurous drama that unravels the secrets of an ancient lost city.",
        category = jik.imbc.model.ContentCategory.DRAMA,
        rating = "3.6",
        releaseDate = "2021-11-22",
    ),
    Content(
        id = 3,
        title = "Family Ties",
        description = "A heartfelt drama focusing on the dynamics and challenges within a family.",
        category = jik.imbc.model.ContentCategory.DRAMA,
        rating = "2.1",
        releaseDate = "2020-08-15",
    ),
)