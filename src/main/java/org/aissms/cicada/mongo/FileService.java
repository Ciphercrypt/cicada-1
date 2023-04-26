package org.aissms.cicada.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

// store image with name = unique random string
// name will be used as file name in mongodb
@Service
public class FileService {
    
    @Autowired
    GridFsTemplate gridFsTemplate;

    public String storeFile(MultipartFile file) {
        String fileName = generateFileName(file);
        try {
            gridFsTemplate.store(file.getInputStream(), fileName);
        } catch(IOException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
        return fileName;
    }
    public Resource loadFileAsResource(String fileName) {
        return  gridFsTemplate.getResource(fileName);
    }
    private String getUniqueString() {
        return UUID.randomUUID().toString();
    }
    private String generateFileName(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        System.out.println(originalFileName + " : original");
        // String extention = originalFileName.substring(originalFileName.lastIndexOf("."));
        String extention = ".png";
        return getUniqueString() + extention;
    }
}
