package com.dicoding.mystoryapp.utils

import com.dicoding.mystoryapp.api.response.ListStoryItem

object DataDummy {
    fun generateDummyStoryList(): List<ListStoryItem> {
        return listOf(
            ListStoryItem(
                photoUrl = "https://example.com/photo1.jpg",
                createdAt = "2024-05-28T10:00:00Z",
                name = "John Doe",
                description = "A beautiful sunrise",
                lon = 123.456,
                id = "1",
                lat = -23.456
            ),
            ListStoryItem(
                photoUrl = "https://example.com/photo2.jpg",
                createdAt = "2024-05-27T09:30:00Z",
                name = "Jane Smith",
                description = "Exploring the mountains",
                lon = 124.456,
                id = "2",
                lat = -24.456
            ),
            ListStoryItem(
                photoUrl = "https://example.com/photo3.jpg",
                createdAt = "2024-05-26T08:15:00Z",
                name = "Alice Johnson",
                description = "A day at the beach",
                lon = 125.456,
                id = "3",
                lat = -25.456
            )
        )
    }
}
