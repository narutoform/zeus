package cn.chinotan.zeus.framework.util;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author xingcheng
 */
public class RedPacketUtil {
    /**
     * 每个红包最大是平均值的倍数
     */
    private static final double TIMES = 2.1;

    /**
     * 拆分红包
     *
     * @param money    分配金额
     * @param count    分配人数
     * @param minMoney 最小红包额度
     * @param maxMoney 最大红包额度
     * @return 分配好的金额
     */
    public static List<Integer> splitRedPackets(int money, int count, int minMoney, int maxMoney) {
        if (!isRight(money, count, minMoney, maxMoney)) {
            return Collections.emptyList();
        }
        List<Integer> list = new ArrayList<>(count);
        //红包最大金额为平均金额的TIMES倍  
        int max = (int) (money * TIMES / count);
        max = max > maxMoney ? maxMoney : max;
        for (int i = 0; i < count; i++) {
            int one = random(money, minMoney, max, count - i, maxMoney);
            list.add(one);
            money -= one;
        }
        return list;
    }

    /**
     * 随机红包额度
     *
     * @param money 分配金额
     * @param minS  最小金额
     * @param maxS  最大金额
     * @param count 分配人数
     * @return 随机的金额
     */
    private static int random(int money, int minS, int maxS, int count, int maxMoney) {
        //红包数量为1，直接返回金额  
        if (count == 1) {
            return money;
        }
        //如果最大金额和最小金额相等，直接返回金额  
        if (minS == maxS) {
            return minS;
        }
        int max = maxS > money ? money : maxS;
        //随机产生一个红包  
        int one = ((int) Math.rint(Math.random() * (max - minS) + minS)) % max + 1;
        int money1 = money - one;
        //判断该种分配方案是否正确  
        if (isRight(money1, count - 1, minS, maxMoney)) {
            return one;
        } else {
            double avg = money1 / (count - 1);
            if (avg < minS) {
                //递归调用，修改红包最大金额  
                return random(money, minS, one, count, maxMoney);
            } else if (avg > maxMoney) {
                //递归调用，修改红包最小金额  
                return random(money, one, maxS, count, maxMoney);
            }
        }
        return one;
    }

    /**
     * 此种红包是否合法
     *
     * @param money 分配金额
     * @param count 分配人数
     * @return 是否合法
     */
    private static boolean isRight(int money, int count, int minMoney, int maxMoney) {
        double avg = money / count;
        if (avg < minMoney) {
            return false;
        }
        if (avg > maxMoney) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(JSON.toJSONString(RedPacketUtil.splitRedPackets(23000, 10000, 1, 3)));
    }
}  
