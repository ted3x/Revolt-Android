package ge.ted3x.revolt.core.domain.models.user

enum class RevoltRelationshipStatus {
    None,
    User,
    Friend,
    Outgoing,
    Incoming,
    Blocked,
    BlockedOther
}