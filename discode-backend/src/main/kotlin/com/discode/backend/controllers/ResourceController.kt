package com.discode.backend.controllers

import com.discode.backend.interfaces.ImageServiceInterface
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.MediaTypeFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/api")
class ResourceController {
    @Autowired
    private lateinit var imageStorageService: ImageServiceInterface

    @GetMapping("/images/{imagePath}")
    fun getImage(@PathVariable imagePath: String): ResponseEntity<Any> {
        return imageStorageService.loadImage(imagePath)?.let {
            ResponseEntity.ok()
                .contentType(MediaTypeFactory.getMediaType(it).orElse(MediaType.APPLICATION_OCTET_STREAM))
                .body(it)
        } ?: ResponseEntity.notFound().build()
    }
}