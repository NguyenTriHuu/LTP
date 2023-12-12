package com.example.demo.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static com.amazonaws.HttpMethod.GET;

@Service
public class FileStore {
    private final AmazonS3 amazonS3Client;

    @Autowired
    public FileStore(AmazonS3 amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }


    /*public void save(String path,
                     String fileName,
                     Optional<Map<String,String>> optionalMetadata,
                     InputStream inputStream){
        ObjectMetadata metadata =new ObjectMetadata();
        optionalMetadata.ifPresent(map ->{
            if(!map.isEmpty()){
                map.forEach((key,value)->metadata.addUserMetadata(key,value));
            }
        });
        try{
            amazonS3Client.putObject(path,fileName,inputStream,metadata);
        }catch (AmazonServiceException e){
            throw new IllegalStateException("Faile to store file to s3",e);
        }
    }*/
    public void save(String path,
                     String fileName,
                     Optional<Map<String,String>> optionalMetadata,
                     InputStream inputStream){
        ObjectMetadata metadata = new ObjectMetadata();
        optionalMetadata.ifPresent(map -> {
            if (!map.isEmpty()) {
                map.forEach(metadata::addUserMetadata);
            }
        });
        try {
            metadata.setContentLength(inputStream.available());
            amazonS3Client.putObject(path, fileName, inputStream, metadata);
        } catch (AmazonServiceException | IOException e) {
            throw new IllegalStateException("Failed to store file to s3", e);
        }
    }

    public void deleteFile(String bucketName, String key) {
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName,key);
        try{
            amazonS3Client.deleteObject(deleteObjectRequest);
        }catch (AmazonServiceException e){
            throw new IllegalStateException("Faile to delete file to s3",e);
        }

    }

    public S3Object getObject(String path,String key){
        S3Object object =amazonS3Client.getObject(path,key);
        return object;
    }

    public String getVideoUrl(String fileName,String path){
        Date expiration = new Date(System.currentTimeMillis() + 3600000); // 1 hour expiration
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(path, fileName)
                        .withMethod(GET)
                        .withExpiration(expiration);
        URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    public void copyObject(String buckeName,String fromKey,String toKey,String folderName){
        String oldFileKey = folderName + "/" + fromKey;
        String newFileKey = folderName + "/" + toKey;
        CopyObjectRequest copyObjRequest = new CopyObjectRequest(buckeName, oldFileKey, buckeName, newFileKey);
        amazonS3Client.copyObject(copyObjRequest);
    }
}

