package com.zhumqs.utils;

import java.util.Random;

/**
 * @author mingqizhu
 * @date 20191201
 */
public class RandomUtils {
    public static int getRandomInterval(int a, int b) {
        return (int)(a + Math.random()*(b - a + 1));
    }

    public static int getRandom(int a)  {
        Random r = new Random();
        return r.nextInt(a);
    }

    public static int getPlusOrMinus() {
        return Math.random() > 0.5 ? 1 : -1;
    }
}
