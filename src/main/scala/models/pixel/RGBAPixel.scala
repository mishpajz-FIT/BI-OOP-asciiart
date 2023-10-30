package models.pixel

case class RGBAPixel(override val r: Int, override val g: Int, override val b: Int, a: Int) extends RGBPixel(r, g, b) {

}
