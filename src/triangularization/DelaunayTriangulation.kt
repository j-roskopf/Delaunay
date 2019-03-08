package triangularization

import java.lang.Exception

class DelaunayTriangulator(pointSet: List<Vector2D>) {

    private var pointSet: List<Vector2D>? = null
    private var triangleSoup: TriangleMap? = null

    val triangles: List<Triangle2D>
        get() = triangleSoup!!.triangles

    init {
        this.pointSet = pointSet
        this.triangleSoup = TriangleMap()
    }

    fun triangulate() {
        triangleSoup = TriangleMap()

        if (pointSet == null || pointSet!!.size < 3) {
            throw Exception("Less than three points in point set.")
        }

        /**
         * In order for the in circumcircle test to not consider the vertices of
         * the super triangle we have to start out with a big triangle
         * containing the whole point set. We have to scale the super triangle
         * to be very large. Otherwise the triangulation is not convex.
         */
        var maxOfAnyCoordinate = 0.0

        for (vector in pointSet!!) {
            maxOfAnyCoordinate = Math.max(Math.max(vector.x, vector.y), maxOfAnyCoordinate)
        }

        maxOfAnyCoordinate *= 16.0

        val p1 = Vector2D(0.0, 3.0 * maxOfAnyCoordinate)
        val p2 = Vector2D(3.0 * maxOfAnyCoordinate, 0.0)
        val p3 = Vector2D(-3.0 * maxOfAnyCoordinate, -3.0 * maxOfAnyCoordinate)

        val superTriangle = Triangle2D(p1, p2, p3)

        triangleSoup!!.add(superTriangle)

        for (i in pointSet!!.indices) {
            val triangle = triangleSoup!!.findContainingTriangle(pointSet!![i])

            if (triangle == null) {
                val edge = triangleSoup!!.findNearestEdge(pointSet!![i])

                val first = triangleSoup!!.findOneTriangleSharing(edge)
                val second = triangleSoup!!.findNeighbour(first!!, edge)

                val firstNoneEdgeVertex = first.getNoneEdgeVertex(edge)!!
                val secondNoneEdgeVertex = second!!.getNoneEdgeVertex(edge)!!

                triangleSoup!!.remove(first)
                triangleSoup!!.remove(second)

                val triangle1 = Triangle2D(edge.a, firstNoneEdgeVertex, pointSet!![i])
                val triangle2 = Triangle2D(edge.b, firstNoneEdgeVertex, pointSet!![i])
                val triangle3 = Triangle2D(edge.a, secondNoneEdgeVertex, pointSet!![i])
                val triangle4 = Triangle2D(edge.b, secondNoneEdgeVertex, pointSet!![i])

                triangleSoup!!.add(triangle1)
                triangleSoup!!.add(triangle2)
                triangleSoup!!.add(triangle3)
                triangleSoup!!.add(triangle4)

                legalizeEdge(triangle1, Edge2D(edge.a, firstNoneEdgeVertex), pointSet!![i])
                legalizeEdge(triangle2, Edge2D(edge.b, firstNoneEdgeVertex), pointSet!![i])
                legalizeEdge(triangle3, Edge2D(edge.a, secondNoneEdgeVertex), pointSet!![i])
                legalizeEdge(triangle4, Edge2D(edge.b, secondNoneEdgeVertex), pointSet!![i])
            } else {
                /**
                 * The vertex is inside a triangle.
                 */
                val a = triangle.a
                val b = triangle.b
                val c = triangle.c

                triangleSoup!!.remove(triangle)

                val first = Triangle2D(a, b, pointSet!![i])
                val second = Triangle2D(b, c, pointSet!![i])
                val third = Triangle2D(c, a, pointSet!![i])

                triangleSoup!!.add(first)
                triangleSoup!!.add(second)
                triangleSoup!!.add(third)

                legalizeEdge(first, Edge2D(a, b), pointSet!![i])
                legalizeEdge(second, Edge2D(b, c), pointSet!![i])
                legalizeEdge(third, Edge2D(c, a), pointSet!![i])
            }
        }

        /**
         * Remove all triangles that contain vertices of the super triangle.
         */
        triangleSoup!!.removeTrianglesUsing(superTriangle.a)
        triangleSoup!!.removeTrianglesUsing(superTriangle.b)
        triangleSoup!!.removeTrianglesUsing(superTriangle.c)
    }

    /**
     * This method legalizes edges by recursively flipping all illegal edges.
     */
    private fun legalizeEdge(triangle: Triangle2D, edge: Edge2D, newVertex: Vector2D) {
        val neighbourTriangle = triangleSoup!!.findNeighbour(triangle, edge)

        /**
         * If the triangle has a neighbor, then legalize the edge
         */
        if (neighbourTriangle != null) {
            if (neighbourTriangle.isPointInCircumCircle(newVertex)) {
                triangleSoup!!.remove(triangle)
                triangleSoup!!.remove(neighbourTriangle)

                val noneEdgeVertex = neighbourTriangle.getNoneEdgeVertex(edge)!!

                val firstTriangle = Triangle2D(noneEdgeVertex, edge.a, newVertex)
                val secondTriangle = Triangle2D(noneEdgeVertex, edge.b, newVertex)

                triangleSoup!!.add(firstTriangle)
                triangleSoup!!.add(secondTriangle)

                legalizeEdge(firstTriangle, Edge2D(noneEdgeVertex, edge.a), newVertex)
                legalizeEdge(secondTriangle, Edge2D(noneEdgeVertex, edge.b), newVertex)
            }
        }
    }
}