package com.strwatcher.screenmaker.extentions

import javafx.embed.swing.SwingFXUtils
import javafx.scene.SnapshotParameters
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.ImageView
import javafx.scene.image.WritableImage
import javafx.scene.layout.StackPane
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

fun Canvas.setImage(image: WritableImage, gc: GraphicsContext, imageView: ImageView, imageContainer: StackPane) {
    gc.clearRect(0.0, 0.0, width, height)
    width = image.width
    height = image.height
    imageView.image = image
    imageContainer.maxWidth = image.width
    imageContainer.maxHeight = image.height
}



fun Canvas.openImage(stage: Stage, gc: GraphicsContext, imageView: ImageView, imageContainer: StackPane) {
    val fileChooser = FileChooser()
    val extensionFilters = listOf(
        FileChooser.ExtensionFilter("PNG files (*.png)", "*.png"),
        FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg"),
        FileChooser.ExtensionFilter("JPEG files (*.jpeg)", "*.jpeg")
    )
    fileChooser.extensionFilters.addAll(extensionFilters)

    val file = fileChooser.showOpenDialog(stage)

    val image =  SwingFXUtils.toFXImage(ImageIO.read(file.inputStream()), null)

    setImage(image, gc, imageView, imageContainer)
}

fun Canvas.closeImage(gc:GraphicsContext, imageView: ImageView) {
    gc.clearRect(0.0, 0.0, width, height)
    width = 0.0
    height = 0.0
    imageView.image = null
}