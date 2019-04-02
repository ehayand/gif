package com.giftest.server.service;

import com.giftest.server.ConvertReq;
import com.giftest.server.util.GifSequenceWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.UUID;

/**
 * Created by ehay@naver.com on 2019-04-02
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Slf4j
@Service
public class ConvertService {

    private final FileUploadService fileUploadService;

    public ConvertService(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    public String convert(final ConvertReq convertReq) {
        String url;

        try {
            String fileName = GifSequenceWriter.write(convertReq);
            File file = new File(System.getProperty("user.dir") + fileName);
            url = fileUploadService.upload(fileName, file);
            file.delete();
        } catch (Exception e) {
            log.error(e.getMessage());
            url = null;
        }

        return url;
    }

    public static String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
