package org.fish.appium.common;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Getter
@Setter
class ProducerAndConsumer {
    public Boolean STATE = false;
    public BlockingQueue<String> DEVICE_STATUS_QUEUE = new ArrayBlockingQueue<>(300);

    public void producer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Producer-Start");
                while (STATE) {
                    try {
                        System.out.println("生产者 生产前 总共有 " + DEVICE_STATUS_QUEUE.size() + " 个商品");
                        DEVICE_STATUS_QUEUE.put("1");
                        System.out.println("生产者  生产后 总共有 " + DEVICE_STATUS_QUEUE.size() + " 个商品");
                        System.out.println("---------------------------------------------------------------------------");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Producer-Close");
            }
        }).start();
    }

    public void consumer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Consumer-Start");
                while (STATE) {
                    try {
                        String take = DEVICE_STATUS_QUEUE.take();
                        System.out.println("消费 " + take);
                        System.out.println("---------------------------------------------------------------------------");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Consumer-Close");
            }
        }).start();
    }
}
