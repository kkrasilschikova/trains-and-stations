# trains-and-stations
Define if and where trains crash based on input of paths and routes.

### Assumptions:
1) If trains crash or reach their final destinations, they disappear from the network.
2) All input is done through provided file, example

```{"path":{"start":1, "finish":2, "length":5}}  
 {"path":{"start":2, "finish":3, "length":4}}  
 {"path":{"start":3, "finish":4, "length":2}}  
 {"path":{"start":2, "finish":5, "length":5}}  
 {"path":{"start":5, "finish":6, "length":3}}  
 {"path":{"start":1, "finish":6, "length":5}}  
 {"path":{"start":4, "finish":5, "length":4}}  
 {"train":[6,1,2,5,6]}  
 {"train":[5,2,3]}  
 {"train":[1,2,5]}  
 {"train":[4,3,2]}
 ```
 
 * Every entry should be on a separate line
 * Path is 2 stations and length in between defined as `{"path":{"start":1, "finish":2, "length":5}}`
 * Route is a list of stations for 1 train defined as `{"train":[5,2,3]}`
 3) The order in which trains are specified in the file defines train number.

### In order to run the program
Build project and start main method in Main object with provided file as an argument.  
For example in sbt console `run ".\\trains.txt"`  
(trains.txt file is included in the project as an example)
