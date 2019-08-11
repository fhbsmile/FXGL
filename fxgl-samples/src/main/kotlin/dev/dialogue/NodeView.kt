/*
 * FXGL - JavaFX Game Library. The MIT License (MIT).
 * Copyright (c) AlmasB (almaslvl@gmail.com).
 * See LICENSE for details.
 */

package dev.dialogue

import com.almasb.fxgl.dsl.FXGL
import javafx.scene.Node
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.CubicCurve
import javafx.scene.text.Text
import sandbox.cutscene.MouseGestures

/**
 *
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
abstract class NodeView(val node: DialogueNode) : Pane() {

    var w: Double = 150.0
    var h: Double = 100.0

    var outPoints = arrayListOf<LinkPoint>()

    var inPoints = arrayListOf<LinkPoint>()

    private val mouseGestures = MouseGestures()

    val contentRoot = VBox(10.0)

    init {



        setPrefSize(w, h)

        background = Background(BackgroundFill(Color.color(0.25, 0.25, 0.25, 0.75), null, null))

        styleClass.add("dialog-border")

        mouseGestures.makeDraggable(this)

        children.add(FXGL.getUIFactory().newText(javaClass.simpleName, Color.WHITE, 24.0))

        contentRoot.translateX = 35.0
        contentRoot.translateY = 10.0

        children.add(contentRoot)


        val text = Text(node.text)
        text.translateY = 50.0

        addContent(text)
    }

    fun addContent(node: Node) {
        contentRoot.children.add(node)
    }

    fun addInPoint(linkPoint: LinkPoint) {
        inPoints.add(linkPoint)

        children.add(linkPoint)

        linkPoint.translateX = 10.0
        linkPoint.translateYProperty().bind(heightProperty().divide(2))
    }

    fun addOutPoint(linkPoint: LinkPoint) {
        outPoints.add(linkPoint)

        children.add(linkPoint)

        linkPoint.translateXProperty().bind(widthProperty().add(-25))
        linkPoint.translateYProperty().bind(heightProperty().divide(2))
    }

    fun connect(fromNodeView: NodeView, outLinkPoint: OutLinkPoint, inLinkPoint: InLinkPoint): Node {

        val curve = CubicCurve()
        with(curve) {
            startXProperty().bind(fromNodeView.layoutXProperty().add(outLinkPoint.translateXProperty().add(10)))
            startYProperty().bind(fromNodeView.layoutYProperty().add(outLinkPoint.translateYProperty().add(10)))


            controlX1Property().bind(startXProperty().add(endXProperty()).divide(2))
            controlY1Property().bind(startYProperty())

            controlX2Property().bind(startXProperty().add(endXProperty()).divide(2))
            controlY2Property().bind(endYProperty())

            strokeWidth = 2.0
            stroke = Color.color(0.9, 0.9, 0.9, 0.9)
            fill = null
        }

        curve.endXProperty().bind(layoutXProperty().add(inLinkPoint.translateXProperty()).add(10))
        curve.endYProperty().bind(layoutYProperty().add(inLinkPoint.translateYProperty()).add(10))

        return curve
    }
}