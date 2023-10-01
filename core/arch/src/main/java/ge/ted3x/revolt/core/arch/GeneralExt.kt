package ge.ted3x.revolt.core.arch

fun notNullOrBlank(vararg args: String?): String? {
    return args.firstOrNull { it?.isNotBlank() == true }
}