package consumer.app;

import consumer.service.CalculatorRemoteImpl;
import provider.service.Calculator;

public class ConsumerApp {
    public static void main(String[] args) {
        Calculator calculator = new CalculatorRemoteImpl();
        int result = calculator.add(1, 2);
        System.out.println(result);
    }
}
