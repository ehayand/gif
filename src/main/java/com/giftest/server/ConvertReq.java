package com.giftest.server;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by ehay@naver.com on 2019-04-02
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Data
public class ConvertReq {
    private MultipartFile[] images;
    private int delay;
}
