package com.ansar.lvkapp.uiKit.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.PlatformParagraphStyle
import androidx.compose.ui.text.PlatformSpanStyle
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.LineHeightStyle
import com.ansar.lvkapp.R
import com.ansar.lvkapp.uiKit.theme.typography.BaseTypography
import com.ansar.lvkapp.uiKit.theme.typography.TextStyleBold
import com.ansar.lvkapp.uiKit.theme.typography.TextStyleMedium
import com.ansar.lvkapp.uiKit.theme.typography.TextStyleRegular
import com.ansar.lvkapp.uiKit.theme.typography.TextStyleSemiBold

object AppFonts {
    //    actual val small: BaseTypography = TextStyleRegular(FontFamily(Font(R.font.inter_400)))
    val regular: BaseTypography = TextStyleRegular(FontFamily(Font(R.font.font_400)))
    val medium: BaseTypography = TextStyleMedium(FontFamily(Font(R.font.font_500)))
    val semiBold: BaseTypography = TextStyleSemiBold(FontFamily(Font(R.font.font_600)))
    val bold: BaseTypography = TextStyleBold(FontFamily(Font(R.font.font_700)))
//    actual val black: BaseTypography = TextStyleBlack(FontFamily(Font(R.font.font_900)))
}


class AppTypography(
    val regular: TextStyle,
    val medium: TextStyle,
    val semiBold: TextStyle,
    val bold: TextStyle,
//    val black: TextStyle,
)


@Composable
fun textStyles(): AppTypography {
    return AppTypography(
        regular = AppFonts.regular.getComposeTextStyle(),
        medium = AppFonts.medium.getComposeTextStyle(),
        semiBold = AppFonts.semiBold.getComposeTextStyle(),
        bold = AppFonts.bold.getComposeTextStyle(),
//        black = AppFonts.black.getComposeTextStyle(),
    )
}

private fun toTextStyle(typographyStyle: BaseTypography): TextStyle {
    return TextStyle(
        fontSize = typographyStyle.fontSize,
        fontFamily = typographyStyle.fontFamily,
        lineHeight = typographyStyle.lineHeight,
        fontWeight = typographyStyle.fontWeight,
        platformStyle = PlatformTextStyle(
            PlatformSpanStyle.Default,
            PlatformParagraphStyle.Default
        ),
        baselineShift = BaselineShift(typographyStyle.baselineShift),
        lineHeightStyle = LineHeightStyle(
            LineHeightStyle.Alignment.Center,
            LineHeightStyle.Trim.None
        )
    )
}

fun BaseTypography.getComposeTextStyle(): TextStyle {
    return toTextStyle(this)
}

internal val LocalTypography = compositionLocalOf<AppTypography> {
    error(
        "No typography provided! Make sure to wrap all usages of components in a " +
                "AppTheme."
    )
}
