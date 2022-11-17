import models.Factory
import utils.AppUtil

object EntryPoint extends App with AppUtil {
  private val sensorReportService = Factory.sensorReportService
  private val sensorStatsService = Factory.sensorStatsService

  /**
   * load and process each report file iteratively to generate sensor measurement model.
   * Also, collects errors, if any.
   */
  val (sensorMeasurementsWithErrors, numberOfFilesProcessed) = sensorReportService.getSensorMeasurements

  /**
   * gather statistics
   */
  val sensorStats = sensorStatsService.getStatistics(extractValidSensorMeasurements(sensorMeasurementsWithErrors), numberOfFilesProcessed)

  val serviceExceptions = extractServiceExceptions(sensorMeasurementsWithErrors)

  /**
   * print statistics to stdout
   */
  printSensorStats(sensorStats)

  /**
   * printing service exceptions, if any
   */
  printServiceExceptions(serviceExceptions)
}
