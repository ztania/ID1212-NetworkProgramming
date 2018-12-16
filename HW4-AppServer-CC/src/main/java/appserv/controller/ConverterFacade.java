/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appserv.controller;

import appserv.integration.ConverterDAO;
import appserv.model.Currency;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Tania
 */
@Stateless
public class ConverterFacade implements Serializable{
    
    @EJB ConverterDAO converterDAO;
    
    public double convert(double amount, String fromCurrency, String toCurrency) {
        
        Currency convertFromCurrency = converterDAO.findCurrencyRate(fromCurrency);
        Currency convertToCurrency = converterDAO.findCurrencyRate(toCurrency);
       
       double convertedAmount = amount * (convertFromCurrency.getRate() / convertToCurrency.getRate());
        
       return convertedAmount;
    }
    
    
}
