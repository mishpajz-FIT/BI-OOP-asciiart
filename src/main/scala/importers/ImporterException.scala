package importers

final case class ImporterException(private val message: String)
    extends Exception(message)
