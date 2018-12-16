/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appserv.view;

import appserv.controller.ConverterFacade;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author Tania
 */
@Named(value = "currencyManager")
@RequestScoped
public class CurrencyManager implements Serializable {
    
    @EJB
    private ConverterFacade converterFacade;
    
    private List<String> currencies;
    private String fromCurrency;
    private String toCurrency;
    private double amount;
    private double convertedAmount;
    
    public void convert() {
        
        convertedAmount = converterFacade.convert(amount, fromCurrency, toCurrency);
    }
    
    public double getConvertedAmount() {
        
        return convertedAmount;
    }
    
    public String getFromCurrency() {
        
        return fromCurrency;
    }
    
    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }
    
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
