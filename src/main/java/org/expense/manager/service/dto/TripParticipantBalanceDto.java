package org.expense.manager.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripParticipantBalanceDto {
    private String user;
    private double owesAmount;
    private double owedAmount;
public TripParticipantBalanceDto(String user){
    this.user = user;
}
    public void addOwedAmount(double amount){
        this.owedAmount+=amount;
    }
    public void addOwesAmount(double amount){
        this.owesAmount+=amount;
    }
}
