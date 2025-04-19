package com.ansar.lvkapp.uiKit.designe

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ansar.lvkapp.base.ext.CardDateMaskLength
import com.ansar.lvkapp.base.ext.CardMaskLength
import com.ansar.lvkapp.base.ext.PhoneLength
import com.ansar.lvkapp.base.ext.cardDateMask
import com.ansar.lvkapp.base.ext.cardMask
import com.ansar.lvkapp.base.ext.clickableRound
import com.ansar.lvkapp.base.ext.phoneMask
import com.ansar.lvkapp.uiKit.theme.AppTheme

enum class AppTextFiledType {
    TEXT, PHONE, PASSWORD, NUMBER, CARD, CARD_DATE
}

@Composable
internal fun AppTextField(
    modifier: Modifier = Modifier,
    hint: String = "",
    backgroundColor: Color = AppTheme.colors.white,
    textColor: Color = AppTheme.colors.black,
    type: AppTextFiledType = AppTextFiledType.TEXT,
    fill: Boolean = true,
    value: String,
    enabled: Boolean = true,
    error: Boolean = false,
    errorText: String? = null,
    paddingStart: Dp = 16.dp,
    paddingEnd: Dp = 16.dp,
    left: @Composable (BoxScope.() -> Unit)? = null,
    right: @Composable (BoxScope.() -> Unit)? = null,
    minLines: Int = 1,
    maxLength: Int = Int.MAX_VALUE,
    minHeight: Dp = 48.dp,
    maxHeight: Dp? = null,
    shape: Shape = RoundedCornerShape(10.dp),
    onClick: () -> Unit = {},
    onValueChange: (String) -> Unit,
) {
    val source = remember {
        MutableInteractionSource()
    }
    var visualTransformation by remember { mutableStateOf(false) }
    var _value by remember { mutableStateOf(value) }
    LaunchedEffect(value) {
        _value = value
    }
    var isFocused by remember { mutableStateOf(false) }

    var size by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    Column {
        if (maxHeight != null) {
            modifier.height(maxHeight)
        }
        Card(
            shape = shape,
            elevation = 0.dp,
            modifier = modifier.defaultMinSize(minHeight = minHeight).clickableRound(8.dp) {
                onClick()
            }.onSizeChanged {
                with(density) {
                    size = it.height.toDp()
                }
            },
            backgroundColor = backgroundColor,
        ) {
            if (source.collectIsPressedAsState().value) {
                onClick()
            }
            Row(modifier = Modifier.padding(start = paddingStart, end = paddingEnd)) {
                val keyboardController = LocalSoftwareKeyboardController.current
                Box(modifier = Modifier.height(size)) {
                    left?.invoke(this)
                }
                BasicTextField(
                    interactionSource = source,
                    enabled = enabled,
                    visualTransformation = when (type) {
                        AppTextFiledType.TEXT -> VisualTransformation.None
                        AppTextFiledType.PHONE -> phoneMask()
                        AppTextFiledType.PASSWORD -> if (visualTransformation) VisualTransformation.None else PasswordVisualTransformation()
                        AppTextFiledType.NUMBER -> VisualTransformation.None
                        AppTextFiledType.CARD -> cardMask()
                        AppTextFiledType.CARD_DATE -> cardDateMask()
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = when (type) {
                            AppTextFiledType.TEXT -> KeyboardType.Text
                            AppTextFiledType.PHONE -> KeyboardType.Number
                            AppTextFiledType.PASSWORD -> KeyboardType.Text
                            AppTextFiledType.NUMBER -> KeyboardType.Number
                            AppTextFiledType.CARD -> KeyboardType.Number
                            AppTextFiledType.CARD_DATE -> KeyboardType.Number
                        },
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        }),
                    value = _value,
                    onValueChange = {
                        val maxLength = when (type) {
                            AppTextFiledType.PHONE -> PhoneLength
                            AppTextFiledType.CARD -> CardMaskLength
                            AppTextFiledType.CARD_DATE -> CardDateMaskLength
                            else -> maxLength
                        }

                        if (it.length <= maxLength) {
                            onValueChange(it)
                            _value = it
                        }

                    },
                    minLines = minLines,
                    singleLine = minLines == 1,
                    modifier = Modifier.padding(vertical = 10.dp)
                        .onFocusChanged {
                            isFocused = it.isFocused
                        }.align(Alignment.CenterVertically)
                        .wrapContentSize(Alignment.Center)
                        .fillMaxWidth().padding(start = 4.dp).weight(1f, fill),
                    textStyle = AppTheme.typography.regular.copy(
                        fontSize = 16.sp,
                        color = if (error) Color.Red else textColor
                    ),
                    decorationBox = { innerTextField ->
                        if (_value.isEmpty() && hint.isNotEmpty()) {
                            Text(
                                text = hint,
                                style = AppTheme.typography.regular.copy(
                                    fontSize = 16.sp,
                                    color = AppTheme.colors.gray,
                                ),
                                maxLines = 1
                            )
                        }
                        innerTextField()
                    },
                    cursorBrush = SolidColor(AppTheme.colors.black),
                )
                if (type == AppTextFiledType.PASSWORD) {
                    Box(modifier = Modifier.height(size)) {
//                        Image(
//                            painter = if (!visualTransformation) AppResource.image.passwordoff.painterResource() else AppResource.image.passwordon.painterResource(),
//                            contentDescription = null,
//                            modifier = Modifier.padding(start = 10.dp).align(
//                                Alignment.Center
//                            ).clickableRound(8.dp) {
//                                visualTransformation = !visualTransformation
//                            },
//                        )
                    }


                } else
                    Box(modifier = Modifier.height(size)) {
                        right?.invoke(this)
                    }


            }
        }
        AnimatedVisibility(error && errorText != null) {
            Text(
                modifier = Modifier.padding(top = 5.dp),
                text = errorText.toString(),
                style = AppTheme.typography.regular.copy(
                    fontSize = 12.sp,
                    lineHeight = 12.sp,
                    color = Color.Red,
                )
            )
        }
    }
}

