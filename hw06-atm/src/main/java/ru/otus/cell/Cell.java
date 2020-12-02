package ru.otus.cell;

import ru.otus.banknote.DenominationsBanknotes;

public interface Cell {
    int getSum();

    int getNumberBills();

    int getDenominations();

    DenominationsBanknotes getDenominationsBanknotes();

    void setNumberBills(int numberBills);

    void take(int numberBills);
}
