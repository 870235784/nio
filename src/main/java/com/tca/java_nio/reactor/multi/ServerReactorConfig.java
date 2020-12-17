package com.tca.java_nio.reactor.multi;

import lombok.Data;

import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhoua
 * @Date 2020/12/18
 */
@Data
public class ServerReactorConfig {

    /**
     * cpu核数
     */
    private int core = Runtime.getRuntime().availableProcessors();

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    /**
     * 使用多个selector
     */
    List<Selector> selectors = new ArrayList<>(core);

    /**
     * 使用多个subReactor
     */
    List<SubReactor> subReactors = new ArrayList<>(core);
}
