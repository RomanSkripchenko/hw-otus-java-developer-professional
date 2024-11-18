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
        int repetitions = 3; // Сколько раз каждый поток должен выполнить последовательность
        for (int r = 0; r < repetitions; r++) {
            synchronized (lock) {
                while ((threadNumber == 1 && !isThread1Turn) || (threadNumber == 2 && isThread1Turn)) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }

                // Печать последовательности чисел
                for (int i = 1; i <= limit; i++) {
                    System.out.println(Thread.currentThread().getName() + ": " + i);
                }
                for (int i = limit - 1; i >= 1; i--) {
                    System.out.println(Thread.currentThread().getName() + ": " + i);
                }

                // Передача очереди другому потоку
                isThread1Turn = !isThread1Turn;
                lock.notifyAll();
            }
        }
    }
}
