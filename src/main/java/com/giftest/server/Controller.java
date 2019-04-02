package com.giftest.server;

import com.giftest.server.service.ConvertService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Created by ehay@naver.com on 2019-04-02
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Slf4j
@RestController
@RequestMapping("")
public class Controller {

    private final ConvertService convertService;

    public Controller(ConvertService convertService) {
        this.convertService = convertService;
    }

    @PostMapping("/convert")
    public String convertImage(Optional<ConvertReq> convertReq) {
        try {
            if(!convertReq.isPresent())
                return "FAIL";

            return convertService.convert(convertReq.get());
        } catch (Exception e) {
            return "FAIL";
        }
    }
}
