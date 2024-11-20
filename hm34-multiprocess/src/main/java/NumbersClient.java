import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ru.otus.numbers.NumberGeneratorGrpc;
import ru.otus.numbers.NumberRangeRequest;
import ru.otus.numbers.NumberResponse;

import java.util.concurrent.atomic.AtomicInteger;

public class NumbersClient {

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8190)
                .usePlaintext()
                .build();

        NumberGeneratorGrpc.NumberGeneratorStub asyncStub = NumberGeneratorGrpc.newStub(channel);

        AtomicInteger currentValue = new AtomicInteger(0);
        AtomicInteger lastServerValue = new AtomicInteger(0);

        // Получение данных от сервера
        asyncStub.generateNumbers(
                NumberRangeRequest.newBuilder().setFirstValue(0).setLastValue(30).build(),
                new StreamObserver<>() {
                    @Override
                    public void onNext(NumberResponse value) {
                        System.out.println("new value: " + value.getValue());
                        lastServerValue.set(value.getValue());
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onCompleted() {
                        System.out.println("Server stream completed.");
                    }
                });

        // Логика клиента
        for (int i = 0; i <= 50; i++) {
            try {
                Thread.sleep(1000); // Интервал 1 секунда
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int lastValue = lastServerValue.getAndSet(0); // Используем только один раз
            currentValue.addAndGet(1 + lastValue);

            System.out.println("currentValue: " + currentValue.get());
        }

        channel.shutdown();
    }
}