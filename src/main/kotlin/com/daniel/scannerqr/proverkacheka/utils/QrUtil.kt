package com.daniel.scannerqr.proverkacheka.utils

import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.NotFoundException
import com.google.zxing.client.j2se.BufferedImageLuminanceSource
import com.google.zxing.common.HybridBinarizer
import org.bytedeco.opencv.global.opencv_imgcodecs.imread
import org.bytedeco.opencv.opencv_objdetect.QRCodeDetector
import org.springframework.web.multipart.MultipartFile
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.nio.file.Files
import javax.imageio.ImageIO

object QrUtils {

    fun enhanceImage(image: BufferedImage): BufferedImage {
        val gray = BufferedImage(image.width, image.height, BufferedImage.TYPE_BYTE_GRAY)
        val g: Graphics2D = gray.createGraphics()
        g.drawImage(image, 0, 0, null)
        g.dispose()

        val scaled = BufferedImage(gray.width * 2, gray.height * 2, BufferedImage.TYPE_BYTE_GRAY)
        scaled.graphics.drawImage(gray, 0, 0, scaled.width, scaled.height, null)
        return scaled
    }

    fun decodeQrWithZXing(buffered: BufferedImage): String? {
        val source = BufferedImageLuminanceSource(buffered)
        val bitmap = BinaryBitmap(HybridBinarizer(source))
        return try {
            MultiFormatReader().apply {
                setHints(mapOf(
                    DecodeHintType.TRY_HARDER to true,
                    DecodeHintType.PURE_BARCODE to false
                ))
            }.decode(bitmap).text
        } catch (_: NotFoundException) {
            null
        }
    }

    fun decodeWithOpenCV(fileBytes: ByteArray): String? {
        val tempFile = Files.createTempFile("qr", ".jpg").toFile()
        tempFile.writeBytes(fileBytes)

        val image = imread(tempFile.absolutePath)
        if (image.empty()) {
            println("OpenCV: изображение пустое")
            return null
        }

        val qrDecoder = QRCodeDetector()
        val resultPointer = qrDecoder.detectAndDecode(image)

        if (resultPointer == null || resultPointer.string.isNullOrBlank()) {
            println("OpenCV не смог распознать QR")
            return null
        }

        val result = resultPointer.string
        println("OpenCV QR: $result")
        return result
    }

    fun decodeQrFromImage(file: MultipartFile): String? {
        val raw = ImageIO.read(file.inputStream) ?: return null
        val pre = enhanceImage(raw)

        val zxingResult = decodeQrWithZXing(pre)
        if (zxingResult != null) return zxingResult

        println("ZXing не смог отсканировать QR")
        return decodeWithOpenCV(file.bytes)
    }
}