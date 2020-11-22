package ru.otus.bench;

import java.util.ArrayList;
import java.util.List;

public class BenchmarkMemoryOverflow implements BenchmarkMemoryOverflowMBean  {
    private volatile int iterarionCounter = 0;
    private int counterElementList;
    private List<Long> list = new ArrayList();

    public BenchmarkMemoryOverflow(int iterarionCounter) {
        this.iterarionCounter = iterarionCounter;
    }

    @Override
    public void     run() throws InterruptedException {
        while (true) {
            for (int idx = 0; idx < iterarionCounter; idx++) {
                list.add(idx * 2L);
                counterElementList++;
            }
            list.subList(0, iterarionCounter / 2).clear();
            Thread.sleep(150);
        }
    }


    @Override
    public int getCounterElementList() {
        return counterElementList;
    }

}
