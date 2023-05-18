package proton.pass.domain

data class Plan(
    val planType: PlanType,
    val hideUpgrade: Boolean,
    val vaultLimit: Int,
    val aliasLimit: Int,
    val totpLimit: Int,
    val updatedAt: Long
)

sealed interface PlanType {

    fun humanReadableName(): String
    fun internalName(): String

    object Free : PlanType {
        override fun humanReadableName(): String = "Proton Free"
        override fun internalName(): String = "free"
    }

    object Trial : PlanType {
        override fun humanReadableName(): String = "Trial"
        override fun internalName(): String = "trial"
    }

    data class Paid(val internal: String, val humanReadable: String) : PlanType {
        override fun humanReadableName(): String = humanReadable
        override fun internalName(): String = internal
    }
}
