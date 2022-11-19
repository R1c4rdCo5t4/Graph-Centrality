package series.serie3

import graphCollections.GraphStructure
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class GraphTest {

    @Test
    fun graph_empty() {
        val graph = GraphStructure<Int, String>()
        assertEquals(0, graph.size)
    }

    @Test
    fun graph_singleton() {
        val graph = GraphStructure<Int, String>()
        assertEquals("V1", graph.addVertex(1, "V1"))
        Assertions.assertNull(graph.addVertex(1, "V1"))
        assertEquals(1, graph.size)
        assertEquals("V1", graph.getVertex(1)!!.data)
        assertEquals(2, graph.addEdge(1, 2))
        assertEquals(3, graph.addEdge(1, 3))
        assertTrue(graph.getEdge(1, 2) != null)
        Assertions.assertNotNull(graph.getEdge(1, 3))
        Assertions.assertNull(graph.addEdge(0, 4))
        Assertions.assertNull(graph.getEdge(0, 4))
        Assertions.assertNull(graph.getEdge(1, 9))
    }

    @Test
    fun graph_equalElements() {
        val graph = GraphStructure<Int, String>()
        val setE: MutableSet<GraphStructure.Edge<Int>?> = mutableSetOf()
        for (id in 1..4)
            assertEquals("V$id", graph.addVertex(id, "V$id"))
        assertEquals(4, graph.size)
        for (id in 1..4) for (id2 in 1..4)
            if (id2 != id) assertEquals(id2, graph.addEdge(id, id2))
        for (id in 1..4) {
            val set2 = mutableSetOf(1, 2, 3, 4)
            set2.remove(id)
            for (adj in set2)
                Assertions.assertNotNull(graph.getEdge(id, adj))
        }
    }

    @Test
    fun graph_someElements() {
        val graph = GraphStructure<Int, String>()
        for (id in 0..99)
            assertEquals("V$id", graph.addVertex(id, "V$id"))
        assertEquals(100, graph.size)
        for (id in 0..99) for (id2 in 0..99)
            if (id2 != id) assertEquals(id2, graph.addEdge(id, id2))
        for (id in 0..99) for (id2 in 0..99)
            if (id2 != id) assertEquals(id2, graph.addEdge(id, id2))
        val setV: MutableSet<String> = mutableSetOf()
        for (id in 0..99) {
            val vertex = graph.getVertex(id)
            if (vertex != null) setV.add(vertex.data)
        }
        val set: MutableSet<String> = mutableSetOf()
        for (id in 0..99)
            set.add("V$id")
        assertEquals(setV, set)
    }

    @Test
    fun graph_testIterators() {
        val graph = GraphStructure<Int, String>()
        val setV: MutableSet<String> = mutableSetOf()
        val setE1: MutableSet<GraphStructure.Edge<Int>?> = mutableSetOf()
        val setE2: MutableSet<GraphStructure.Edge<Int>?> = mutableSetOf()
        for (id in 1..4)
            assertEquals("V$id", graph.addVertex(id, "V$id"))
        for (v in graph)
            setV.add(v.data)
        assertEquals(setV, mutableSetOf("V1", "V2", "V3", "V4"))
        for (id in 1..4) for (id2 in 1..4)
            if (id2 != id) assertEquals(id2, graph.addEdge(id, id2))
        for (e in graph.edgesIterator())
            setE1.add(e)
        for (v in graph) {
            val edges = v.getAdjacencies()
            for (e in edges) setE2.add(e)
        }
        assertEquals(setE1, setE2)
    }
    
    /**
     * Vertex Iterator
     */
    @Test
    fun no_vertices() {
        val graph = GraphStructure<Int, String>()

        val iterator = graph.iterator()
        val actualSet  = mutableSetOf<GraphStructure.Vertex<Int, String>?>()
        val expectedSet = mutableSetOf<GraphStructure.Vertex<Int, String>?>()

        while(iterator.hasNext()) actualSet.add(iterator.next())



        assertEquals(expectedSet, actualSet)
    }

    @Test
    fun multiple_vertices() {
        val graph = GraphStructure<Int, String>()

        for(id in 1..20) graph.addVertex(id,"$id")
        graph.addVertex(1,"$1")

        val iterator = graph.iterator()
        val actualSet  = mutableSetOf<GraphStructure.Vertex<Int, String>>()
        val expectedSet = mutableSetOf<GraphStructure.Vertex<Int, String>>()

        while(iterator.hasNext()) actualSet.add(iterator.next())

        for(id in 1..graph.size+1){
            val vertex = graph.getVertex(id)
            if(vertex != null) expectedSet.add(vertex)
        }

        assertEquals(expectedSet, actualSet)

    }

    @Test
    fun random_vertices() {
        val graph = GraphStructure<Int, String>()
        val random = (0..100).toList().shuffled()


        for(i in 1..50){
            graph.addVertex(random[i],"${random[i]}")
        }

        val iterator = graph.iterator()
        val actualSet = mutableSetOf<Int>()
        val expectedSet = mutableSetOf<Int>()

        while(iterator.hasNext()) actualSet.add(iterator.next().id)


        for(i in 0..graph.size){
            val vertex = graph.getVertex(random[i])
            if(vertex != null) expectedSet.add(vertex.id)
        }


        assertEquals(expectedSet, actualSet)
    }

    /**
     * Edges Iterator
     */
    @Test
    fun no_edges() {
        val graph = GraphStructure<Int, String>()

        for(id in 1..4) graph.addVertex(id,"$id")

        val iterator = graph.edgesIterator()
        val actualSet  = mutableSetOf<GraphStructure.Edge<Int>>()

        while(iterator.hasNext()){
            actualSet.add(iterator.next())
        }

        val expectedSet = mutableSetOf<GraphStructure.Edge<Int>>()

        assertEquals(expectedSet, actualSet)
    }

    @Test
    fun two_edges() {
        val graph = GraphStructure<Int, String>()

        graph.addVertex(1,"1")
        graph.addVertex(2,"2")

        graph.addEdge(1,2)
        graph.addEdge(2,1)
        graph.addEdge(2,1)

        val actualSet = mutableSetOf<GraphStructure.Edge<Int>>()
        val expectedSet = mutableSetOf(GraphStructure.Edge(1, 2), GraphStructure.Edge(2, 1))

        val iterator = graph.edgesIterator()
        while(iterator.hasNext()) {
            actualSet.add(iterator.next())
        }

        for(i in expectedSet.indices){
            assertEquals(true, cmpEdges(expectedSet.elementAt(i),actualSet.elementAt(i)))
        }


    }

    @Test
    fun multiple_edges() {
        val graph = GraphStructure<Int, String>()


        for(id in 0..49) graph.addVertex(id,"$id")
        val actualSet = mutableSetOf<GraphStructure.Edge<Int>>()
        val expectedSet = mutableSetOf<GraphStructure.Edge<Int>>()

        for(id in 0..24){
            for(idAdj in 25..50){
                graph.addEdge(id,idAdj)
                expectedSet.add(graph.getEdge(id,idAdj)!!)
            }
        }


        val iterator = graph.edgesIterator()
        while(iterator.hasNext()) {
            actualSet.add(iterator.next())
        }


        for(i in expectedSet.indices){
            assertEquals(true, cmpEdges(expectedSet.elementAt(i),actualSet.elementAt(i)))
        }

    }

    @Test
    fun random_vertex_and_edges() {


        val graph = GraphStructure<Int, String>()
        val random = (0..100).toList().shuffled()
        val actualSet = mutableSetOf<GraphStructure.Edge<Int>>()
        val expectedSet = mutableSetOf<GraphStructure.Edge<Int>>()

        for(id in 0..50) graph.addVertex(random[id],"${random[id]}")

        for(id in 0..24){
            for(idAdj in 25..50){
                graph.addEdge(random[id],random[idAdj])
                expectedSet.add(graph.getEdge(random[id],random[idAdj])!!)
            }
        }


        val iterator = graph.edgesIterator()
        while(iterator.hasNext()) {
            val e = iterator.next()
            actualSet.add(e)
        }


        assertEquals(true,expectedSet.containsAll(actualSet))

    }

    private fun cmpEdges(edg1: GraphStructure.Edge<Int>?, edg2: GraphStructure.Edge<Int>?) = edg1?.id == edg2?.id && edg1?.adjacent == edg2?.adjacent

}