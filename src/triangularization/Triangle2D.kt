package triangularization

import java.util.Arrays


class Triangle2D(var a: Vector2D, var b: Vector2D, var c: Vector2D) {

    /**
     * Test if this triangle is oriented counterclockwise (CCW). Let A, B and C
     * be three 2D points. If det &gt; 0, C lies to the left of the directed
     * line AB. Equivalently the triangle ABC is oriented counterclockwise. When
     * det &lt; 0, C lies to the right of the directed line AB, and the triangle
     * ABC is oriented clockwise. When det = 0, the three points are colinear.
     * See Real-Time Collision Detection, chap. 3, p. 32
     *
     */
    private val isOrientedCCW: Boolean
        get() {
            val a11 = a.x - c.x
            val a21 = b.x - c.x

            val a12 = a.y - c.y
            val a22 = b.y - c.y

            val det = a11 * a22 - a12 * a21

            return det > 0.0
        }

    /**
     * Tests if a 2D point lies inside this 2D triangle. See Real-Time Collision
     * Detection, chap. 5, p. 206.
     *
     * @param point
     * The point to be tested
     * @return Returns true iff the point lies inside this 2D triangle
     */
    operator fun contains(point: Vector2D): Boolean {
        val pab = point.sub(a).cross(b.sub(a))
        val pbc = point.sub(b).cross(c.sub(b))

        if (!hasSameSign(pab, pbc)) {
            return false
        }

        val pca = point.sub(c).cross(a.sub(c))

        return hasSameSign(pab, pca)

    }

    /**
     * Tests if a given point lies in the circumcircle of this triangle. Let the
     * triangle ABC appear in counterclockwise (CCW) order. Then when det &gt;
     * 0, the point lies inside the circumcircle through the three points a, b
     * and c. If instead det &lt; 0, the point lies outside the circumcircle.
     * When det = 0, the four points are cocircular. If the triangle is oriented
     * clockwise (CW) the result is reversed. See Real-Time Collision Detection,
     * chap. 3, p. 34.
     *
     */
    fun isPointInCircumCircle(point: Vector2D): Boolean {
        val a11 = a.x - point.x
        val a21 = b.x - point.x
        val a31 = c.x - point.x

        val a12 = a.y - point.y
        val a22 = b.y - point.y
        val a32 = c.y - point.y

        val a13 = (a.x - point.x) * (a.x - point.x) + (a.y - point.y) * (a.y - point.y)
        val a23 = (b.x - point.x) * (b.x - point.x) + (b.y - point.y) * (b.y - point.y)
        val a33 = (c.x - point.x) * (c.x - point.x) + (c.y - point.y) * (c.y - point.y)

        val det = (a11 * a22 * a33 + a12 * a23 * a31 + a13 * a21 * a32 - a13 * a22 * a31 - a12 * a21 * a33
                - a11 * a23 * a32)

        return if (isOrientedCCW) {
            det > 0.0
        } else det < 0.0

    }

    /**
     * Returns true if this triangle contains the given edge.
     */
    fun isNeighbour(edge: Edge2D): Boolean {
        return (a == edge.a || b == edge.a || c == edge.a) && (a == edge.b || b == edge.b || c == edge.b)
    }

    /**
     * Returns the vertex of this triangle that is not part of the given edge.
     */
    fun getNoneEdgeVertex(edge: Edge2D): Vector2D? {
        if (a != edge.a && a != edge.b) {
            return a
        } else if (b != edge.a && b != edge.b) {
            return b
        } else if (c != edge.a && c != edge.b) {
            return c
        }

        return null
    }

    /**
     * Returns true if the given vertex is one of the vertices describing this
     * triangle.
     */
    fun hasVertex(vertex: Vector2D): Boolean {
        return a == vertex || b == vertex || c == vertex

    }

    /**
     * Returns an EdgeDistancePack containing the edge and its distance nearest
     * to the specified point.
     */
    fun findNearestEdge(point: Vector2D): EdgeDistancePack {
        val edges = arrayOfNulls<EdgeDistancePack>(3)

        edges[0] = EdgeDistancePack(
            Edge2D(a, b),
            computeClosestPoint(Edge2D(a, b), point).sub(point).mag()
        )
        edges[1] = EdgeDistancePack(
            Edge2D(b, c),
            computeClosestPoint(Edge2D(b, c), point).sub(point).mag()
        )
        edges[2] = EdgeDistancePack(
            Edge2D(c, a),
            computeClosestPoint(Edge2D(c, a), point).sub(point).mag()
        )

        Arrays.sort(edges)
        return edges[0]!!
    }

    /**
     * Computes the closest point on the given edge to the specified point.
     */
    private fun computeClosestPoint(edge: Edge2D, point: Vector2D): Vector2D {
        val ab = edge.b.sub(edge.a)
        var t = point.sub(edge.a).dot(ab) / ab.dot(ab)

        if (t < 0.0) {
            t = 0.0
        } else if (t > 1.0) {
            t = 1.0
        }

        return edge.a.add(ab.mult(t))
    }

    /**
     * Tests if the two arguments have the same sign.
     */
    private fun hasSameSign(a: Double, b: Double): Boolean {
        return Math.signum(a) == Math.signum(b)
    }
}