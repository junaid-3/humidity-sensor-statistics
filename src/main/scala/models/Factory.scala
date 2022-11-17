package models

import services.{SensorReportService, SensorStatsService}

object Factory {
  def sensorReportService: SensorReportService = new SensorReportService()

  def sensorStatsService: SensorStatsService = new SensorStatsService()

  def properties: Properties = new Properties()
}
