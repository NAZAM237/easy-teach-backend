package fr.cleanarchitecture.easyteach.course.domain.valueobject;

import java.math.BigDecimal;

public class Price {
    private BigDecimal amount;
    private String currency;

    public Price() {}

    public Price(BigDecimal amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }
}
