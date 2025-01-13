public class AlternatingNumberPrinter {
    private final Object lock = new Object();
    private boolean isThread1Turn = true;

    public static void main(String[] args) {
        AlternatingNumberPrinter printer = new AlternatingNumberPrinter();
        Thread thread1 = new Thread(() -> printer.printNumbers(1), "Поток 1");
        Thread thread2 = new Thread(() -> printer.printNumbers(2), "Поток 2");

        thread1.start();
        thread2.start();
    }

    public void printNumbers(int threadNumber) {
        int limit = 10;
        int number = 1;
        boolean ascending = true;

        while (true) {
            synchronized (lock) {
                while ((threadNumber == 1 && !isThread1Turn) || (threadNumber == 2 && isThread1Turn)) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }

                // Печать текущего числа
                System.out.println(Thread.currentThread().getName() + ": " + number);

                // Изменение числа
                if (ascending) {
                    if (number < limit) {
                        number++;
                    } else {
                        ascending = false;
                        number--;
                    }
                } else {
                    if (number > 1) {
                        number--;
                    } else {
                        ascending = true;
                        number++;
                    }
                }

                // Передача очереди другому потоку
                isThread1Turn = !isThread1Turn;
                lock.notifyAll();
            }
        }
    }
}
