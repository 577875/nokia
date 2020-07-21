package com.example.demo.rest;

import com.example.demo.service.DriveDownload;
import com.example.demo.service.DriveUpload;
import com.google.api.services.drive.model.File;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RequestMapping("/api")
@RestController
@EnableSwagger2
public class Controller
{
    @Autowired
    DriveUpload fileStoreService;

    @Autowired
    DriveDownload driveDownload;

    @RequestMapping(value = "/upload/key/{key}", method = RequestMethod.POST)
    @ApiOperation(value = "Upload configuration data for a given key and override type",
            notes = "If configuration data exists then override data otherwise create a new configuration ",
            response = String.class)
    public ResponseEntity upload(@RequestParam("file") MultipartFile file,
                                 @ApiParam(value = "Key of the object ", required = true) @PathVariable("key") String key)
    {
        ResponseEntity responseEntity = null;
        try
        {
            fileStoreService.upload(file,key);
            responseEntity = new ResponseEntity<>(null, HttpStatus.OK);
        }
        catch (Exception e)
        {
            responseEntity = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @RequestMapping(value = "/download/key/{key}", method = RequestMethod.GET)
    @ApiOperation(value = "Upload configuration data for a given key and override type",
            notes = "If configuration data exists then override data otherwise create a new configuration ",
            response = String.class)
    public ResponseEntity download(@ApiParam(value = "Key of the object ", required = true) @PathVariable("key") String key)
    {
        ResponseEntity<File> responseEntity = null;
        try
        {
            File downloadfile = driveDownload.download(key);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", key);
            responseEntity = ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + key)
                    .contentType(MediaType.parseMediaType("application/zip"))
                    .body(downloadfile);
        }
        catch (Exception e)
        {
            responseEntity = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiOperation(value = "Upload configuration data for a given key and override type",
            notes = "If configuration data exists then override data otherwise create a new configuration ",
            response = String.class)
    public ResponseEntity listFiles() throws IOException, GeneralSecurityException {
        ResponseEntity<List<File>> responseEntity = null;
        try
        {
            List<File> files = driveDownload.listFiles();
            responseEntity = new ResponseEntity<>(files, HttpStatus.OK);
        }
        catch (Exception e)
        {
            responseEntity = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
}
