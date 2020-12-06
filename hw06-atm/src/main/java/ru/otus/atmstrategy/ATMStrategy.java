package ru.otus.atmstrategy;

import ru.otus.banknote.DenominationsBanknotes;
import ru.otus.cell.Cell;

import java.util.List;
import java.util.Map;

public interface ATMStrategy {
    int getSum(List<Cell> cellATMS);

    int getNumberBills(List<Cell> cellATMS, DenominationsBanknotes banknote);

    void setNumberBills(List<Cell> cellATMS, DenominationsBanknotes banknote, int numberBills);

    Map<DenominationsBanknotes, Integer> takeMoney(List<Cell> cellATMS, int sum);
}
