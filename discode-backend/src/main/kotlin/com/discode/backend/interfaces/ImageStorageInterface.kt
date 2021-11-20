package com.discode.backend.interfaces

interface ImageStorageInterface {
    fun saveImage(imageBytes: ByteArray): String
    fun loadImage(imagePath: String): ByteArray
}