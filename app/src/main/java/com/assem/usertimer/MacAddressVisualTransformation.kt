package com.assem.usertimer

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class MacAddressVisualTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {

        val formattedText = text.text.formatMacAddress()
        val transformedText = AnnotatedString(formattedText)

        val offsetMapping = object : OffsetMapping {

            override fun originalToTransformed(offset: Int): Int {
                return formattedText.take(offset).length
            }

            override fun transformedToOriginal(offset: Int): Int {
                return text.text.take(offset).filter { !it.isWhitespace() }.length
            }
        }

        return TransformedText(transformedText, offsetMapping)
    }

}