@Composable
internal fun TitleAppTextField(
    title: String,
    endTitle: String? = null,
    endTitleOnClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    hint: String = "",
    backgroundColor: Color = Color.Transparent,
    textColor: Color = AppTheme.colors.black,
    type: AppTextFiledType = AppTextFiledType.TEXT,
    fill: Boolean = true,
    value: String,
    enabled: Boolean = true,
    error: Boolean = false,
    errorText: String? = null,
    left: @Composable (BoxScope.() -> Unit)? = null,
    right: @Composable (BoxScope.() -> Unit)? = null,
    minLines: Int = 1,
    maxLength: Int = Int.MAX_VALUE,
    minHeight: Dp = 48.dp,
    maxHeight: Dp? = null,
    shape: Shape = RoundedCornerShape(10.dp),
    onClick: () -> Unit = {},
    onValueChange: (String) -> Unit,
) {
    Column(modifier) {
        Row {
            Text(
                modifier = Modifier.weight(1f),
                text = title,
                style = AppTheme.typography.medium.copy(
                    fontSize = 14.sp,
                    color = AppTheme.colors.black,
                )
            )
            if (endTitle != null) {
                Text(
                    modifier = Modifier.clickable {
                        endTitleOnClick()
                    },
                    text = endTitle,
                    style = AppTheme.typography.medium.copy(
                        fontSize = 14.sp,
                        color = AppTheme.colors.black,
                    )
                )
            }

        }

        AppTextField(
            modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
            hint = hint,
            backgroundColor = backgroundColor,
            textColor = textColor,
            type = type,
            fill = fill,
            value = value,
            enabled = enabled,
            error = error,
            errorText = errorText,
            left = left,
            right = right,
            minLines = minLines,
            maxLength = maxLength,
            minHeight = minHeight,
            maxHeight = maxHeight,
            shape = shape,
            onClick = onClick,
            onValueChange = onValueChange
        )
    }
}