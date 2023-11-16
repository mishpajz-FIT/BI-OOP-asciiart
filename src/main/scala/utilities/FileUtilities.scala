package utilities

import java.io.File

object FileUtilities {
  implicit class FileExtensions(file: File) {
    def getExtension: String = {
      val fileName = file.getName

      if (!fileName.contains("."))
        return ""

      val extensionIndex = fileName.lastIndexOf('.') + 1
      fileName.substring(extensionIndex)
    }
  }
}
