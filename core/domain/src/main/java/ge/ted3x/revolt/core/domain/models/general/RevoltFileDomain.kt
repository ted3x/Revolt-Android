package ge.ted3x.revolt.core.domain.models.general

enum class RevoltFileDomain(val path: String) {
    Avatar("/avatars/"),
    Background("/backgrounds/"),
    Attachments("/attachments/"),
    Icons("/icons/"),
    Banners("/banners/");

    fun withoutLastSlash() = this.path.dropLast(1)
}