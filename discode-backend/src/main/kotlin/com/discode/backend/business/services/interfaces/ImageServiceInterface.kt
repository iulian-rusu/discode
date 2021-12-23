package com.discode.backend.business.services.interfaces

import org.springframework.core.io.UrlResource

interface ImageServiceInterface {
    fun imagePathFor(userId: Long): String
    fun saveImage(userId: Long, imageBytes: ByteArray): String
    fun loadImage(imagePath: String): UrlResource?
    fun deleteImage(imagePath: String)
}