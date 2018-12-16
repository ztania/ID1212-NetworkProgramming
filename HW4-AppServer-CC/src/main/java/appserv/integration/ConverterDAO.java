/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appserv.integration;

import appserv.model.Currency;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author Tania
 */
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateless
public class ConverterDAO {
    @PersistenceContext(unitName = "converterPU")
    private EntityManager em;
    
    public Currency findCurrencyRate(String convertCurrency) {
        
        Currency currency = em.find(Currency.class, convertCurrency);
        
        if(currency == null) {
            System.out.println("No currency found");
        }
        
        return currency;
    }

}
