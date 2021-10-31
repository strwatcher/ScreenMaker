package com.strwatcher.screenmaker

import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.robot.Robot
import javafx.stage.Screen
import javafx.stage.Stage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainScreenController {
    var stage: Stage? = null
    private val robot = Robot()
    @FXML
    private lateinit var borderPaneMain: BorderPane

    @FXML
    private lateinit var menuBar: MenuBar

    @FXML
    private lateinit var menuFile: Menu

    @FXML
    private lateinit var miOpen: MenuItem

    @FXML
    private lateinit var miSave: MenuItem

    @FXML
    private lateinit var miSaveAs: MenuItem

    @FXML
    private lateinit var miClose: MenuItem

    @FXML
    private lateinit var toolsScrollPane: ScrollPane

    @FXML
    private lateinit var vboxTools: VBox

    @FXML
    private lateinit var durationSlider: Slider

    @FXML
    private lateinit var collapseOptionCheckBox: CheckBox

    @FXML
    private lateinit var colorPicker: ColorPicker

    @FXML
    private lateinit var eraseCheckBox: CheckBox

    @FXML
    private lateinit var scrollImageContainer: ScrollPane

    @FXML
    private lateinit var imageContainer: StackPane

    @FXML
    private lateinit var imageView: ImageView

    @FXML
    private lateinit var screenShotButton: Button


    @DelicateCoroutinesApi
    fun onScreenShotButtonClicked() {
        val duration = durationSlider.value

        val job = GlobalScope.launch {
            delay(duration.toLong() * 1000L)

            Platform.runLater {
                val screen = Screen.getPrimary().bounds
                val img = robot.getScreenCapture(null, screen)

                imageView.image = img
                imageView.fitWidth = screen.width
                imageView.fitHeight = screen.height

                stage?.isIconified = false
            }
        }


        if (collapseOptionCheckBox.isSelected) stage?.isIconified = true
        job.start()

    }


}