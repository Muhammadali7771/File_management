package org.example.controller;

import org.example.daos.UploadsDao;
import org.example.domain.Uploads;
import org.example.dto.BookCreateDTO;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
public class UploadController {

    private final UploadsDao uploadsDao;
    private final Path rootPath = Path.of("C:\\Users\\Muhammadali\\Desktop\\files\\upload\\");

    public UploadController(UploadsDao uploadsDao) {
        this.uploadsDao = uploadsDao;
    }
    /*@GetMapping("/upload")
    @ResponseBody
    public String a(@RequestParam(name = "param1", required = false) String o,
                    @RequestParam(name = "param2", required = false) String o2){
        return "Server received " + o + ":" + o2;
    }*/

    /*@GetMapping("/download/{filename:.+}")
    @ResponseBody
    public String download(@PathVariable(name = "filename") String fileName){
        return "File with name : " + fileName + " Successfully downloaded";
    }*/

    @GetMapping("/upload")
    public String uploadPage(){
        return "upload";
    }

    @PostMapping("/upload")
    public String uploadFile(@ModelAttribute BookCreateDTO dto) throws IOException {
        /*String originalFilename = dto.getFile().getOriginalFilename();
        System.out.println("dto = " + dto);
        System.out.println("File uploaded " + originalFilename);
        String newName = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(originalFilename);
        Path path = Path.of("C:\\Users\\Muhammadali\\Desktop\\files\\upload", newName);
        Files.copy(dto.getFile().getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        return "redirect:/upload";*/

        for (MultipartFile file : dto.getFiles()) {
            String originalFilename = file.getOriginalFilename();
            System.out.println("File uploaded " + originalFilename);
            String newName = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(originalFilename);
            Path path = rootPath.resolve(newName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        }
        return "redirect:/upload";
    }

    @PostMapping("/upload2")
    public String upload2File(@RequestParam("file") MultipartFile file) throws IOException {
        Uploads uploads = Uploads.builder()
                .originalName(file.getOriginalFilename())
                .generatedName(UUID.randomUUID() + "." + StringUtils.getFilenameExtension(file.getOriginalFilename()))
                .size(file.getSize())
                .mimeType(file.getContentType())
                .build();
        uploadsDao.save(uploads);
        Files.copy(file.getInputStream(), rootPath.resolve(uploads.getGeneratedName()), StandardCopyOption.REPLACE_EXISTING);
        return "redirect:/upload";
    }


    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> downloadPage(@PathVariable("filename") String filename){
        Uploads uploads = uploadsDao.findByGeneratedName(filename);
        FileSystemResource fileSystemResource = new FileSystemResource(rootPath.resolve(filename));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(uploads.getMimeType()))
                .contentLength(uploads.getSize())
                .header("Content-Disposition", "attachment; filename = " + uploads.getOriginalName())
                .body(fileSystemResource);
    }

}
