package TrainsAndStations

import scala.util.Try

case class Station(number: Int)

case class Path(st1: Station, st2: Station, length: Int)

case class Route(list: List[Station])

//left - steps to future station, visited - number of visited stations
class Train(val number: Int, val stations: Route, val route: List[Path], var future: Try[Station],
            var current: Try[Station], var left: Int, var visited: Int, var done: Boolean)

case class Network(set: Set[Path])