package filters.image

import models.image.Image
import models.pixel.Pixel
import org.mockito.MockitoSugar.{mock, verify, when}
import org.scalatest.{FlatSpec, Matchers}

class CompoundedImageFilterSpecs extends FlatSpec with Matchers {
  behavior of "CompoundedImageFilter"

  it should "successfully filter with single filter" in {
    val mockFilter = mock[ImageFilter[Pixel]]

    val mockImage = mock[Image[Pixel]]

    when(mockFilter.transform(mockImage)).thenReturn(mockImage)

    val filter = CompoundedImageFilter(Seq(mockFilter))

    val result = filter.transform(mockImage)

    result shouldBe mockImage

    verify(mockFilter).transform(mockImage)
  }

  it should "successfully filter with multiple filters" in {
    val mockFilter1 = mock[ImageFilter[Pixel]]
    val mockFilter2 = mock[ImageFilter[Pixel]]
    val subfilters = Seq(mockFilter1, mockFilter2)

    val mockImage = mock[Image[Pixel]]

    when(mockFilter1.transform(mockImage)).thenReturn(mockImage)
    when(mockFilter2.transform(mockImage)).thenReturn(mockImage)

    val filter = CompoundedImageFilter(subfilters)

    val result = filter.transform(mockImage)

    result shouldBe mockImage

    verify(mockFilter1).transform(mockImage)
    verify(mockFilter2).transform(mockImage)
  }

  it should "do nothing with empty sequence of filters" in {
    val filter = CompoundedImageFilter(Seq.empty[ImageFilter[Pixel]])

    val mockImage = mock[Image[Pixel]]

    val result = filter.transform(mockImage)

    result shouldBe mockImage
  }
}
