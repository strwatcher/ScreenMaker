package com.strwatcher.screenmaker.extentions

import javafx.stage.FileChooser
import javafx.stage.Stage
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.imageio.ImageIO
import javax.swing.JFileChooser

fun BufferedImage.fastSave() {
    val picturesDirectory = System.getProperty("user.home") + File.separator + "Pictures"
    val file = File(
        picturesDirectory +
                File.separator +
                "Screenshot " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hhmmss")) +
                ".png"
    )
    println(file.path)
    save(file)
}

fun BufferedImage.saveAs(stage: Stage) {
    var file: File?
    val fileChooser = FileChooser()

    fileChooser.initialDirectory = if (false) {
        File(JFileChooser().fileSystemView.defaultDirectory.toString())
    } else {
        File(JFileChooser().fileSystemView.defaultDirectory.toString())
    }
    val extensionFilter = FileChooser.ExtensionFilter(
        "PNG files (*.png)", "*.png"
    )
    fileChooser.extensionFilters.add(extensionFilter)

    file = fileChooser.showSaveDialog(stage)
    if (file.nameWithoutExtension == file.name) {
        file = File(file.parentFile, file.nameWithoutExtension + ".png")
    }

    file?.let { save(file) }
}

fun BufferedImage.save(file: File) {
    try {
        ImageIO.write(this, "png", file)
    } catch (exception: IOException) {
        exception.printStackTrace()
    }
}