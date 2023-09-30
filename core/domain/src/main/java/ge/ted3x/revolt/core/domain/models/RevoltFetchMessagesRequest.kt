package ge.ted3x.revolt.core.domain.models

data class RevoltFetchMessagesRequest(
    val channelId: String,
    val limit: Int? = null,
    val before: String? = null,
    val after: String? = null,
    val sort: Sort = Sort.Latest,
    val nearby: String? = null,
    val includeUsers: Boolean? = null
) {

    enum class Sort {
        Relevance,
        Latest,
        Oldest
    }
}