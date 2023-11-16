package exporters.asciiimage.text

import java.io.{File, FileOutputStream}

class FileASCIIImageExporter(file: File)
    extends StreamASCIIImageExporter(new FileOutputStream(file)) {}
