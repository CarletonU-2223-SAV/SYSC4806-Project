package com.groupseven.sysc4806project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;

@Controller
@RequestMapping("book-img")
public class ImageController {
    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping( value = "/{bookId}", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public ResponseEntity<InputStreamResource> cover(@PathVariable Integer bookId) throws IOException {
        File file;
        if (imageService.coverExists(bookId)) {
            file = imageService.getImageFile(bookId);
        } else {
            file = ImageService.DEFAULT_COVER;
        }

        return ResponseEntity.ok()
            .contentLength(file.length())
            .contentType(MediaType.IMAGE_PNG)
            .body(new InputStreamResource(new FileInputStream(file)));
    }
}
