package com.example.threadexample;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

@Slf4j
@RequiredArgsConstructor
public class SubProcess implements Callable<String> {

    private final Integer i;

    @Override
    public String call() throws Exception {
        log.info("SubProcess Start. {}sec", i);
        Thread.sleep(i * 1000);
        log.info("SubProcess End. {}sec", i);
        return "SubProcess result " + i;
    }
}