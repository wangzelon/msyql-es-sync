package com.opshop.controller;

import com.opshop.core.SyncMysql;
import com.opshop.entity.TestOne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * created by wangzelong 2019/3/16 10:59
 */
@RestController
public class TestOneController {
    @Autowired
    private SyncMysql syncMysql;

    @GetMapping("/test")
    public TestOne test() {
        return null;
    }

    @GetMapping("/add")
    public String testAdd() throws IOException {
        syncMysql.addEs();
        return "success";
    }
}
