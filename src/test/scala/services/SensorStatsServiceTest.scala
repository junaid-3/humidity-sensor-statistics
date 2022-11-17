package services

import models.Models.{FailedSensorMeasurement, NaNSensorHumidityStat, RawSensorMeasurement, SensorHumidityStat, SensorStatistics, SuccessSensorMeasurement}
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class SensorStatsServiceTest extends AnyFunSpec with Matchers {
  describe("Sensor Stats Service") {
    val service = new SensorStatsService
    val sensorMeasurements = Seq(SuccessSensorMeasurement("s2", 80), FailedSensorMeasurement("s3", "NaN"),
      SuccessSensorMeasurement("s2", 78), SuccessSensorMeasurement("s1", 98), RawSensorMeasurement("s5", "1000"),
      SuccessSensorMeasurement("s1", 10), SuccessSensorMeasurement("s2", 88), FailedSensorMeasurement("s1", "NaN"))

    val actual = service.getStatistics(sensorMeasurements, 2)
    val expected = SensorStatistics(2, 8, 2, List(SensorHumidityStat("s2", 78, 82, 88), SensorHumidityStat("s1", 10, 54, 98),
      NaNSensorHumidityStat("s3", "NaN", "NaN", "NaN"), NaNSensorHumidityStat("s5", "NaN", "NaN", "NaN")))

    it("should return statistics") {
      actual shouldEqual expected
    }
  }
}
