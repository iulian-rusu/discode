package com.discode.backend.interfaces

interface ImageStorageInterface {
    fun saveImage(userId: Long, imageBytes: ByteArray): String
    fun loadImage(imagePath: String): ByteArray?
    fun deleteImage(imagePath: String)
}