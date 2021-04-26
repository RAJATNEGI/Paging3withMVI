package com.rajat.paging3withmvi.tmdb.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PopularMoviesResponse(
    @SerializedName("page")
    @Expose
    var page: Int? = null,

    @SerializedName("results")
    @Expose
    var results: List<Movie>? = null,

    @SerializedName("total_pages")
    @Expose
    var totalPages: Int? = null,

    @SerializedName("total_results")
    @Expose
    var totalResults: Int? = null
)