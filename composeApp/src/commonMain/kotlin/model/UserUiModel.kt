package model

data class UserUiModel(
    val userIdentification: String,
    val userAuthorization: String,
    val userLoggedIn: Boolean,
)