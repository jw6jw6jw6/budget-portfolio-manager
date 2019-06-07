package com.bitbluesoftware.bpm.util;

import net.sf.ofx4j.OFXException;
import net.sf.ofx4j.client.*;
import net.sf.ofx4j.client.impl.FinancialInstitutionServiceImpl;
import net.sf.ofx4j.client.impl.OFXHomeFIDataStore;
import net.sf.ofx4j.domain.data.banking.AccountType;
import net.sf.ofx4j.domain.data.banking.BankAccountDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FinancialInterface {

    Logger log = LoggerFactory.getLogger(FinancialInterface.class);

    public void init() {
        try {
            OFXHomeFIDataStore fiDataStore = new OFXHomeFIDataStore();
            FinancialInstitutionData data = fiDataStore.getInstitutionData("726");
            FinancialInstitutionService service = new FinancialInstitutionServiceImpl();
            FinancialInstitution fi = service.getFinancialInstitution(data);
//            FinancialInstitutionProfile profile = fi.readProfile();
            BankAccountDetails bankAccountDetails = new BankAccountDetails();
            bankAccountDetails.setRoutingNumber("314977405");
            bankAccountDetails.setAccountNumber("1800900769465");
            bankAccountDetails.setAccountType(AccountType.CHECKING);
            BankAccount bankAccount = fi.loadBankAccount(bankAccountDetails,"jw6jw6jw6","Goofball95");

            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            Date startDate = simpleDateFormat.parse("2018-09-09");
            Date endDate = simpleDateFormat.parse("2018-09-09");

            AccountStatement statement = bankAccount.readStatement(startDate, endDate);

            log.error("$"+statement.getAvailableBalance().getAmount());

        } catch(ParseException| OFXException e) {
            log.error("Financial Data Load Exception: "+e.getMessage());
        }
    }
//    fi.setLanguage(Locale.US.getISO3Language().toUpperCase());

}
