package graphCollections

import java.io.BufferedReader
import java.io.FileReader
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*


fun GraphStructure<Int, VertexProperties?>.build(file : String){
    val reader = BufferedReader(FileReader(file))
    var currLine = reader.readLine()
    println("Loading $file...")
    while(currLine != null){

        val input = currLine.split("\\s".toRegex()).map{it.trim().toInt()}
        input.forEach { addVertex(it, null) }
        addEdge(input[0],input[1])
        addEdge(input[1],input[0]) // adds reverse edge

        currLine = reader.readLine()
    }
}


fun GraphStructure<Int, VertexProperties?>.getDegree(vertexId: Int):Int{
    return getVertex(vertexId)?.getAdjacencies()?.size!!
}


fun GraphStructure<Int, VertexProperties?>.getCloseness(vertexId: Int): Float{

    var smallestDstSum = 0F
    BFS(vertexId)
    for(v in this.iterator()){
        if(v.id != vertexId) {
            val dst = getVertex(v.id)?.data?.distance!!
            if(dst != INFINITY) smallestDstSum += dst
        }
    }
    return 1 / smallestDstSum
}

fun GraphStructure<Int, VertexProperties?>.getSmallestDistance(vertexId1:Int, vertexId2:Int):Int{
    BFS(vertexId1)
    return getVertex(vertexId2)?.data?.distance!!
}


fun format(value:Any):String{
    val df = DecimalFormat("#.###")
    df.roundingMode = RoundingMode.HALF_UP
    if(value == INFINITY) return "∞"
    return df.format(value)
}


const val INFINITY = Int.MAX_VALUE
enum class Color { WHITE, BLACK, GREY }

data class VertexProperties(var color: Color, var distance:Int, var p : GraphStructure.Vertex<Int, VertexProperties?>? )

fun GraphStructure<Int, VertexProperties?>.BFS(vertexId1: Int) {

    for(v in this.iterator()) v.setData(VertexProperties(Color.WHITE, INFINITY,null)) // mark all vertices as unvisited

    val s = getVertex(vertexId1) // initial vertex
    s?.setData(VertexProperties(Color.GREY, 0,null)) // mark initial vertex as visited

    val q = Queue<GraphStructure.Vertex<Int, VertexProperties?>?>(size)
    if (s != null) q.enqueue(s) // add first vertex

    while (!q.isEmpty()) {
        val u = q.dequeue() // removing each vertex from queue, whose adjacents will be visited
        if (u != null) {
            val edges = u.getAdjacencies() // get all adjacents to u
            for (edge in edges){ // iterate over all edges of u
                val v = edge?.adjacent?.let { getVertex(it) } // set v as each adjacent
                if (v != null && v.data?.color == Color.WHITE) { // if v was not visited yet
                    v.setData(VertexProperties(Color.GREY, u.data?.distance!! + 1,u)) // mark v as visited
                    q.enqueue(v) // add v to the queue
                }
            }
            u.data?.color = Color.BLACK // all adjacents to u visited
        }
    }
}

class Queue<E>(max: Int) {
    private val q = Array<Any?>(max){ null } as Array<E?>
    private var head = 0
    private var tail = 0
    private var count = 0

    fun isEmpty() = count == 0

    fun enqueue(item: E): E? =
        if (count == q.size) null
        else {
            q[tail] = item
            tail = (tail + 1) % q.size
            count++
            item
        }

    fun dequeue(): E? =
        if (isEmpty()) throw EmptyStackException()
        else {
            val item = q[head]
            head = (head + 1) % q.size
            count--
            item
        }

    fun peek(): E? =
        if (isEmpty()) throw EmptyStackException()
        else q[head]
}