import graphCollections.*


fun main(args : Array<String>){
    val network = GraphStructure<Int, VertexProperties?>()
    network.build(args[0])

    while(true){
        print("\nEnter command: ")
        val cmd = readln().trim().lowercase().split(" ")
        try{
            when(cmd[0]) {
                "degree" -> println(format(network.getDegree(cmd[1].toInt())))
                "smallestdistance" -> println(format(network.getSmallestDistance(cmd[1].toInt(),cmd[2].toInt())))
                "degrees" -> for(v in network.iterator()) println("V${v.id}: ${network.getDegree(v.id)}")
                "closeness" ->
                    try{ println(network.getCloseness(cmd[1].toInt())) }
                    catch(e:IndexOutOfBoundsException){
                        for(v in network.iterator()) println("V${v.id}: ${network.getCloseness(v.id)}")
                    }

                "exit" -> break
                "help" -> println("Commands:\n\n1.degree vertexId\n2.smallestDistance vertexId1 vertexId2\n" +
                        "3.closeness vertexId\n4.degrees\n5.closeness ")
                else -> println("Invalid Input.")
            }
        }
        catch(e:Exception){
            println("Invalid Input.")
        }
    }
}