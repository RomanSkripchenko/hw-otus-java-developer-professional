package ru.calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Summator {
    private int sum = 0;  // Заменили Integer на int
    private int prevValue = 0;  // Заменили Integer на int
    private int prevPrevValue = 0;  // Заменили Integer на int
    private int sumLastThreeValues = 0;  // Заменили Integer на int
    private int someValue = 0;  // Заменили Integer на int
    private final List<Data> listValues = new ArrayList<>();
    private final Random random = new Random(10);

    public void calc(Data data) {
        listValues.add(data);

        if (listValues.size() % 100_000 == 0) {
            listValues.clear();
        }

        int dataValue = data.getValue();

        // Суммируем данные
        sum += dataValue + random.nextInt();
        sumLastThreeValues = dataValue + prevValue + prevPrevValue;

        prevPrevValue = prevValue;
        prevValue = dataValue;

        // Оптимизируем someValue, делая расчет за один цикл
        int temp = sumLastThreeValues * sumLastThreeValues / (dataValue + 1) - sum;
        someValue += Math.abs(temp) * 3 + listValues.size();
    }

    public int getSum() {  // Возвращаем int вместо Integer
        return sum;
    }

    public int getPrevValue() {  // Возвращаем int вместо Integer
        return prevValue;
    }

    public int getPrevPrevValue() {  // Возвращаем int вместо Integer
        return prevPrevValue;
    }

    public int getSumLastThreeValues() {  // Возвращаем int вместо Integer
        return sumLastThreeValues;
    }

    public int getSomeValue() {  // Возвращаем int вместо Integer
        return someValue;
    }
}
