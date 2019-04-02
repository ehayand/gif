package com.giftest.server.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by ehay@naver.com on 2019-04-02
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Service
public class FileUploadService {

    @Value("${cloud.aws.s3.bucket.url}")
    private String defaultUrl;

    private final S3Service s3Service;

    public FileUploadService(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    public String upload(String saveFileName, final File file) throws IOException {
        String url;

        try {
            //S3 파일 업로드
            s3Service.uploadOnS3(saveFileName, file);
            //주소 할당
            url = defaultUrl + saveFileName;
        } catch (StringIndexOutOfBoundsException e) {
            //파일이 없을 경우
            url = null;
        }

        return url;
    }

    public static String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
