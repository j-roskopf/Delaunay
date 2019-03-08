package triangularization

data class EdgeDistancePack(var edge: Edge2D, private var distance: Double) : Comparable<EdgeDistancePack> {
    override fun compareTo(other: EdgeDistancePack): Int {
        return java.lang.Double.compare(this.distance, other.distance)
    }
}