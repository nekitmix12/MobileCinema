package com.example.mobilecinema.data.model.auth
import javax.mail.internet.AddressException
import javax.mail.internet.InternetAddress
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ProfileDTO(
    val id:	String,
    val nickName: String?,
    val email: String,
    val avatarLink:	String?,
    val name: String,
    val birthDate:	String,
    val gender: Int
)
