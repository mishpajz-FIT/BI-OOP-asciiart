package utilities

import java.io.File

/**
  * Utilities for [[File]].
  * 
  */
object FileUtilities {

  implicit class FileExtensions(file: File) {

    /**
      * Get the extension of a [[File]].
      *
      * Extension is considered the part of the [[File]] name after the last dot.
      * If the [[File]] does not have an extension, an empty [[String]] is returned.
      * 
      * @return extension of the [[File]] as a string
      */
    def getExtension: String = {
      val fileName = file.getName

      if (!fileName.contains("."))
        return ""

      val extensionIndex = fileName.lastIndexOf('.') + 1
      fileName.substring(extensionIndex)
    }
  }
}
