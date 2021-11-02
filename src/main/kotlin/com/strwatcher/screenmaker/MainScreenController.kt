package com.strwatcher.screenmaker

import com.strwatcher.screenmaker.extentions.*
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.*
import javafx.scene.input.MouseEvent
import javafx.scene.layout.StackPane
import javafx.scene.robot.Robot
import javafx.scene.shape.ArcType
import javafx.stage.Screen
import javafx.stage.Stage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainScreenController {

    @FXML
    private lateinit var miOpen: MenuItem

    @FXML
    private lateinit var miSave: MenuItem

    @FXML
    private lateinit var miSaveAs: MenuItem

    @FXML
    private lateinit var miClose: MenuItem

    @FXML
    private lateinit var durationSlider: Slider

    @FXML
    private lateinit var collapseOptionCheckBox: CheckBox

    @FXML
    private lateinit var colorPicker: ColorPicker

    @FXML
    private lateinit var eraseCheckBox: CheckBox

    @FXML
    private lateinit var screenShotButton: Button

    @FXML
    private lateinit var brushSizeSlider: Slider

    @FXML
    private lateinit var scrollImageContainer: ScrollPane

    @FXML
    private lateinit var imageContainer: StackPane

    @FXML
    private lateinit var canvas: Canvas

    private lateinit var graphicsContext: GraphicsContext

    private val robot = Robot()

    private val brushSize get() = brushSizeSlider.value
    private val duration get() = ((durationSlider.value + 0.3) * 1000L).toLong()

    private val drawHandler get() = EventHandler {
        event: MouseEvent ->
        graphicsContext.fill = colorPicker.value
        val x = event.x - brushSize / 2
        val y = event.y - brushSize / 2

        graphicsContext.fillArc(x, y, brushSize, brushSize, 0.0, 360.0, ArcType.OPEN)
    }

    private val eraseHandler get() = EventHandler {
        event: MouseEvent ->

        val x = event.x - brushSize / 2
        val y = event.y - brushSize / 2

        graphicsContext.clearRect(x, y, brushSize, brushSize)
    }

    private val drawEraseCheckBoxHandler get() = EventHandler {
        _: ActionEvent ->

        if (eraseCheckBox.isSelected) {
            canvas.onMouseClicked = eraseHandler
            canvas.onMouseDragged = eraseHandler
        }
        else {
            canvas.onMouseClicked = drawHandler
            canvas.onMouseDragged = drawHandler
        }
    }

    @DelicateCoroutinesApi
    private val takeScreenshot get() = GlobalScope.launch {
        delay(duration)
        Platform.runLater {
            val fullScreenshot = robot.getScreenCapture(null, Screen.getPrimary().bounds)
            val screenshotRectangle = ScreenshotAreaSelector().start(fullScreenshot)
            val croppedScreenshot = fullScreenshot.crop(screenshotRectangle)

            canvas.setImage(croppedScreenshot, graphicsContext)

            stage?.isIconified = false
        }
    }

    @DelicateCoroutinesApi
    private val takeScreenshotHandler get() = EventHandler {
        _: ActionEvent ->
        if (collapseOptionCheckBox.isSelected) stage?.isIconified = true
        takeScreenshot.start()
    }


    var stage: Stage? = null

    @DelicateCoroutinesApi
    fun initialize() {
        imageContainer.prefWidthProperty().bind(scrollImageContainer.widthProperty())
        imageContainer.prefHeightProperty().bind(scrollImageContainer.heightProperty())

        graphicsContext = canvas.graphicsContext2D
        graphicsContext.fill = colorPicker.value

        screenShotButton.onAction = takeScreenshotHandler

        canvas.onMouseDragged = drawHandler
        canvas.onMouseClicked = drawHandler

        eraseCheckBox.onAction = drawEraseCheckBoxHandler

        miSave.onAction = EventHandler { canvas.getBufferedSnapshot().fastSave() }
        miSaveAs.onAction = EventHandler { canvas.getBufferedSnapshot().saveAs(stage!!) }
        miOpen.onAction = EventHandler { canvas.openImage(stage!!, graphicsContext) }
        miClose.onAction = EventHandler { canvas.closeImage(graphicsContext) }
    }
}
