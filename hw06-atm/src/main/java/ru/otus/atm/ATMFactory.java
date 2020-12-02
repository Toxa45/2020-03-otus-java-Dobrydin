package ru.otus.atm;

import ru.otus.atmstrategy.ATMStrategyStandart;
import ru.otus.banknote.DenominationsBanknotes;
import ru.otus.banknote.RubBanknotes;
import ru.otus.cell.Cell;
import ru.otus.cell.CellATM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ATMFactory {
    public static ATMStandart atmForRubles() {
        var banknotes = new HashMap<DenominationsBanknotes, Integer>();
        banknotes.put(RubBanknotes.rub_10, 100);
        banknotes.put(RubBanknotes.rub_50, 100);
        banknotes.put(RubBanknotes.rub_100, 80);
        banknotes.put(RubBanknotes.rub_200, 50);
        banknotes.put(RubBanknotes.rub_500, 50);
        banknotes.put(RubBanknotes.rub_1000, 20);
        banknotes.put(RubBanknotes.rub_2000, 20);
        banknotes.put(RubBanknotes.rub_5000, 10);
        return standardATM(banknotes);
    }


    public static ATMStandart standardATM(Map<DenominationsBanknotes, Integer> banknotes) {
        List<Cell> cellATMS = new ArrayList();
        banknotes.forEach((banknot, count)->cellATMS.add(new CellATM(banknot, count)));
        return new ATMStandart(cellATMS , new ATMStrategyStandart());
    }
}
