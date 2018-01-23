package integration

import TrainsAndStations.Main._
import TrainsAndStations._
import org.scalatest.{FeatureSpec, GivenWhenThen}

import scala.collection.mutable.ArrayBuffer
import scala.util.Try

class TestTrainsModel extends FeatureSpec with GivenWhenThen {
  feature("Test the model") {
    scenario("We want to verify correctly defined model") {
      Given("correctly defined 4 paths between stations and 2 routes of trains")
      val path1 = Path(Station(1), Station(2), 4)
      val path2 = Path(Station(2), Station(3), 7)
      val path3 = Path(Station(2), Station(4), 2)
      val paths = List(path1, path2, path3)

      val train1 = Route(List(Station(4), Station(2), Station(3)))
      val train2 = Route(List(Station(3), Station(2)))
      val routes = List(train1, train2).filter(_.list.length >= 2)
      if (routes.isEmpty) {println("There are no moving trains, exit"); System.exit(0)}

      When("we form custom ArrayBuffer[Train]")
      val n = Network((paths ++ paths.map(x => Main.reverse(x))).toSet)
      val lists: List[List[List[Station]]] = routes.map(x => Main.groupByTwo(x))

      def fullPath(list: List[List[Station]]): List[Path] = list.flatMap(elem =>
        n.set.filter(path => path.st1 == elem.head && path.st2 == elem(1)))

      val richRoutes: List[List[Path]] = lists.map(x => fullPath(x))
      val timeRoutes: List[Int] = richRoutes.map(x => Main.timeOfRoute(x))
      var maxTime: Int = timeRoutes.max

      val allTrains: ArrayBuffer[Train] = new ArrayBuffer[Train]
      for (i <- routes.indices)
        allTrains += new Train(number = i + 1, stations = routes(i),
          route = richRoutes(i), future = Try(routes(i).list.tail.head), current = Try(routes(i).list.head),
          left = richRoutes(i).head.length, visited = 1, done = false)

      Then("after calling go method we get correct printed output about crash")
      //crash of trains 1 and 2 between Station(3) and Station(2)
      def go(trains: ArrayBuffer[Train]): Unit = {
        while (maxTime >= 0 && trains.length > 1) {
          trains.foreach(step)
          //remove if trains on the same path or station
          var pairs = toPairs(trains)
          for (pair <- pairs) {
            if (theSamePath(pair._1, pair._2)) {
              println(s"crash of trains ${pair._1.number} and ${pair._2.number} " +
                s"between ${pair._1.future.get} and ${pair._2.future.get}")
              trains -= pair._1
              trains -= pair._2
            }
          }
          pairs = toPairs(trains)
          for (pair <- pairs) {
            if (theSameStation(pair._1, pair._2)) {
              println(s"crash of trains ${pair._1.number} and ${pair._2.number} " +
                s"at ${pair._1.future.get}")
              trains -= pair._1
              trains -= pair._2
            }
          }
          //remove the train from array if it is done
          trains --= trains.filter(_.done == true)
          maxTime -= 1
        }
        if (trains.length ==1 ) println(s"Just train ${trains.head.number} left, it will finish its journey successfully")
      }
      go(allTrains)
    }

    scenario("Test the model with only one train provided") {
      Given("correctly defined paths between stations and just 1 route for 1 train")
      val path1 = Path(Station(1), Station(2), 4)
      val path2 = Path(Station(2), Station(3), 7)
      val path3 = Path(Station(2), Station(4), 2)
      val paths = List(path1, path2, path3)

      val train1 = Route(List(Station(4), Station(2), Station(3)))
      val routes = List(train1).filter(_.list.length >= 2)
      if (routes.isEmpty) {println("There are no moving trains, exit"); System.exit(0)}

      When("we form custom ArrayBuffer[Train]")
      val n = Network((paths ++ paths.map(x => Main.reverse(x))).toSet)
      val lists: List[List[List[Station]]] = routes.map(x => Main.groupByTwo(x))

      def fullPath(list: List[List[Station]]): List[Path] = list.flatMap(elem =>
        n.set.filter(path => path.st1 == elem.head && path.st2 == elem(1)))

      val richRoutes: List[List[Path]] = lists.map(x => fullPath(x))
      val timeRoutes: List[Int] = richRoutes.map(x => Main.timeOfRoute(x))
      var maxTime: Int = timeRoutes.max

      val allTrains: ArrayBuffer[Train] = new ArrayBuffer[Train]
      for (i <- routes.indices)
        allTrains += new Train(number = i + 1, stations = routes(i),
          route = richRoutes(i), future = Try(routes(i).list.tail.head), current = Try(routes(i).list.head),
          left = richRoutes(i).head.length, visited = 1, done = false)

      Then("after calling go method we get 'Just train 1 left, it will finish its journey successfully'")
      def go(trains: ArrayBuffer[Train]): Unit = {
        while (maxTime >= 0 && trains.length > 1) {
          trains.foreach(step)
          //remove if trains on the same path or station
          var pairs = toPairs(trains)
          for (pair <- pairs) {
            if (theSamePath(pair._1, pair._2)) {
              println(s"crash of trains ${pair._1.number} and ${pair._2.number} " +
                s"between ${pair._1.future.get} and ${pair._2.future.get}")
              trains -= pair._1
              trains -= pair._2
            }
          }
          pairs = toPairs(trains)
          for (pair <- pairs) {
            if (theSameStation(pair._1, pair._2)) {
              println(s"crash of trains ${pair._1.number} and ${pair._2.number} " +
                s"at ${pair._1.future.get}")
              trains -= pair._1
              trains -= pair._2
            }
          }
          //remove the train from array if it is done
          trains --= trains.filter(_.done == true)
          maxTime -= 1
        }
        if (trains.length ==1 ) println(s"Just train ${trains.head.number} left, it will finish its journey successfully")
      }
      go(allTrains)
    }
  }

}