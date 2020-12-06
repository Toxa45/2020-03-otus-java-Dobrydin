package ru.otus.atm;

import ru.otus.atmstrategy.ATMStrategy;
import ru.otus.banknote.DenominationsBanknotes;
import ru.otus.cell.Cell;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ATMStandart implements ATM {
    private static final Comparator<Cell> CELL_COMPARATOR = Comparator.comparingInt((Cell c) -> c.getDenominationsBanknotes().getDenominations()).reversed();

    private ATMStrategy atmStrategy;
    private List<Cell> cellATMS;


    public ATMStandart(List<Cell> cellATMS, ATMStrategy atmComand) {
        cellATMS.sort(CELL_COMPARATOR);
        this.cellATMS = cellATMS;
        this.atmStrategy = atmComand;
    }

    public int getSum(){
        return atmStrategy.getSum(cellATMS);
    }

    @Override
    public List<Cell> getCellATMS() {
        return Collections.unmodifiableList(cellATMS);
    }

    @Override
    public int getNumberBills(DenominationsBanknotes banknote) {
        return atmStrategy.getNumberBills(cellATMS , banknote);
    }

    @Override
    public void setNumberBills(DenominationsBanknotes banknote, int numberBills) {
        atmStrategy.setNumberBills(cellATMS, banknote, numberBills);
    }

    @Override
    public Map<DenominationsBanknotes, Integer> takeMoney(int sum) {
        return atmStrategy.takeMoney(cellATMS, sum);
    }
}
