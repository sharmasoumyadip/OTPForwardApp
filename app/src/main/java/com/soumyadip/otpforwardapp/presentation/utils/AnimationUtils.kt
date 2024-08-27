package com.soumyadip.otpforwardapp.presentation.utils

import android.view.View

fun viewPagerFlipTransformation(page: View, position: Float) {
    page.cameraDistance = 12000f
    when {
        position < -1 -> {
            page.alpha = 0f
        }

        position <= 0 -> {
            page.alpha = 1f
            page.pivotX = page.width * 0.5f
            page.pivotY = page.height * 0.5f
            page.rotationY = 180 * position
        }

        position <= 1 -> {
            page.alpha = 1f
            page.pivotX = page.width * 0.5f
            page.pivotY = page.height * 0.5f
            page.rotationY = 180 * position
        }

        else -> {
            page.alpha = 0f
        }
    }
}

fun viewPagerInDepthTransformation(page: View, position: Float) {
    when {
        position < -1 -> {
            page.alpha = 0f
        }

        position <= 0 -> {
            page.alpha = 1f
            page.translationX = 0f
            page.translationZ = 0f
            page.scaleX = 1f
            page.scaleY = 1f
        }

        position <= 1 -> {
            page.alpha = 1 - position
            page.translationX = page.width * -position
            page.translationZ = -1f
            val scaleFactor = 0.75f + (1 - 0.75f) * (1 - kotlin.math.abs(position))
            page.scaleX = scaleFactor
            page.scaleY = scaleFactor
        }

        else -> {
            page.alpha = 0f
        }
    }
}

fun viewZoomOutTransformation(page: View, position: Float) {
    val pageWidth = page.width
    val pageHeight = page.height
    when {
        position < -1 -> {
            page.alpha = 0f
        }

        position <= 1 -> {
            val scaleFactor = maxOf(0.85f, 1 - kotlin.math.abs(position))
            val vertMargin = pageHeight * (1 - scaleFactor) / 2
            val horzMargin = pageWidth * (1 - scaleFactor) / 2
            if (position < 0) {
                page.translationX = horzMargin - vertMargin / 2
            } else {
                page.translationX = -horzMargin + vertMargin / 2
            }
            page.scaleX = scaleFactor
            page.scaleY = scaleFactor
            page.alpha = 0.85f + (scaleFactor - 0.85f) / (1 - 0.85f) * (1 - 0.85f)
        }

        else -> {
            page.alpha = 0f
        }
    }
}


