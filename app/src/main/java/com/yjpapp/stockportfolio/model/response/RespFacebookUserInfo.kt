package com.yjpapp.stockportfolio.model.response

import kotlinx.serialization.Serializable

@Serializable
data class RespFacebookUserInfo (
    var name: String = "",
    var email: String = "",
    var id: String = ""
)