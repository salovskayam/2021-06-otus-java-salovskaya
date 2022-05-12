package homework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SynchronizedDemo {
    private static final Logger logger = LoggerFactory.getLogger(SynchronizedDemo.class);
    private static final int FROM = 1;
    private static final int TO = 10;

    private int sharedCounter = 0;
    // может принимать значения 1 и -1. Определяет вывод по возрастанию или убыванию
    private static int order = 1;
    // определяет, нужно ли инкрементировать sharedCounter или просто вывести на печать
    private boolean modify = true;

    public static void main(String[] args) {
        new SynchronizedDemo().go();
    }

    private void go() {
        new Thread(() -> writer(true), "writer-1").start();
        new Thread(() -> writer(false), "writer-2").start();
    }

    private synchronized void writer(boolean modifyNeed) {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                while (modify != modifyNeed) {
                    wait();
                }
                if (modifyNeed) {
                    if (order > 0) {
                        sharedCounter++;
                    } else {
                        sharedCounter--;
                    }
                    if (sharedCounter == TO || order < 0 && sharedCounter == FROM) {
                        order *= -1;
                    }
                }
                logger.info("write:{}", sharedCounter);
                modify = !modify;
                notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
