package ru.calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Summator {
    private Integer sum = 0;
    private Integer prevValue = 0;
    private Integer prevPrevValue = 0;
    private Integer sumLastThreeValues = 0;
    private Integer someValue = 0;
    // Эта коллекция должна остаться, чтобы симулировать реальное использование данных
    private final List<Data> listValues = new ArrayList<>();
    private final Random random = new Random(10);

    public void calc(Data data) {
        listValues.add(data);

        // Очищаем коллекцию, чтобы уменьшить объем памяти, занимаемой объектами
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

    public Integer getSum() {
        return sum;
    }

    public Integer getPrevValue() {
        return prevValue;
    }

    public Integer getPrevPrevValue() {
        return prevPrevValue;
    }

    public Integer getSumLastThreeValues() {
        return sumLastThreeValues;
    }

    public Integer getSomeValue() {
        return someValue;
    }
}
