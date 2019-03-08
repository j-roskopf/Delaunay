package triangularization

class Vector2D
/**
 * Constructor of the 2D vector class used to create new vector instances.
 *
 * @param x
 * The x coordinate of the new vector
 * @param y
 * The y coordinate of the new vector
 */
    (var x: Double, var y: Double) {

    /**
     * Subtracts the given vector from this.
     *
     * @param vector
     * The vector to be subtracted from this
     * @return A new instance holding the result of the vector subtraction
     */
    fun sub(vector: Vector2D): Vector2D {
        return Vector2D(this.x - vector.x, this.y - vector.y)
    }

    /**
     * Adds the given vector to this.
     *
     * @param vector
     * The vector to be added to this
     * @return A new instance holding the result of the vector addition
     */
    fun add(vector: Vector2D): Vector2D {
        return Vector2D(this.x + vector.x, this.y + vector.y)
    }

    /**
     * Multiplies this by the given scalar.
     *
     * @param scalar
     * The scalar to be multiplied by this
     * @return A new instance holding the result of the multiplication
     */
    fun mult(scalar: Double): Vector2D {
        return Vector2D(this.x * scalar, this.y * scalar)
    }

    /**
     * Computes the magnitude or length of this.
     *
     * @return The magnitude of this
     */
    fun mag(): Double {
        return Math.sqrt(this.x * this.x + this.y * this.y)
    }

    /**
     * Computes the dot product of this and the given vector.
     *
     * @param vector
     * The vector to be multiplied by this
     * @return A new instance holding the result of the multiplication
     */
    fun dot(vector: Vector2D): Double {
        return this.x * vector.x + this.y * vector.y
    }

    /**
     * Computes the 2D pseudo cross product Dot(Perp(this), vector) of this and
     * the given vector.
     *
     * @param vector
     * The vector to be multiplied to the perpendicular vector of
     * this
     * @return A new instance holding the result of the pseudo cross product
     */
    fun cross(vector: Vector2D): Double {
        return this.y * vector.x - this.x * vector.y
    }

    override fun toString(): String {
        return "Vector2D[$x, $y]"
    }

}