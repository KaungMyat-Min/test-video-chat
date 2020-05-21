package me.kaungmyatmin.videochat

import com.twilio.jwt.accesstoken.AccessToken

import com.twilio.jwt.accesstoken.VideoGrant


/**
 * Token generating MUST be on server side for security issues
 *
 */
class TokenGenerator {
    fun generateToken(identity: String, roomName: String): String {
        // Required for all types of tokens
        val twilioAccountSid = "ACc3a8aff1f95ab2d1d59c42ad1262e1fa"
        val twilioApiKey = "SK92338d75d6d3b180d74943d07c05af06"
        val twilioApiSecret = "XyIu4QUarY1UdYtI8qHrOvPRstFEcsE6"

        // Create Video grant
        val grant = VideoGrant().setRoom(roomName)

        // Create access token
        val token = AccessToken.Builder(
            twilioAccountSid,
            twilioApiKey,
            twilioApiSecret
        ).identity(identity).grant(grant).build()

        return token.toJwt()
    }
}