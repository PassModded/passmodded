package proton.android.pass.data.impl.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetShareKeysResponse(
    @SerialName("ShareKeys")
    val keys: ShareKeysListResponse
)

@Serializable
data class ShareKeysListResponse(
    @SerialName("Keys")
    val keys: List<ShareKeyResponse>,
    @SerialName("Total")
    val total: Long
)

@Serializable
data class ShareKeyResponse(
    @SerialName("KeyRotation")
    val keyRotation: Long,
    @SerialName("UserKeyID")
    val userKeyId: String,
    @SerialName("Key")
    val key: String,
    @SerialName("CreateTime")
    val createTime: Long
)
