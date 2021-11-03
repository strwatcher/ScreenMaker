package com.strwatcher.screenmaker

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.stage.Stage
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.util.*
import kotlin.io.path.Path


class ScreenMakerApplication : Application() {
    private lateinit var fxmlLoader: FXMLLoader
    private lateinit var scene: Scene
    private lateinit var mainScreenController: MainScreenController

    private val configPath = Paths.home + File.separator + ".screen_maker"
    private lateinit var config: File
    private val appProperties = Properties()



    private val saveCombination = KeyCodeCombination(
        KeyCode.S,
        KeyCodeCombination.CONTROL_DOWN
    )
    private val saveAsCombination = KeyCodeCombination(
        KeyCode.S,
        KeyCodeCombination.CONTROL_DOWN,
        KeyCodeCombination.SHIFT_DOWN
    )
    private val openCombination = KeyCodeCombination(
        KeyCode.O,
        KeyCodeCombination.CONTROL_DOWN
    )
    private val closeCombination = KeyCodeCombination(
        KeyCode.E,
        KeyCodeCombination.CONTROL_DOWN
    )

    private fun bindShortcuts(stage: Stage) {
        stage.scene.accelerators[saveCombination] = Runnable {
            mainScreenController.save()
        }

        stage.scene.accelerators[saveAsCombination] = Runnable {
            mainScreenController.saveAs()
        }

        stage.scene.accelerators[openCombination] = Runnable {
            mainScreenController.open()
        }

        stage.scene.accelerators[closeCombination] = Runnable {
            mainScreenController.close()
        }
    }

    private fun setupProperties() {
        if (Files.notExists(Path(configPath))) {
            Files.createDirectory(Path(configPath))
        }

        config = File(configPath + File.separator + ".config")

        if(!config.exists()) {
            config.createNewFile()
        }

        FileInputStream(config).use {
            appProperties.load(it)
        }
        mainScreenController.appProperties = appProperties
    }

    override fun start(stage: Stage) {
        fxmlLoader = FXMLLoader(ScreenMakerApplication::class.java.getResource("main_screen.fxml"))
        scene = Scene(fxmlLoader.load())
        stage.title = "ScreenMaker"
        stage.scene = scene

        mainScreenController = fxmlLoader.getController()
        mainScreenController.stage = stage
        bindShortcuts(stage)
        setupProperties()
        stage.show()
    }

    override fun stop() {
        FileOutputStream(config).use {
            appProperties.store(it, "")
        }

        super.stop()
    }
}