package com.example.threadexample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 並行処理の実装例
 * (参考文献) https://qiita.com/KenyaSaitoh/items/9e049e7fb49b22546c05
 */
@Slf4j
@Component
public class MainClass implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws ExecutionException, InterruptedException, TimeoutException {

        log.info("Start!!!");

        ExecutorService executor = Executors.newFixedThreadPool(10); // スレッドプールを10個準備(最大10並列で実行)
        List<Future<String>> futureList = new ArrayList<>(); // futureを保持するリスト
        List<String> results = new ArrayList<>(); // SubProcessの処理結果を格納するリスト

        // 引数を変更しながらSubProcessを並列実行(引数は待機秒数)
        futureList.add(executor.submit(new SubProcess(29)));
        futureList.add(executor.submit(new SubProcess(10)));
        futureList.add(executor.submit(new SubProcess(11)));
        futureList.add(executor.submit(new SubProcess(12)));
        futureList.add(executor.submit(new SubProcess(13)));
        futureList.add(executor.submit(new SubProcess(14)));

        // Submitしたプロセスがすべて終了するとスレットプールも終了する。
        executor.shutdown();

        try {

            // Submitしたプロセス全体に対してタイムアウトを設定する場合は、awaitTerminationを使う
            // if (!executor.awaitTermination(15L, TimeUnit.SECONDS)) { //【2】
            //   log.info("[ Main ] shutdown now");
            //   executor.shutdownNow();
            // }

            // 処理結果をリストに格納、１スレッドあたりのタイムアウトとして20秒を設定
            for (var future : futureList) {
                results.add(future.get(20, TimeUnit.SECONDS));
            }

        } catch (TimeoutException e) {
            log.warn("Timeout.");
            executor.shutdownNow();
            return;
        }

        // 処理結果をログに出力
        for (var result : results) {
            log.info(result);
        }

        log.info("End!!!");
    }


}
