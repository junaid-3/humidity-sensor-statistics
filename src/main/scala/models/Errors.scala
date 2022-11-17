package models

object Errors {

  sealed trait ServiceException { val message: String }

  case class COULD_NOT_READ_REPORT_LINE(message: String) extends ServiceException

  case class COULD_NOT_READ_MEASUREMENT(message: String) extends ServiceException

}
