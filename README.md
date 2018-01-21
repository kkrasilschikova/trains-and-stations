# trains-and-stations
Define if and where trains crash based on input.

### Assumptions:
1) Minimum 2 trains.
2) For each train minimum path is 2 stations (the first is where the train begins its journey).
3) Trains should start from different stations.
4) If trains crash or reach their final destinations, they disappear from the network.
5) All input is done in Preparation class:
* Path is 2 stations and length in between, for example `val path1 = Path(Station(1), Station(2), 4)`
* Route is a list of stations for 1 train, for example `val r1 = Route(List(Station(1), Station(2), Station(3)))`
6) If output is `crash of trains 2 and 3 at Station(2)`, it means trains defined by val r2 and val r3 in Preparation class.

### In order to run the program
Build project and start main method in Main object.
