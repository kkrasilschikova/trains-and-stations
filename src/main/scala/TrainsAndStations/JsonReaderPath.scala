package TrainsAndStations

import play.api.libs.json.JsValue

trait JsonReader[A] {
  def read(value: A): Path
}

object JsonReaderInstances {
  implicit val reader: JsonReader[JsValue] = new JsonReader[JsValue] {
    def read(value: JsValue): Path = {
      val st1 = (value \ "path" \ "start").as[Int] match {
        case start => Station(start)
      }
      val st2 = (value \ "path" \ "finish").as[Int] match {
        case finish => Station(finish)
      }
      val length = (value \ "path" \ "length").as[Int]
      Path(st1, st2, length)
    }
  }
}

object JsonReaderSyntax {
  implicit class JsonReaderOps[A](value: A) {
    def toPath(implicit r: JsonReader[A]): Path =
      r.read(value)
  }
}