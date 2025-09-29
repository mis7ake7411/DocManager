package com.docmanager.util;

public class SnowflakeIdGenerator {

  // 起始時間戳（自訂一個基準點，減少 timestamp 的長度）
  private final static long START_STAMP = 1609459200000L; // 2021-01-01

  // 各部分位數
  private final static long SEQUENCE_BIT = 12;  // 每毫秒序列號佔 12 位
  private final static long MACHINE_BIT  = 5;   // 機器編號佔 5 位
  private final static long DATACENTER_BIT = 5; // 資料中心編號佔 5 位

  // 各部分最大值
  private final static long MAX_DATACENTER_NUM = ~(-1L << DATACENTER_BIT);
  private final static long MAX_MACHINE_NUM    = ~(-1L << MACHINE_BIT);
  private final static long MAX_SEQUENCE       = ~(-1L << SEQUENCE_BIT);

  // 各部分位移
  private final static long MACHINE_LEFT    = SEQUENCE_BIT;
  private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
  private final static long TIMESTAMP_LEFT  = DATACENTER_LEFT + DATACENTER_BIT;

  private long datacenterId;  // 資料中心 ID
  private long machineId;     // 機器 ID
  private long sequence = 0L; // 毫秒內序號
  private long lastStamp = -1L; // 上次生成 ID 的時間戳

  public SnowflakeIdGenerator(long datacenterId, long machineId) {
    if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
      throw new IllegalArgumentException("DatacenterId out of range");
    }
    if (machineId > MAX_MACHINE_NUM || machineId < 0) {
      throw new IllegalArgumentException("MachineId out of range");
    }
    this.datacenterId = datacenterId;
    this.machineId = machineId;
  }

  public synchronized long nextId() {
    long currStamp = getNewStamp();

    if (currStamp < lastStamp) {
      throw new RuntimeException("Clock moved backwards. Refusing to generate id");
    }

    if (currStamp == lastStamp) {
      // 相同毫秒內，序列號自增
      sequence = (sequence + 1) & MAX_SEQUENCE;
      if (sequence == 0L) {
        // 序列號用盡，等到下一毫秒
        currStamp = getNextMill();
      }
    } else {
      sequence = 0L;
    }

    lastStamp = currStamp;

    return (currStamp - START_STAMP) << TIMESTAMP_LEFT   // 時間戳部分
        | datacenterId << DATACENTER_LEFT              // 資料中心部分
        | machineId << MACHINE_LEFT                    // 機器部分
        | sequence;                                    // 序列號部分
  }

  private long getNextMill() {
    long mill = getNewStamp();
    while (mill <= lastStamp) {
      mill = getNewStamp();
    }
    return mill;
  }

  private long getNewStamp() {
    return System.currentTimeMillis();
  }

}

