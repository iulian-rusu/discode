package com.discode.backend.services

import com.discode.backend.interfaces.ImageStorageInterface
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.File
import javax.imageio.ImageIO
import javax.imageio.ImageReader

@Service
class ImageStorageService : ImageStorageInterface {
    companion object {
        private const val IMAGE_DIR = "images"
    }

    override fun saveImage(userId: Long, imageBytes: ByteArray): String {
        val bufferedImage = getBufferedImage(imageBytes)

        val userDirName = "user-$userId"
        val userDir = File("$IMAGE_DIR/$userDirName")
        if (!userDir.exists())
            userDir.mkdir()
        val imagePath = "$userDirName/avatar.png"
        val imageFile = File("$IMAGE_DIR/$imagePath")
        imageFile.createNewFile()
        ImageIO.write(bufferedImage, "png", imageFile)
        return imagePath
    }

    override fun loadImage(imagePath: String): ByteArray? {
        val urlResource = UrlResource("file:$IMAGE_DIR/$imagePath")
        if (urlResource.exists())
            return urlResource.file.readBytes()
        return null
    }

    override fun deleteImage(imagePath: String) {
        val imageFile = File("$IMAGE_DIR/$imagePath")
        imageFile.delete()
    }

    private fun getBufferedImage(imageBytes: ByteArray): BufferedImage {
        val byteInputStream = ByteArrayInputStream(imageBytes)
        val reader = ImageIO.getImageReadersByFormatName("png").next() as ImageReader
        val imgInputStream = ImageIO.createImageInputStream(byteInputStream)
        reader.setInput(imgInputStream, true)

        val image: Image = reader.read(0, reader.defaultReadParam)
        val bufferedImage = BufferedImage(
            image.getWidth(null),
            image.getHeight(null),
            BufferedImage.TYPE_INT_RGB
        )

        val g2d = bufferedImage.createGraphics()
        g2d.drawImage(image, null, null)

        return bufferedImage
    }
}