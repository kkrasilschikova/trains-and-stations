package TrainsAndStations

import java.io.File

import play.api.libs.json.Json

import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import scala.util.{Failure, Success, Try}

import JsonReaderPathInstances._
import JsonReaderPathSyntax._

object Main{
  def main(args: Array[String]): Unit = {
    if (args.isEmpty) println("Please specify source file")
    else {
      val filepath = args(0)

      if (new File(filepath).exists() && new File(filepath).isFile) {
        val file = io.Source.fromFile(filepath)
        val lines = file.getLines().toList

        val paths: List[Path] = lines.map(x => Try(Json.parse(x))
          .flatMap(y => Try(y.toPath))).filter(_.isSuccess).map(_.get)

        import JsonReaderRouteInstances._
        import JsonReaderRouteSyntax._

        val routes: List[Route] = lines.map(x => Try(Json.parse(x))
          .flatMap(y => Try(y.toRoute))).filter(_.isSuccess).map(_.get)

        if (routes.isEmpty) println("There are no moving trains, exit")
        else {
          //building the whole network
          val n = Network((paths ++ paths.map(x => reverse(x))).toSet)
          val lists: List[List[List[Station]]] = routes.map(x => groupByTwo(x))

          //groups of 2 stations and path in between
          def fullPath(list: List[List[Station]]): List[Path] = list.flatMap(elem =>
            n.set.filter(path => path.st1 == elem.head && path.st2 == elem(1)))

          val richRoutes: List[List[Path]] = lists.map(x => fullPath(x))

          val timeRoutes: List[Int] = richRoutes.map(x => timeOfRoute(x))
          val maxTime: Int = timeRoutes.max

          val allTrains: ArrayBuffer[Train] = new ArrayBuffer[Train]
          for (i <- routes.indices)
            allTrains += new Train(number = i + 1, stations = routes(i),
              route = richRoutes(i), future = Try(routes(i).list.tail.head), current = Try(routes(i).list.head),
              left = richRoutes(i).head.length, visited = 1, done = false)
          go(allTrains, maxTime)
        }
      }
      else println("Source file doesn't exist")
    }
  }

  def reverse(path: Path) = Path(path.st2, path.st1, path.length)

  //grouped by 2 stations
  def groupByTwo(route: Route): List[List[Station]] = route.list.sliding(2,1).toList

  //define time for every route
  def timeOfRoute(route: List[Path]): Int = route.map(x => x.length).sum

  def theSameStation(train1: Train, train2: Train): Boolean =
    train1.future == train2.future && train1.left == train2.left && train1.left == 1

  def theSamePath(train1: Train, train2: Train): Boolean = {
    train1.future == train2.current && train1.current == train2.future
  }

  def step(train: Train): Unit = {
    //if not all stations visited according to the path
    if (train.visited != train.stations.list.length) {
      //if train is on path (between the stations)
      if (train.left > 0) {
        train.left -= 1
        if (train.left == 0) {
          //change of station
          train.current = train.future
          train.visited += 1
          train.future = Try(train.stations.list(train.visited))
          train.left = train.future match {
            case Failure(_) => 0
            case Success(_) => train.route(train.visited - 1).length
          }
        }
      }
    }
    //train has finished its route
    else {train.done = true; println(s"train ${train.number} finished")}
  }

  //break array of Train into pairs
  def toPairs(trains: ArrayBuffer[Train]): ListBuffer[(Train, Train)] = {
    var pairs = ListBuffer[(Train, Train)]()
    for (i <- trains)
      for (j <- trains)
        if (i != j && !pairs.contains((j, i))) pairs += ((i, j))
    pairs
  }

  def go(trains: ArrayBuffer[Train], time: Int): Unit = {
    var maxTime = time
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
    if (trains.length == 1) println(s"just train ${trains.head.number} left, it will finish its journey successfully")
  }

}