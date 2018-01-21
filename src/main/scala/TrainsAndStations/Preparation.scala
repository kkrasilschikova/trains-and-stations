package TrainsAndStations

import scala.collection.mutable.ArrayBuffer
import scala.util.Try

class Preparation {
  //input - paths and routes
  val path1 = Path(Station(1), Station(2), 4)
  val path2 = Path(Station(2), Station(3), 3)
  val path3 = Path(Station(3), Station(4), 2)
  val path4 = Path(Station(2), Station(5), 5)
  val paths: List[Path] = List(path1, path2, path3, path4)

  val r1 = Route(List(Station(1), Station(2), Station(3)))
  val r2 = Route(List(Station(5), Station(2), Station(3), Station(4)))
  val r3 = Route(List(Station(3), Station(2), Station(1)))
  val routes: List[Route] = List(r1, r2, r3)

  //building the whole network
  def reverse(path: Path) = Path(path.st2, path.st1, path.length)
  val n = Network((paths ++ paths.map(x => reverse(x))).toSet)

  //grouped by 2 stations
  def groupByTwo(route: Route): List[List[Station]] = route.list.sliding(2,1).toList

  /*val lists1: List[List[Station]] = groupByTwo(r1)
  val lists2: List[List[Station]] = groupByTwo(r2)
  val lists3: List[List[Station]] = groupByTwo(r3)*/
  val lists: List[List[List[Station]]] = routes.map(x => groupByTwo(x))

  //groups of 2 stations and path in between
  def fullPath(list: List[List[Station]]): List[Path] = list.flatMap(elem =>
    n.set.filter(path => path.st1 == elem.head && path.st2 == elem(1)))

  /*val richRoute1: List[Path] = fullPath(lists1)
  val richRoute2: List[Path] = fullPath(lists2)
  val richRoute3: List[Path] = fullPath(lists3)*/
  val richRoutes: List[List[Path]] = lists.map(x => fullPath(x))

  //define time for every route and max time
  def timeOfRoute(route: List[Path]): Int = route.map(x => x.length).sum
  /*val timeRoute1: Int = timeOfRoute(richRoute1)
  val timeRoute2: Int = timeOfRoute(richRoute2)
  val timeRoute3: Int = timeOfRoute(richRoute3)*/
  val timeRoutes: List[Int] = richRoutes.map(x => timeOfRoute(x))

  val maxTime: Int = timeRoutes.max

  /*val t1 = new Train(r1.list.head.number, r1, richRoute1, Try(r1.list.tail.head),
    Try(r1.list.head), richRoute1.head.length, 1, false)
  val t2 = new Train(r2.list.head.number, r2, richRoute2,  Try(r2.list.tail.head),
    Try(r2.list.head), richRoute2.head.length, 1, false)
  val t3 = new Train(r3.list.head.number, r3, richRoute3,  Try(r3.list.tail.head),
    Try(r3.list.head), richRoute3.head.length, 1, false)*/

  val allTrains: ArrayBuffer[Train] = new ArrayBuffer[Train]
  for (i <- routes.indices)
    allTrains += new Train(number = routes(i).list.head.number, stations = routes(i),
      route = richRoutes(i), future = Try(routes(i).list.tail.head), current = Try(routes(i).list.head),
      left = richRoutes(i).head.length, visited = 1, done = false)

}