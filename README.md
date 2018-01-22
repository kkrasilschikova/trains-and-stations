# trains-and-stations
Define if and where trains crash based on input of paths and routes.

### Assumptions:
1) If trains crash or reach their final destinations, they disappear from the network.
2) All input is done in Preparation class:
* Path is 2 stations and length in between, for example `val path1 = Path(Station(1), Station(2), 4)`
* Route is a list of stations for 1 train, for example `val train1 = Route(List(Station(1), Station(2), Station(3)))`

### In order to run the program
Build project and start main method in Main object.