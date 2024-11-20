import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import ru.otus.numbers.NumberGeneratorGrpc;
import ru.otus.numbers.NumberRangeRequest;
import ru.otus.numbers.NumberResponse;

import java.io.IOException;

public class NumbersServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder
                .forPort(8190)
                .addService(new NumberGeneratorService())
                .build();

        System.out.println("Server is starting...");
        server.start();
        System.out.println("Server started.");
        server.awaitTermination();
    }

    static class NumberGeneratorService extends NumberGeneratorGrpc.NumberGeneratorImplBase {
        @Override
        public void generateNumbers(NumberRangeRequest request, StreamObserver<NumberResponse> responseObserver) {
            int firstValue = request.getFirstValue();
            int lastValue = request.getLastValue();

            for (int i = firstValue + 1; i <= lastValue; i++) {
                responseObserver.onNext(NumberResponse.newBuilder().setValue(i).build());
                try {
                    Thread.sleep(2000); // Генерация нового числа каждые 2 секунды
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            responseObserver.onCompleted();
        }
    }
}