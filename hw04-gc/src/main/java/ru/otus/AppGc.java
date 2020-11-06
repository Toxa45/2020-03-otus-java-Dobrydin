package ru.otus;

/*
-Xms256m
-Xmx256m
-Xlog:gc=debug:file=./logs/gc-%p-%t.log:tags,uptime,time,level:filecount=5,filesize=10m
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=./logs/dump
-XX:+UseG1GC
 */

import com.sun.management.GarbageCollectionNotificationInfo;
import ru.otus.bench.BenchmarkMemoryOverflow;
import ru.otus.bench.BenchmarkMemoryOverflowMBean;

import javax.management.*;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AppGc {
    private static long wTime;
    private static List<GarbageCollectionNotificationInfo> garbageCollectionNotificationInfoList= new ArrayList<>();

    public static void main(String[] args)  throws Exception{
        long beginTime = System.currentTimeMillis();
        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());
        switchOnMonitoring();

        int loopCounter = 100000;
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("ru.otus:type=BenchmarkMemoryOverflow");

        BenchmarkMemoryOverflow mbean = new BenchmarkMemoryOverflow(loopCounter);
        mbs.registerMBean(mbean, name);

        try {
            mbean.run();
        } catch (OutOfMemoryError e) {
            System.out.println(e.getMessage());
        }
        wTime = (System.currentTimeMillis() - beginTime) / 1000;
        System.out.println("/.\n Время работы: " + wTime + " сек");
        printReport(mbean);
    }

   private static void printReport(BenchmarkMemoryOverflowMBean mbean) {
       System.out.println("Отчет");
       Map<Long, Double> gcDurationPeriod = garbageCollectionNotificationInfoList.stream()
               .collect(
                       Collectors.groupingBy(
                               gcNot -> gcNot.getGcInfo().getStartTime() / 60_000,
                               Collectors.summingDouble(gcNotValue -> gcNotValue.getGcInfo().getDuration())
                       )
               );

       Map<String, Long> gcCountPeriod = garbageCollectionNotificationInfoList.stream()
               .collect(
                       Collectors.groupingBy(
                               GarbageCollectionNotificationInfo::getGcName,
                               Collectors.counting()
                       )
               );

       Map<String, Double> gcDuration = garbageCollectionNotificationInfoList.stream()
               .collect(
                       Collectors.groupingBy(
                               GarbageCollectionNotificationInfo::getGcName,
                               Collectors.summingDouble(gcNotValue -> gcNotValue.getGcInfo().getDuration())
                       )
               );

       long sumDuratonGC = garbageCollectionNotificationInfoList.stream().mapToLong(gcNotValue -> gcNotValue.getGcInfo().getDuration()).sum();

       System.out.println("Общее время ряботы GC: " + sumDuratonGC + " ms");
       System.out.println("Количестчо элементов добавленных в список: " + mbean.getCounterElementList());
       gcCountPeriod.forEach((name, count) -> System.out.println("Название GC: " + name + ", количество запусков: " + count + " время работы " + gcDuration.get(name)));
       gcDurationPeriod.forEach((minute, duration) -> System.out.println((minute + 1) + " мин GC работал --> " + duration + " сек"));
   }

    private static void switchOnMonitoring() {
        List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcbean : gcbeans) {
            System.out.println("GC name:" + gcbean.getName());
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            NotificationListener listener = (notification, handback) -> {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                    garbageCollectionNotificationInfoList.add(info);
                    String gcName = info.getGcName();
                    String gcAction = info.getGcAction();
                    String gcCause = info.getGcCause();

                    long startTime = info.getGcInfo().getStartTime();
                    long duration = info.getGcInfo().getDuration();

                    System.out.println("start:" + startTime + " Name:" + gcName + ", action:" + gcAction + ", gcCause:" + gcCause + "(" + duration + " ms)");
                }
            };
            emitter.addNotificationListener(listener, null, null);
        }
    }

}
