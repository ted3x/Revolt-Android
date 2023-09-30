package ge.ted3x.revolt.core.domain.core

enum class RevoltFileDomain(val path: String) {
    Avatar("/avatars/"),
    Background("/backgrounds/"),
    Attachments("/attachments/");

    fun withoutLastSlash() = this.path.dropLast(1)
}