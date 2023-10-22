package com.example.demo.s3;

public enum BucketName {
    COURSE_IMAGE("nhttri-image-upload"),
    COURSE_VIDEO("nhttri-upload-video");

    private final String bucketName;

    BucketName (String bucketName){
        this.bucketName=bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}
