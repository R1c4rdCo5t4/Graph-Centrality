package graphCollections

import kotlin.NoSuchElementException
import kotlin.collections.HashMap


class GraphStructure<I, D>: Graph<I, D> {

    private var map = HashMap<I, Vertex<I, D>>()
    override var size = 0

    open class Vertex<I, D>(i: I, d: D) : Graph.Vertex<I, D>{

        override var id : I = i
        override var data : D = d

        private var adjSet = mutableSetOf<Edge<I>?>()

        override fun setData(newData: D): D{
            val lastData = data
            data = newData
            return lastData
        }

        override fun getAdjacencies(): MutableSet<Edge<I>?> {
            return adjSet
        }
    }

    class Edge<I>(i: I,adj: I): Graph.Edge<I>{
        override val id: I = i
        override val adjacent: I = adj
    }


    override fun addVertex(id: I, d: D): D? {

        if(id !in map.keys) {
            map[id] = Vertex(id,d) // add key-value pair (id-vertex)
            size++
            return d
        }
        return null
    }

    override fun addEdge(id: I, idAdj: I): I? {

        val vertex = map[id]
        val edge = Edge(id,idAdj)

        if(vertex != null){
            if(edge !in vertex.getAdjacencies()) {
                vertex.getAdjacencies().add(edge)
                return idAdj
            }
        }
        return null
    }

    override fun getVertex(id: I): Vertex<I, D>? {
        return map[id] // return value vertex by key id
    }

    override fun getEdge(id: I, idAdj: I): Edge<I>? {

        val adj = getVertex(id)?.getAdjacencies()

        if (adj != null) {
            for(i in adj) if(i?.adjacent == idAdj) return i // return correspondent edge from adjacencies
        }
        return null

    }

    override fun iterator() : Iterator<Vertex<I, D>> = MyIterator()

    private inner class MyIterator : Iterator<Vertex<I, D>> {

        var currIdx = -1
        var currVertex : Vertex<I, D>? = null

        override fun hasNext(): Boolean {
            if(currVertex != null) return true

            if(currIdx < (map.values.size - 1)){ // has next vertex
                currVertex = map.values.elementAt(++currIdx)
                return true
            }
            return false
        }

        override fun next(): Vertex<I, D> {
            if (!hasNext()) throw NoSuchElementException() // no more vertices

            val aux = currVertex
            currVertex = null
            return aux!!
        }
    }


    override fun edgesIterator(): Iterator<Edge<I>> = MyEdgeIterator()

    private inner class MyEdgeIterator : Iterator<Edge<I>> {

        var currEdgeIdx = -1
        val vertexIterator = MyIterator()
        var currVertex : Vertex<I, D>? = null
        var currEdge : Edge<I>? = null
        var currVertexAdj = mutableSetOf<Edge<I>?>()

        override fun hasNext(): Boolean {

            if(currEdge != null) return true // already has edge to return, don't change

            while(true){
                if(currEdgeIdx < currVertexAdj.size - 1) { // currVertex has next edge
                    currEdge = currVertexAdj.elementAt(++currEdgeIdx) // vertex's edge at ++currEdgeIdx
                    return true
                }
                // new vertex
                if(vertexIterator.hasNext()){
                    currVertex = vertexIterator.next()
                    currEdgeIdx = -1
                    currVertexAdj = currVertex!!.getAdjacencies()
                }
                else return false
            }
        }

        override fun next(): Edge<I> {
            if (!hasNext()) throw NoSuchElementException() // no more edges in any vertex

            val aux = currEdge
            currEdge = null
            return aux!!
        }
    }
}







