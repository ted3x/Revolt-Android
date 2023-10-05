package ge.ted3x.revolt.core.designsystem.text

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import java.util.UUID

@Composable
fun SpannedText(
    text: String,
    spannedText: String,
    annotation: String = "",
    onClick: () -> Unit
) {
    val tag = UUID.randomUUID().toString()
    val annotatedString = buildAnnotatedString {
        append(text)

        pushStringAnnotation(tag = tag, annotation = annotation)
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
            append(spannedText)
        }
        pop()
    }
    ClickableText(text = annotatedString) { offset ->
        annotatedString.getStringAnnotations(tag = tag, start = offset, end = offset)
            .firstOrNull()?.let {
            onClick.invoke()
        }
    }
}