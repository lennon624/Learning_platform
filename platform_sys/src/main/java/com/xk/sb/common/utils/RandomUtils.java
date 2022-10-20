package com.xk.sb.common.utils;

public class RandomUtils {

    /**
     * 随机生成指定范围内N个不重复的数
     *
     * @param min 最小值
     * @param max 最大值
     * @param n   数量
     * @return (min, max)内n个不重复的随机数
     */
    public static int[] randomIntNumbersBetween(int min, int max, int n) {
        if (n > (max - min + 1) || max < min) {
            return null;
        }
        int[] result = new int[n];
        int count = 0;
        while (count < n) {
            int num = (int) (Math.random() * (max - min)) + min;
            boolean flag = true;
            for (int j = 0; j < n; j++) {
                if (num == result[j]) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                result[count] = num;
                count++;
            }
        }
        return result;
    }
}
