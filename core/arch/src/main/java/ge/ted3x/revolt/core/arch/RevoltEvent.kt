package ge.ted3x.revolt.core.arch

interface RevoltEvent

data class OpenProfileEvent(val userId: String): RevoltEvent