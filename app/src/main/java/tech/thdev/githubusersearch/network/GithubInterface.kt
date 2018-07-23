package tech.thdev.githubusersearch.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import tech.thdev.githubusersearch.data.GithubResponse


interface GithubInterface {

    /**
     * Github user search api - https://developer.github.com/v3/search/#search-users
     * Find users via various criteria. This method returns up to 100 results per page.
     *
     * @param q     String : Required. The search terms.
     * @param sort  String : The sort field. Can be followers, repositories, or joined. Default: results are sorted by best match.
     * @param order String : The sort order if sort parameter is provided. One of asc or desc. Default: desc
     *
     * Example api - curl https://api.github.com/search/users?q=tom+repos:%3E42+followers:%3E1000
     */
    @GET("/search/users?")
    fun searchUser(
            @Query(value = "q", encoded = true) userKeyword: String,
            @Query("page") page: Int,
            @Query("per_page") perPage: Int): Single<GithubResponse>
}