package com.jobboard.job_board.Upload;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "File Upload", description = "Resume upload to Cloudinary")
@RequestMapping("/api/upload")
public class FileUploadController {
    private final FileUploadService fileUploadService;

    @Operation(summary = "Upload resume PDF",
            description = "APPLICANT only — max 5MB PDF")
    @PreAuthorize("hasRole('APPLICANT')")
    @PostMapping("/resume")
    public ResponseEntity <Map<String,String>> uploadResume(@RequestParam("file") MultipartFile file){
        String resumeUrl= fileUploadService.uploadResume(file);
        return ResponseEntity.ok(Map.of("url",resumeUrl,"message","Resume uploaded successfully"));

    }
}
