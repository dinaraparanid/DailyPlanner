package com.paranid5.daily_planner.domain

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.paranid5.daily_planner.data.Release
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET

const val CURRENT_VERSION = "V0.1.0.1"

interface GitHubApi {
    @GET("repos/dinaraparanid/DailyPlanner/releases")
    suspend fun getReleases(): Response<List<Release>>
}

private suspend fun GitHubApi.getLatestReleaseAsync() = coroutineScope {
    async(Dispatchers.IO) { getReleases().body()?.first() }
}

internal suspend inline fun GitHubApi.checkForUpdatesAsync() = runCatching {
    getLatestReleaseAsync().await()?.takeIf { it.tagName > CURRENT_VERSION }
}.getOrNull()

inline val githubRetrofit
    get() = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(
            JacksonConverterFactory.create(
                ObjectMapper().configure(
                    DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,
                    false
                )
            )
        )
        .build()