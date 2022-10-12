package com.lhw.blond.google;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;


/**
 * @author ：linhw
 * @date ：22.10.12 15:36
 * @description：谷歌布隆过滤器实现
 * @modified By：
 */
public class GoogleBlondImpl {

    private static int size = 1000000;
    private static double fpp = 0.01;

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        BloomFilter bloomFilter = BloomFilter.create(Funnels.integerFunnel(), size, fpp);
        //添加数据，插入10w个数据
        for (int i = 0; i < size; i++) {
            bloomFilter.put(i);
        }

        double validCount = 100000;
        double errCount = 0;
        for (int i = size; i < size + validCount; i++) {
            if (bloomFilter.mightContain(i)) {
                errCount++;
            }
        }
        System.out.println("误判次数为：" + errCount);
        System.out.println("误判率为：" + (errCount / validCount));
    }

}
