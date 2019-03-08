import triangularization.DelaunayTriangulator
import triangularization.Vector2D
import java.awt.Color
import java.awt.Polygon
import java.awt.geom.Ellipse2D
import java.awt.image.BufferedImage
import java.io.File
import java.time.Duration
import java.util.*
import javax.imageio.ImageIO
import java.time.Instant


class Main {
    companion object {

        private var cells = 8000

        private const val fileName = "in/sunset.jpg"

        private val random = Random()

        @JvmStatic
        fun main(args: Array<String>) {

            val file = File(fileName)

            if (file.exists()) {
                val start = Instant.now()

                val img = ImageIO.read(file)

                val toWrite = BufferedImage(img.width, img.height, BufferedImage.TYPE_4BYTE_ABGR)

                val points = arrayListOf<Vector2D>()

                for (i in 0 until cells) {
                    val xCord = random.nextDouble() * img.width
                    val yCord = random.nextDouble() * img.height
                    points.add(Vector2D(xCord, yCord))
                }

                // add points to complete 'hyper rect' - one at each corner of image
                for (i in 0 until img.width) {
                    points.add(Vector2D(i.toDouble(), 0.0))
                    points.add(Vector2D(i.toDouble(), img.height.toDouble() - 1.0))
                }

                for (i in 0 until img.height) {
                    points.add(Vector2D(0.0, i.toDouble()))
                    points.add(Vector2D(img.width.toDouble() - 1.0, i.toDouble()))
                }

                val delaunayTriangulator = DelaunayTriangulator(points)
                delaunayTriangulator.triangulate()

                val graphics = toWrite.createGraphics()
                graphics.color = Color.BLACK

                for (tt in points.indices) {
                    val point = points[tt]
                    graphics.fill(Ellipse2D.Double(point.x - 2.5, point.y - 2.5, 5.0, 5.0))
                }

                delaunayTriangulator.triangles.forEach {  triangle ->
                    val xPoints = intArrayOf(triangle.a.x.toInt(), triangle.b.x.toInt(), triangle.c.x.toInt())
                    val yPoints = intArrayOf(triangle.a.y.toInt(), triangle.b.y.toInt(), triangle.c.y.toInt())
                    graphics.color = averageColor(xPoints, yPoints, img)
                    graphics.fillPolygon(Polygon(xPoints, yPoints, 3))
                }

                val outputFile = File("out/sunset.png")
                ImageIO.write(toWrite, "png", outputFile)

                val end = Instant.now()

                println(Duration.between(start, end))
            } else {
                println("File doesn't exist")
            }
        }

        private fun averageColor(xPoints: IntArray, yPoints: IntArray, img: BufferedImage): Color {
            val colorOne = Color(img.getRGB(xPoints[0], yPoints[0]))
            val colorTwo = Color(img.getRGB(xPoints[0], yPoints[0]))
            val colorThree = Color(img.getRGB(xPoints[0], yPoints[0]))

            val redAverage = ((colorOne.red + colorTwo.red + colorThree.red).toDouble() / 3.0)
            val blueAverage = ((colorOne.blue + colorTwo.blue + colorThree.blue).toDouble() / 3.0)
            val greenAverage = ((colorOne.green + colorTwo.green + colorThree.green).toDouble() / 3.0)

            return Color(redAverage.toInt(), greenAverage.toInt(), blueAverage.toInt())
        }
    }
}