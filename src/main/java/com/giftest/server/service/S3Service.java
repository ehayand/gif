package com.giftest.server.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

/**
 * Created by ehay@naver.com on 2019-04-02
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Slf4j
@Component
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.region.static}")
    private String region;
    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKeyId;
    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKeyId;

    private final AmazonS3Client amazonS3Client;

    public S3Service(AmazonS3Client amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    // S3 파일 업로드
    @Transactional
    public void uploadOnS3(final String fileName, final File file) {
        // AWS S3 전송 객체
        final TransferManager transferManager = new TransferManager(this.amazonS3Client);
        // 요청 객체
        final PutObjectRequest request = new PutObjectRequest(bucket, fileName, file);
        // 업로드 시도
        final Upload upload = transferManager.upload(request);

        try {
            //완료 확인
            upload.waitForCompletion();
        } catch (AmazonClientException amazonClientException) {
            log.error(amazonClientException.getMessage());
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }

    @Transactional
    public void deleteS3(final String fileName) {
        try {
            BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKeyId, secretKeyId);
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                    .withRegion(region)
                    .build();

            s3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
    }
}