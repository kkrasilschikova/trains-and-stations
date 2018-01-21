package TrainsAndStations

import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import scala.util.{Failure, Success, Try}

object Main {
  val prep = new Preparation

  def main(args: Array[String]): Unit = go(prep.allTrains)

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
    else {
      train.done = true
      println(s"train ${train.number} finished")
    }
  }

  //break array of Train into pairs
  def toPairs(trains: ArrayBuffer[Train]): ListBuffer[(Train, Train)] = {
    var pairs = ListBuffer[(Train, Train)]()
    for (i <- trains)
      for (j <- trains)
        if (i != j && !pairs.contains((j, i))) pairs += ((i, j))
    pairs
  }

  var maxTime: Int = prep.maxTime

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
  }

}