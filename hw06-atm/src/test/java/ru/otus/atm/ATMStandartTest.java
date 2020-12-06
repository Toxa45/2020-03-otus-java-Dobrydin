package ru.otus.atm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.atmstrategy.ATMStrategyStandart;
import ru.otus.banknote.DenominationsBanknotes;
import ru.otus.banknote.RubBanknotes;
import ru.otus.cell.Cell;
import ru.otus.cell.CellATM;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Банкомат")
public class ATMStandartTest {
    private ATMStandart atmStandart;

    @BeforeEach
    void setUp(){
        atmStandart = ATMFactory.atmForRubles();
    }

    @Test
    @DisplayName("выдавать деньги минимальным количеством купюр")
    void issueMoneyWithMminimumAmountDenominations(){
        var take_100 = atmStandart.takeMoney(100);
        assertEquals(1, take_100.size(), "ошибка при выдаче 100р. "+take_100.toString());
        assertEquals(100, sumMaoney(take_100), "ошибка при выдаче 100р. "+take_100.toString());

        var take_1000 = atmStandart.takeMoney(1_000);
        assertEquals(1, take_1000.size(), "ошибка при выдаче 1000р. "+take_1000.toString());
        assertEquals(1_000, sumMaoney(take_1000), "ошибка при выдаче 1000р. "+take_1000.toString());

        var take_100000 = atmStandart.takeMoney(100_000);
        assertEquals(3, take_100000.size(), "ошибка при выдаче 100000р. "+take_100000.toString());
        assertEquals(40, numberBills(take_100000), "ошибка при выдаче 100000р. "+take_100000.toString());
        assertEquals(100_000, sumMaoney(take_100000), "ошибка при выдаче 100000р. "+take_100000.toString());

        var take_2000 = atmStandart.takeMoney(2_000);
        assertEquals(1, take_2000.size(), "ошибка при выдаче 2000р. "+take_2000.toString());
        assertEquals(2_000, sumMaoney(take_2000), "ошибка при выдаче 2000р. "+take_2000.toString());

    }

    @Test
    @DisplayName("выбрасит ошибку когда нет подходящих купюр для сдачи, или сумма<=0")
    void willThrowErrorWhenThereNoMatchingDenominations() {
        assertThrows(RuntimeException.class, () -> atmStandart.takeMoney(5));
        assertThrows(RuntimeException.class, () -> atmStandart.takeMoney(-1));
        assertThrows(RuntimeException.class, () -> atmStandart.takeMoney(0));
    }

    @Test
    @DisplayName("выбрасит ошибку если запрошена сумма больше, чем есть на остатке")
    void willThrowErrorIfTheRequesteASmountIsGreaterThanTheBalance() {
        assertThrows(RuntimeException.class, () -> atmStandart.takeMoney(1_000_000_000));
    }


    @Test
    @DisplayName("выбрасит ошибку при попытке установить количество купюр, ячейки с которыми нет")
    void willThrowErrorIfYouSetTheNumberOfBillsWithNoCells() {
        List<Cell> cellATMS = new ArrayList();
        cellATMS.add(new CellATM(RubBanknotes.rub_10, 10));
        cellATMS.add(new CellATM(RubBanknotes.rub_1000, 10));
        ATMStandart atm = new ATMStandart(cellATMS, new ATMStrategyStandart());

        assertThrows(IllegalArgumentException.class, () -> atm.setNumberBills(RubBanknotes.rub_5000,500) );
        assertEquals(0, atm.getNumberBills(RubBanknotes.rub_5000));
        assertDoesNotThrow(() -> atm.getNumberBills(RubBanknotes.rub_10));
        assertDoesNotThrow(() -> atm.setNumberBills(RubBanknotes.rub_10, 1));
    }


    @Test
    @DisplayName("должен вернуть правильный остаток")
    void shouldReturnTheCorrectRemainder() {
        assertEquals(159_000, atmStandart.getSum());

        var take_100000 = atmStandart.takeMoney(100_000);

        assertEquals(59_000, atmStandart.getSum());
        var take_59000 = atmStandart.takeMoney(59_000);

        assertEquals(0, atmStandart.getSum());
    }

    private int sumMaoney(Map<DenominationsBanknotes, Integer> map){
        return map
                .entrySet()
                .stream()
                .mapToInt( entry-> entry.getKey().toMoney(entry.getValue()) )
                .sum();
    }


    private int numberBills(Map<DenominationsBanknotes, Integer> map){
        return map
                .entrySet()
                .stream()
                .mapToInt( entry-> entry.getValue() )
                .sum();
    }
}
