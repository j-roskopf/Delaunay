package triangularization

import java.util.ArrayList
import java.util.Arrays


internal class TriangleMap {

    private val triangleMap: MutableList<Triangle2D>

    /**
     * Returns the triangles from this triangle soup.
     *
     */
    val triangles: List<Triangle2D>
        get() = this.triangleMap

    /**
     * Constructor of the triangle soup class used to create a new triangle soup
     * instance.
     */
    init {
        this.triangleMap = ArrayList()
    }

    /**
     * Adds a triangle to this triangle soup.
     *
     */
    fun add(triangle: Triangle2D) {
        this.triangleMap.add(triangle)
    }

    /**
     * Removes a triangle from this triangle soup.
     *
     */
    fun remove(triangle: Triangle2D) {
        this.triangleMap.remove(triangle)
    }

    /**
     * Returns the triangle from this triangle soup that contains the specified
     * point or null if no triangle from the triangle soup contains the point.
     *
     */
    fun findContainingTriangle(point: Vector2D): Triangle2D? {
        for (triangle in triangleMap) {
            if (triangle.contains(point)) {
                return triangle
            }
        }
        return null
    }

    /**
     * Returns the neighbor triangle of the specified triangle sharing the same
     * edge as specified. If no neighbor sharing the same edge exists null is
     * returned.
     *
     */
    fun findNeighbour(triangle: Triangle2D, edge: Edge2D): Triangle2D? {
        for (t in triangleMap) {
            if (t.isNeighbour(edge) && t !== triangle) {
                return t
            }
        }
        return null
    }

    /**
     * Returns one of the possible triangles sharing the specified edge. Based
     * on the ordering of the triangles in this triangle soup the returned
     * triangle may differ. To find the other triangle that shares this edge use
     * the [] method.
     */
    fun findOneTriangleSharing(edge: Edge2D): Triangle2D? {
        for (triangle in triangleMap) {
            if (triangle.isNeighbour(edge)) {
                return triangle
            }
        }
        return null
    }

    /**
     * Returns the edge from the triangle soup nearest to the specified point.
     */
    fun findNearestEdge(point: Vector2D): Edge2D {
        val edgeList = ArrayList<EdgeDistancePack>()

        for (triangle in triangleMap) {
            edgeList.add(triangle.findNearestEdge(point))
        }

        val temp = edgeList.toTypedArray()

        Arrays.sort(temp)
        return temp[0].edge
    }

    /**
     * Removes all triangles from this triangle soup that contain the specified
     * vertex.
     */
    fun removeTrianglesUsing(vertex: Vector2D) {
        val trianglesToBeRemoved = ArrayList<Triangle2D>()

        for (triangle in triangleMap) {
            if (triangle.hasVertex(vertex)) {
                trianglesToBeRemoved.add(triangle)
            }
        }

        triangleMap.removeAll(trianglesToBeRemoved)
    }

}