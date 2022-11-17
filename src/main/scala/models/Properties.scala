package models

import com.typesafe.config.ConfigFactory

class Properties {
  private val config = ConfigFactory.load

  val reportsPath: String = config.getString("REPORTS_PATH")
}
