package com.groupseven.sysc4806project;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ImageService {
    private static final String IMAGE_DIR = "book-img/";
    public static final File DEFAULT_COVER = new File(IMAGE_DIR + "default.png");

    public boolean coverExists(int bookId) {
        Path imagePath = Paths.get(IMAGE_DIR + bookId + ".png");
        return Files.exists(imagePath);
    }

    public File getImageFile(int bookId) {
        return new File(IMAGE_DIR + bookId + ".png");
    }

    public boolean writeImage(int bookId, MultipartFile image) {
        Path savePath = Paths.get(IMAGE_DIR + bookId + ".png");

        try (InputStream is = image.getInputStream()) {
            Files.copy(is, savePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean removeImage(int bookId) {
        if (coverExists(bookId)) {
            try {
                Path savePath = Paths.get(IMAGE_DIR + bookId + ".png");
                Files.delete(savePath);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
