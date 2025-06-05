package org.example.first_unit.load_balance;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum LoadBalanceAlgorithm {

    RANDOM((list, current) -> list.get(new Random().nextInt(list.size()))),
    ROUND_ROBIN((list, current) -> list.get(current.getAndUpdate(i -> (i + 1) % list.size()))),;

    private final BiFunction<List<String>, AtomicInteger, String> algorithm;

    public String get(final List<String> list, final AtomicInteger current) {
        return algorithm.apply(list, current);
    }

}
