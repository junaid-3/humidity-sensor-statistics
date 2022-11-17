import models.Errors.ServiceException
import models.Factory
import models.Models.SensorMeasurement
import services.SensorReportService.SensorMeasurementEither
import utils.StdOutUtil

object EntryPoint extends App with StdOutUtil {
  private val reportsPath = Factory.properties.reportsPath
  private val sensorReportService = Factory.sensorReportService
  private val sensorStatsService = Factory.sensorStatsService

  /**
   * load and process each report file iteratively to generate sensor measurement model.
   * Also, collects errors while reading, if any.
   */
  val (sensorMeasurementsWithErrors, numberOfFilesProcessed) = sensorReportService.getSensorMeasurements(reportsPath)

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

  private def extractValidSensorMeasurements(sensorMeasurements: Seq[SensorMeasurementEither]): Seq[SensorMeasurement] =
    sensorMeasurements.collect({ case Right(sm) => sm })

  private def extractServiceExceptions(sensorMeasurements: Seq[SensorMeasurementEither]): Seq[ServiceException] =
    sensorMeasurements.collect({ case Left(ex) => ex })
}
