package ru.otus.atm;

import ru.otus.banknote.DenominationsBanknotes;
import ru.otus.cell.Cell;

import java.util.List;
import java.util.Map;

public interface ATM {
    int getSum();

    int getNumberBills(DenominationsBanknotes banknote);

    void setNumberBills(DenominationsBanknotes banknote, int numberBills);

    Map<DenominationsBanknotes, Integer> takeMoney(int sum);

    List<Cell> getCellATMS();
}
