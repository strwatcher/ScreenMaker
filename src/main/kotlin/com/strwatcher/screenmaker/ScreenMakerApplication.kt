package com.strwatcher.screenmaker

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class ScreenMakerApplication : Application() {
    private lateinit var fxmlLoader: FXMLLoader
    private lateinit var scene: Scene
    private lateinit var mainScreenController: MainScreenController
    override fun start(stage: Stage) {
        fxmlLoader = FXMLLoader(ScreenMakerApplication::class.java.getResource("main_screen.fxml"))
        scene = Scene(fxmlLoader.load(), 900.0, 700.0)
        stage.title = "ScreenMaker"
        stage.scene = scene

        mainScreenController = fxmlLoader.getController()
        mainScreenController.stage = stage

        stage.show()
    }
}

fun main() {
    Application.launch(ScreenMakerApplication::class.java)
}