package com.ansar.lvkapp

import com.ansar.lvkapp.base.utils.BaseScreenModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce

class MainActivityScreenModel :
    BaseScreenModel<MainActivityState, MainActivityEvent>(MainActivityState.Default) {
    init {
        state()
    }

    private fun state() = intent {
        reduce {
            state.copy(
                images = listOf(
                    ImageState(id = 1, R.drawable.landscape2),
                    ImageState(id = 2, R.drawable.landscape3)
                )
            )
        }
        postSideEffectLocal(MainActivityEvent.Success)
    }

    fun changeImages(imageState: ImageState, z: Boolean) = intent {

        if (z || state.zIndex == 0) {
            val list = state.images.filter { it.id != imageState.id } + listOf(imageState)
            reduce {
                state.copy(
                    images = list,
                    zIndex = imageState.id,
                )
            }
        } else {
            reduce {
                state.copy(
                    images = state.images.map {
                        if (it.id == imageState.id) it.copy(
                            offset = imageState.offset,
                            width = imageState.width,
                            height = imageState.height
                        ) else it
                    }
                )
            }
        }
    }


    fun changeImages(imageState: ImageState, z: Boolean, id: Int) = intent {

        if (z || state.zIndex == 0) {
            val list = state.images.filter { it.id != imageState.id } + listOf(imageState)
            reduce {
                state.copy(
//                    images = list,
                    zIndex = imageState.id,
                    selectedId = id
                )
            }
        } else {
            reduce {
                state.copy(
                    selectedId = id,
                    images = state.images.map {
                        if (it.id == imageState.id) it.copy(
                            offset = imageState.offset,
                            width = imageState.width,
                            height = imageState.height
                        ) else it
                    }
                )
            }
        }

    }
}