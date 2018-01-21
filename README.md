# trains-and-stations
Define if and where trains crash based on input.

### Assumptions:
Minimum 2 trains, for each train minimum path is 2 stations (the first is where the train begins its journey).  
Trains start from different stations and named after the first station.
If trains crash or reach their final destinations, they disappear from the network.  
All input is done in Preparations class:  
* Path is 2 stations and length in between, for example `val path1 = Path(Station(1), Station(2), 4)`
* Route is a list of stations for 1 train, for example `val r1 = Route(List(Station(1), Station(2), Station(3)))`  


### In order to run the program
Start main method in Main object.
