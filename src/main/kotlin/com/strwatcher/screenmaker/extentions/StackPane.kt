package com.strwatcher.screenmaker.extentions

import javafx.embed.swing.SwingFXUtils
import javafx.scene.SnapshotParameters
import javafx.scene.canvas.Canvas
import javafx.scene.image.WritableImage
import javafx.scene.layout.StackPane
import java.awt.image.BufferedImage

fun StackPane.getBufferedSnapshot(): BufferedImage {
    val parameters = SnapshotParameters()
    val image = WritableImage(width.toInt(), height.toInt())
    snapshot(parameters, image)
    return SwingFXUtils.fromFXImage(image, null)
}