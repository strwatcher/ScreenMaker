package com.strwatcher.screenmaker.extentions

import com.strwatcher.screenmaker.constants.Paths
import com.strwatcher.screenmaker.constants.lastSaveFolder
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.imageio.ImageIO
import javax.swing.JFileChooser

fun BufferedImage.fastSave(appProperties: Properties) {
    val file = File(
            Paths.pictures +
                File.separator +
                "Screenshot " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hhmmss")) +
                ".png"
    )
    save(file, appProperties)
}

fun BufferedImage.saveAs(stage: Stage, appProperties: Properties) {
    var file: File?
    val fileChooser = FileChooser()
    fileChooser.initialDirectory = if (appProperties.keys.contains(lastSaveFolder)) {
        File(appProperties.getProperty(lastSaveFolder))
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

    file?.let { save(file, appProperties) }
}

fun BufferedImage.save(file: File, appProperties: Properties) {
    try {
        ImageIO.write(this, "png", file)
        appProperties[lastSaveFolder] = file.parent
    } catch (exception: IOException) {
        exception.printStackTrace()
    }
}