package com.dpmg.patrimonio.utils;

import com.dpmg.patrimonio.exceptions.FailedImportException;
import lombok.experimental.UtilityClass;
import org.apache.poi.util.IOUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@UtilityClass
public class FileBase64 {
    public String encodeFileToBase64(MultipartFile file) {
        try {
            byte[] fileBytes = IOUtils.toByteArray(file.getInputStream());
            return Base64.getEncoder().encodeToString(fileBytes);
        } catch (IOException e) {
            throw new FailedImportException();
        }
    }

    public MultipartFile decodeBase64ToFile(String base64, String originalFilename, String contentType) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64);
            InputStream inputStream = new ByteArrayInputStream(decodedBytes);
        } catch (IOException e) {
            throw new FailedImportException();
        }
    }
}
