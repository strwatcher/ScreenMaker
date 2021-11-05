package com.strwatcher.screenmaker

import com.strwatcher.screenmaker.extentions.*
import javafx.application.Platform
import javafx.embed.swing.SwingFXUtils
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.*
import javafx.scene.image.ImageView
import javafx.scene.image.WritableImage
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.scene.robot.Robot
import javafx.scene.shape.ArcType
import javafx.stage.FileChooser
import javafx.stage.Screen
import javafx.stage.Stage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.imageio.ImageIO

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
    private lateinit var toolsScrollPane: ScrollPane

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

    @FXML
    private lateinit var imageView: ImageView

    @FXML
    private lateinit var imageWrapper: StackPane

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

    private val drawEraseCheckBoxHandler = EventHandler {
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
    private val takeScreenshotJob get() = GlobalScope.launch {
        delay(duration)
        Platform.runLater {
            val fullScreenshot = robot.getScreenCapture(null, Screen.getPrimary().bounds)
            val screenshotRectangle = ScreenshotAreaSelector().start(fullScreenshot)
            val croppedScreenshot = fullScreenshot.crop(screenshotRectangle)

            setupImageView(croppedScreenshot)
            stage?.isIconified = false
        }
    }

    private fun setupImageView(image: WritableImage) {
        closeImage()
        canvas.width = image.width
        canvas.height = image.height
        imageView.image = image
        imageContainer.maxWidth = image.width
        imageContainer.maxHeight = image.height
    }

    private fun openImage() {
        val fileChooser = FileChooser()
        val extensionFilters = listOf(
            FileChooser.ExtensionFilter("PNG files (*.png)", "*.png"),
            FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg"),
            FileChooser.ExtensionFilter("JPEG files (*.jpeg)", "*.jpeg")
        )
        fileChooser.extensionFilters.addAll(extensionFilters)

        val file = fileChooser.showOpenDialog(stage)
        val image =  SwingFXUtils.toFXImage(ImageIO.read(file.inputStream()), null)
        setupImageView(image)
    }

    private fun closeImage() {
        graphicsContext.clearRect(0.0, 0.0, canvas.width, canvas.height)
        canvas.width = 0.0
        canvas.height = 0.0
        imageView.image = null
    }

    var stage: Stage? = null
    var appProperties: Properties? = null

    @DelicateCoroutinesApi
    fun initialize() {
        toolsScrollPane.hbarPolicy = ScrollPane.ScrollBarPolicy.NEVER

        imageWrapper.prefWidthProperty().bind(scrollImageContainer.widthProperty())
        imageWrapper.prefHeightProperty().bind(scrollImageContainer.heightProperty())

        graphicsContext = canvas.graphicsContext2D
        graphicsContext.fill = colorPicker.value

        screenShotButton.onAction = EventHandler { screenshot() }

        canvas.onMouseDragged = drawHandler
        canvas.onMouseClicked = drawHandler

        eraseCheckBox.onAction = drawEraseCheckBoxHandler

        miSave.onAction = EventHandler { save() }
        miSaveAs.onAction = EventHandler { saveAs() }
        miOpen.onAction = EventHandler { open() }
        miClose.onAction = EventHandler { close() }
    }

    fun save() {
        imageContainer.getBufferedSnapshot().fastSave(appProperties!!)
    }

    fun saveAs() {
        imageContainer.getBufferedSnapshot().saveAs(stage!!, appProperties!!)
    }

    fun open() {
        openImage()
    }

    fun close() {
        closeImage()
    }

    @DelicateCoroutinesApi
    fun screenshot() {
        if (collapseOptionCheckBox.isSelected) stage?.isIconified = true
        takeScreenshotJob.start()
    }
}
