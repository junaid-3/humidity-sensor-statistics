# Humidity Sensor Statistics

Reads multiple sensor reports available in .CSV format and gathers humidity statistics

Statistics calculated based on below requirements 

  * Multiple reports are contained in a directory, which acts as input to the application
  * Directory path is maintained in `application.conf` and is configurable
  * Each report in directory contains large amount of measurement data arranged in `sensor-id,humidity` format
  * A sensor can report measurement in multiple reports
  * Humidity measurements are sorted by highest average humidity
  * Completely failed humidity measurements for a sensor displayed as `NaN`
  
To run, either run `EntryPoint.scala` directly from an IDE or compile and run `scala EntryPoint`

The statistics will print to STDOUT  
