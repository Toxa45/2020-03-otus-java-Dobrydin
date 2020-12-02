package ru.otus.atmstrategy;

import ru.otus.banknote.DenominationsBanknotes;
import ru.otus.cell.Cell;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ATMStrategyStandart implements ATMStrategy {

    @Override
    public int getSum(List<Cell> cellATMS) {
        return cellATMS.stream().mapToInt(Cell::getSum).sum();
    }

    @Override
    public int getNumberBills(List<Cell> cellATMS, DenominationsBanknotes banknote) {
        return findCellByBannote(cellATMS, banknote).mapToInt(Cell::getNumberBills).sum();
    }

    @Override
    public void setNumberBills(List<Cell> cellATMS, DenominationsBanknotes banknote, int numberBills) {
        findCellByBannote(cellATMS, banknote)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("нет ячейки с купурами наминалом " + banknote.getDenominations()))
                .setNumberBills(numberBills);
    }

    @Override
    public Map<DenominationsBanknotes, Integer> takeMoney(List<Cell> cellATMS, int sum) {
        if (sum <= 0) {
            throw new IllegalArgumentException("Нельзя выдать сумму <=0, запрошенная сумма = " + sum);
        }
        List<Cell> cellsSorted = Collections.unmodifiableCollection(cellATMS)
                .stream()
                .filter(cell -> cell.getSum() > 0)
                .collect(Collectors.toList());

        var takenMap = new HashMap<DenominationsBanknotes, Integer>();

        int sumLocal = sum;

        for (Cell cell :
                cellsSorted) {
            if (cell.getDenominations() <= sumLocal) {
                Integer countBanknote = sumLocal / cell.getDenominations();
                DenominationsBanknotes banknotes = cell.getDenominationsBanknotes();
                final Integer countBanknoteFinal = countBanknote < cell.getNumberBills() ? countBanknote : cell.getNumberBills();
                Integer compute = takenMap.compute(banknotes, (k, v) -> v == null ? countBanknoteFinal : v + countBanknoteFinal);

                sumLocal -= countBanknoteFinal * cell.getDenominations();

                if (sumLocal < 0)
                    throw new RuntimeException("Не удалось выдать сумму = "+sum);

                if (sumLocal == 0)
                    break;
            }
        }


        if (sumLocal == 0){
            takenMap.forEach((banknote,count)->take(cellATMS,banknote,count));
            return takenMap;
        }

        throw new RuntimeException("Не удалось выдать сумму = "+sum);
    }



    private void take(List<Cell> cellATMS, DenominationsBanknotes banknote, int numberBills) {
        Cell cellBAnknote = findCellByBannote(cellATMS, banknote).filter(cell -> cell.getNumberBills() >= numberBills)
                .findFirst().orElseGet(() -> {throw new RuntimeException("Не удалось выдать "+numberBills+" купюр номиналом "+banknote.getDenominations()); });
        cellBAnknote.take(numberBills);
    }

    private Stream<Cell> findCellByBannote(List<Cell> cellATMS, DenominationsBanknotes banknote) {
        return cellATMS
                .stream()
                .filter(c -> c.getDenominationsBanknotes().equals(banknote));
    }
}
