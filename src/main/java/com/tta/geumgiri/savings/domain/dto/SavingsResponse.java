package com.tta.geumgiri.savings.domain.dto;

import com.tta.geumgiri.savings.domain.Deposit;
import com.tta.geumgiri.savings.domain.Savings;

import java.util.List;

public class SavingsResponse {
    private List<Deposit> deposits;
    private List<Savings> savingsList;

    public SavingsResponse(List<Deposit> deposits, List<Savings> savingsList) {
        this.deposits = deposits;
        this.savingsList = savingsList;
    }

    public List<Deposit> getDeposits() {
        return deposits;
    }

    public List<Savings> getSavingsList() {
        return savingsList;
    }
}
