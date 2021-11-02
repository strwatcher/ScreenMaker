package com.strwatcher.screenmaker

import javafx.application.Platform
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.geometry.Rectangle2D
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.*
import javafx.scene.image.ImageView
import javafx.scene.image.WritableImage
import javafx.scene.image.WritablePixelFormat
import javafx.scene.input.MouseEvent
import javafx.scene.layout.BorderPane
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.robot.Robot
import javafx.scene.shape.ArcType
import javafx.scene.shape.StrokeLineCap
import javafx.scene.shape.StrokeLineJoin
import javafx.stage.Screen
import javafx.stage.Stage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainScreenController {

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
    private lateinit var screenShotButton: Button

    @FXML
    private lateinit var brushSizeSlider: Slider

    @FXML
    private lateinit var scrollImageContainer: ScrollPane

    @FXML
    private lateinit var imageContainer: StackPane

    @FXML
    private lateinit var imageView: ImageView

    @FXML
    private lateinit var canvas: Canvas


    var stage: Stage? = null
    private val robot = Robot()
    private lateinit var graphicsContext: GraphicsContext

    private val brushSize get() = brushSizeSlider.value

    private val drawHandler = EventHandler {
        event: MouseEvent ->
        graphicsContext.fill = colorPicker.value
        val x = event.x - brushSize / 2
        val y = event.y - brushSize / 2

        graphicsContext.fillArc(x, y, brushSize, brushSize, 0.0, 360.0, ArcType.OPEN)
    }

    private val eraseHandler = EventHandler {
        event: MouseEvent ->

        val x = event.x - brushSize / 2
        val y = event.y - brushSize / 2

        graphicsContext.clearRect(x, y, brushSize, brushSize)
    }

    fun initialize() {
        graphicsContext = canvas.graphicsContext2D

        graphicsContext.fill = colorPicker.value
        graphicsContext.lineJoin = StrokeLineJoin.ROUND
        graphicsContext.lineCap = StrokeLineCap.ROUND

        canvas.onMouseDragged = drawHandler
        canvas.onMouseClicked = drawHandler

        eraseCheckBox.onAction = EventHandler {
            if (eraseCheckBox.isSelected) {
                canvas.onMouseClicked = eraseHandler
                canvas.onMouseDragged = eraseHandler
            }
            else {
                canvas.onMouseClicked = drawHandler
                canvas.onMouseDragged = drawHandler
            }
        }
    }



    @DelicateCoroutinesApi
    fun onScreenShotButtonClicked() {
        val duration = durationSlider.value + 0.3

        val job = GlobalScope.launch {
            delay((duration * 1000).toLong())

            Platform.runLater {
                val screen = Screen.getPrimary().bounds
                val fullScreenshot = robot.getScreenCapture(null, screen)
                val screenshotRect = ScreenshotAreaSelector().start(fullScreenshot)

                val screenshot = cropImage(fullScreenshot, screenshotRect)

                imageView.image = screenshot
                imageView.fitWidth = screenshot.width
                imageView.fitHeight = screenshot.height
                imageContainer.prefWidthProperty().bind(scrollImageContainer.widthProperty())
                imageContainer.prefHeightProperty().bind(scrollImageContainer.heightProperty())
                canvas.widthProperty().bind(imageView.fitWidthProperty())
                canvas.heightProperty().bind(imageView.fitHeightProperty())

                stage?.isIconified = false
            }
        }


        if (collapseOptionCheckBox.isSelected) stage?.isIconified = true
        job.start()

    }

    private fun cropImage(image: WritableImage, newImageRect: Rectangle2D): WritableImage {
        val croppedImage = WritableImage(newImageRect.width.toInt(), newImageRect.height.toInt())

        val reader = image.pixelReader
        val writer = croppedImage.pixelWriter
        val buffer = ByteArray(newImageRect.width.toInt() * newImageRect.height.toInt() * 4)
        val format = WritablePixelFormat.getByteBgraInstance()

        reader.getPixels(
            newImageRect.minX.toInt(), newImageRect.minY.toInt(),
            newImageRect.width.toInt(), newImageRect.height.toInt(),
            format, buffer, 0, newImageRect.width.toInt() * 4
        )

        writer.setPixels(
            0, 0,
            newImageRect.width.toInt(), newImageRect.height.toInt(),
            format, buffer, 0, newImageRect.width.toInt() * 4
        )

        return croppedImage
    }


}