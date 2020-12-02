package ru.otus.banknote;

public enum RubBanknotes implements DenominationsBanknotes {
    rub_10(10),
    rub_50(50),
    rub_100(100),
    rub_200(200),
    rub_500(500),
    rub_1000(1000),
    rub_2000(2000),
    rub_5000(5000);

    private final int denominations;

    RubBanknotes(int denominations) {
        this.denominations = denominations;
    }

    @Override
    public int getDenominations() {
        return denominations;
    }
}
