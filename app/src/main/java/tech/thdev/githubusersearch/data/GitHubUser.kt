package tech.thdev.githubusersearch.data

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["login"])
data class GitHubUser(
    @field:SerializedName("login") val login: String,
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("avatar_url") val avatarUrl: String,
    @field:SerializedName("score") val score: Double,
) {

    var isLike: Boolean = false
}