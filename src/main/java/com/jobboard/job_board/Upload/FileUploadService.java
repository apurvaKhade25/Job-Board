package com.jobboard.job_board.Upload;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.jobboard.job_board.Config.CloudinaryConfig;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class FileUploadService {

    private final Cloudinary cloudinary;

    public String uploadResume(MultipartFile file){
        if(file.isEmpty()){
            throw new RuntimeException("File is empty");
        }

        String ContentType= file.getContentType();
        if(ContentType==null||!ContentType.equals("applications/pdf")) {
            throw new RuntimeException("Only pdf files are allowed");
        }
        if (file.getSize()>5*1024*1024){
            throw new RuntimeException("File should be less than 5MB");
        }

        try {
            Map UploadResult=cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "folder","resumes",
                            "resource_type","raw",
                            "format","pdf",
                            "public_id","resume_"+System.currentTimeMillis()
                    ));
            return UploadResult.get("secure_url").toString();

        } catch (IOException e) {
            throw new RuntimeException("Upload failed"+e.getMessage());
        }
    }



}
