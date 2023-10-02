package ge.ted3x.revolt.core.arch

fun notNullOrBlank(vararg args: String?, onNull: () -> String = { args.last()!! }): String {
    return args.first { it?.isNotBlank() == true } ?: onNull.invoke()
}

fun firstNotNullAndBlankOrNull(vararg args: String?): String? {
    return args.first { it?.isNotBlank() == true }
}