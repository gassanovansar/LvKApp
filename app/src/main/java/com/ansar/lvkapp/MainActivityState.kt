package com.ansar.lvkapp

data class MainActivityState(
    val images: List<ImageState>,
    val zIndex: Int,
    val selectedId: Int
) {
    companion object {
        val Default = MainActivityState(emptyList(),0, -1)
    }
}