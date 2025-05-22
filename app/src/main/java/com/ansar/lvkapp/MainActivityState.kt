package com.ansar.lvkapp

data class MainActivityState(
    val images: List<ImageState>,
    val zIndex: Int,
) {
    companion object {
        val Default = MainActivityState(emptyList(),0)
    }
}