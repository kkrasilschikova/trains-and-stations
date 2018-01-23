package unit

import TrainsAndStations.{Route, Station}
import org.scalatest.{FeatureSpec, GivenWhenThen}

class TestRoutesInput extends FeatureSpec with GivenWhenThen {
  feature("Test not common routes input"){
    scenario("We want to get an empty list if trains have 1 or 0 stations"){
      Given("2 trains with 1 and 0 stations")
      val train1 = Route(List(Station(4)))
      val train2 = Route(List())

      When("we filter the list of routes")
      val routes = List(train1, train2).filter(_.list.length >= 2)

      Then("we get an empty list because trains don't move")
      assert(routes.isEmpty)
    }

    scenario("We want to exit if there are no trains") {
      Given("no trains")

      When("we filter an empty list")
      val routes = List.empty[Route].filter(_.list.length >= 2)

      Then("we exit the program because there is nothing to process")
      if (routes.isEmpty) println("There are no moving trains, exit")
    }
  }

}