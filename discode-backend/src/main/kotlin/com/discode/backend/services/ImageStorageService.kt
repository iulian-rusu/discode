package com.discode.backend.services

import com.discode.backend.interfaces.ImageStorageInterface
import org.springframework.stereotype.Service

@Service
class ImageStorageService : ImageStorageInterface {
    override fun saveImage(imageBytes: ByteArray): String {
        return "/todo/handle/images"
    }

    override fun loadImage(imagePath: String): ByteArray {
        return byteArrayOf()
    }
}