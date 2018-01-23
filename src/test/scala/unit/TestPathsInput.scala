package unit

import TrainsAndStations._
import org.scalatest.{FeatureSpec, GivenWhenThen}

class TestPathsInput extends FeatureSpec with GivenWhenThen {
  feature("Test not common paths input") {
    scenario("We want to get network made by only one path") {
      Given("1 path and routes for different stations")
      val path1 = Path(Station(1), Station(2), 5)
      val paths = List(path1)
      val train1 = Route(List(Station(6), Station(1), Station(2), Station(5), Station(6)))
      val train2 = Route(List(Station(5), Station(2), Station(3)))
      val routes = List(train1, train2)

      When("we try to construct the network")
      val n = Network((paths ++ paths.map(x => Main.reverse(x))).toSet)

      Then("we get successful network made of 1 path")
      val lists: List[List[List[Station]]] = routes.map(x => Main.groupByTwo(x))

      def fullPath(list: List[List[Station]]): List[Path] = list.flatMap(elem =>
        n.set.filter(path => path.st1 == elem.head && path.st2 == elem(1)))
      val richRoutes: List[List[Path]] = lists.map(x => fullPath(x))
      println(richRoutes)
    }
  }
}
