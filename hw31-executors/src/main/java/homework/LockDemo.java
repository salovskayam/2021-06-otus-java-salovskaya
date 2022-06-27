package homework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class LockDemo {
    private static final Logger logger = LoggerFactory.getLogger(LockDemo.class);
    private static final int FROM = 1;
    private static final int TO = 10;
    private final Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();

    private int sharedCounter = 0;
    // может принимать значения 1 и -1. Определяет вывод по возрастанию или убыванию
    private int order = 1;
    // определяет, нужно ли инкрементировать sharedCounter или просто вывести на печать
    private boolean modify = true;

    public static void main(String[] args) {
        new LockDemo().go();
    }

    private void go() {
        new Thread(this::writer1, "writer-1").start();
        new Thread(this::writer2, "writer-2").start();
    }

    private void writer1() {
        while (!Thread.currentThread().isInterrupted()) {
            lock.lock();
            try {
                while (modify) {
                    condition.await();
                }
                if (order > 0) {
                    sharedCounter++;
                } else {
                    sharedCounter--;
                }
                logger.info("write:{}", sharedCounter);
                modify = !modify;
                condition.signalAll();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
    }

    private void writer2() {
        while (!Thread.currentThread().isInterrupted()) {
            lock.lock();
            try {
                while (!modify) {
                    condition.await();
                }
                if (sharedCounter == TO || (order < 0 && sharedCounter == FROM)) {
                    order *= -1;
                }
                logger.info("write:{}", sharedCounter);
                modify = !modify;
                condition.signalAll();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
    }
}
