package com.example.mobilecinema.data.model.auth
import javax.mail.internet.AddressException
import javax.mail.internet.InternetAddress
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ProfileModel(
    val id:	String,
    val nickName: String?,
    val email: String,
    val avatarLink:	String?,
    val name: String,
    val birthDate:	Instant,
    val gender: Gender
) {
    init{
        require(try {
            UUID.fromString(id)
            true
        } catch (e: IllegalArgumentException) {
            false
        })

        require(try {
            InternetAddress(email).validate()
            true
        } catch (ex: AddressException) {
            false
        })
    }
}