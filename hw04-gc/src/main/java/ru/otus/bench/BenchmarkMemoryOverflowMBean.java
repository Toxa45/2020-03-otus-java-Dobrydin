package ru.otus.bench;

public interface BenchmarkMemoryOverflowMBean {
    void run() throws InterruptedException;

    int getCounterElementList();
}
