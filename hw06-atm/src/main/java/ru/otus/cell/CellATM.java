package ru.otus.cell;

import ru.otus.banknote.DenominationsBanknotes;

public class CellATM implements Cell{
    private int numberBills;
    private DenominationsBanknotes denominationsBanknotes;

    public CellATM( DenominationsBanknotes denominationsBanknotes, int numberBills) {
        this.numberBills = numberBills;
        this.denominationsBanknotes = denominationsBanknotes;
    }

    @Override
    public int getSum() {
        return getNumberBills()*getDenominationsBanknotes().getDenominations();
    }

    @Override
    public int getNumberBills() {
        return numberBills;
    }

    @Override
    public int getDenominations(){
        return denominationsBanknotes!=null ? denominationsBanknotes.getDenominations() : 0;
    }

    @Override
    public DenominationsBanknotes getDenominationsBanknotes() {
        return denominationsBanknotes;
    }

    @Override
    public void setNumberBills(int numberBills) {
        this.numberBills = numberBills;
    }

    @Override
    public void take(int numberBills) {
        if(numberBills > this.numberBills)
            throw new RuntimeException("Не удалось выдать "+numberBills+" купюр номиналом "+this.denominationsBanknotes.getDenominations());
        setNumberBills(this.numberBills -numberBills);
    }

}
