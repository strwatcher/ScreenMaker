package com.strwatcher.screenmaker.extentions

import javafx.embed.swing.SwingFXUtils
import javafx.scene.SnapshotParameters
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.WritableImage
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

fun Canvas.setImage(image: WritableImage, gc: GraphicsContext) {
    gc.clearRect(0.0, 0.0, width, height)
    width = image.width
    height = image.height
    gc.drawImage(image, 0.0, 0.0)
}

fun Canvas.getBufferedSnapshot(): BufferedImage {
    val parameters = SnapshotParameters()
    val image = WritableImage(width.toInt(), height.toInt())
    snapshot(parameters, image)
    return SwingFXUtils.fromFXImage(image, null)
}

fun Canvas.openImage(stage: Stage, gc: GraphicsContext) {
    val fileChooser = FileChooser()
    val extensionFilters = listOf(
        FileChooser.ExtensionFilter("PNG files (*.png)", "*.png"),
        FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg"),
        FileChooser.ExtensionFilter("JPEG files (*.jpeg)", "*.jpeg")
    )
    fileChooser.extensionFilters.addAll(extensionFilters)

    val file = fileChooser.showOpenDialog(stage)

    val image =  SwingFXUtils.toFXImage(ImageIO.read(file.inputStream()), null)

    setImage(image, gc)
}

fun Canvas.closeImage(gc:GraphicsContext) {
    gc.clearRect(0.0, 0.0, width, height)
    width = 0.0
    height = 0.0
}