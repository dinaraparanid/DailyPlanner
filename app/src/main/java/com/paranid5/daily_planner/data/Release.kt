package com.paranid5.daily_planner.data

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonIgnoreProperties(ignoreUnknown = true)
data class Release(
    @JsonProperty("html_url") val htmlUrl: String,
    @JsonProperty("tag_name") val tagName: String,
    @JsonProperty("name") val name: String,
    @JsonProperty("body") val body: String
) : Parcelable
