package com.jobboard.job_board.Upload;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final Cloudinary cloudinary;

    public String uploadResume(MultipartFile file) {

        System.out.println("File name = " + file.getOriginalFilename());
        System.out.println("Content type = " + file.getContentType());
        System.out.println("Size = " + file.getSize());

        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        String ContentType = file.getContentType();
        if (ContentType == null || !ContentType.startsWith("application/pdf")) {
            throw new RuntimeException("Only pdf files are allowed");
        }
        if (file.getSize() > 5 * 1024 * 1024) {        //5MB in bytes
            throw new RuntimeException("File should be less than 5MB");
        }

        try {
            Map UploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "resumes",
                            "resource_type", "raw",
                            "format", "pdf",
                            "public_id", "resume_" + UUID.randomUUID()
                    ));
            System.out.println(UploadResult);
            return UploadResult.get("secure_url").toString();

        } catch (IOException e) {
            throw new RuntimeException("Upload failed" + e.getMessage());

        }

    }


}
