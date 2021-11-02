package com.strwatcher.screenmaker.extentions

import javafx.geometry.Rectangle2D
import javafx.scene.image.WritableImage
import javafx.scene.image.WritablePixelFormat

fun WritableImage.crop(newRect: Rectangle2D): WritableImage {
    val croppedImage = WritableImage(newRect.width.toInt(), newRect.height.toInt())

    val reader = this.pixelReader
    val writer = croppedImage.pixelWriter
    val buffer = ByteArray(newRect.width.toInt() * newRect.height.toInt() * 4)
    val format = WritablePixelFormat.getByteBgraInstance()

    reader.getPixels(
        newRect.minX.toInt(), newRect.minY.toInt(),
        newRect.width.toInt(), newRect.height.toInt(),
        format, buffer, 0, newRect.width.toInt() * 4
    )

    writer.setPixels(
        0, 0,
        newRect.width.toInt(), newRect.height.toInt(),
        format, buffer, 0, newRect.width.toInt() * 4
    )

    return croppedImage
}