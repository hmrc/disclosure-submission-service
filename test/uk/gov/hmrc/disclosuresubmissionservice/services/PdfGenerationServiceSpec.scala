/*
 * Copyright 2022 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package services

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper

class PdfGenerationServiceSpec extends AnyWordSpec with Matchers with GuiceOneAppPerSuite {

  private lazy val service: PdfGenerationService = app.injector.instanceOf[PdfGenerationService]
  "Pdf Generation Service" should {
    "must match with the 'submission.pdf' template" in {
      import java.nio.file.{Paths, Files}

      val result = service.generate    
      //Files.write(Paths.get("submission.pdf"), result)


      val pdfPath          = Paths.get("submission.pdf")
      val pdf: Array[Byte] = Files.readAllBytes(pdfPath)

      val pdfDocument: PDDocument         = PDDocument.load(service.generate)
      val expectedPdfDocument: PDDocument = PDDocument.load(pdf)

      try {
        val pdfData         = new PDFTextStripper().getText(pdfDocument)
        val expectedPdfData = new PDFTextStripper().getText(expectedPdfDocument)
        pdfData shouldBe expectedPdfData
      } finally {
        pdfDocument.close()
        expectedPdfDocument.close()
      }
    }
  }

}
