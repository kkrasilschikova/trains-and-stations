package TrainsAndStations

import play.api.libs.json.JsValue

trait JsonReaderRoute[A] {
  def read(value: A): Route
}

object JsonReaderRouteInstances {
  implicit val routeReader: JsonReaderRoute[JsValue] = new JsonReaderRoute[JsValue] {
    def read(value: JsValue): Route = {
      val stations = (value \ "train").as[List[Int]] match {
        case list => list.map(x => Station(x))
      }
      Route(stations)
    }
  }
}

object JsonReaderRouteSyntax {
  implicit class JsonReaderOps[A](value: A) {
    def toRoute(implicit r: JsonReaderRoute[A]): Route =
      r.read(value)
  }
}