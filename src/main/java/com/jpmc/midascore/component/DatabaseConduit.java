package com.jpmc.midascore.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.jpmc.midascore.entity.IncentiveRecord;
import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.IncentiveRepository;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;

@Component
public class DatabaseConduit {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private IncentiveRepository incentiveRepository;

    @Autowired
    private RestTemplate restTemplate;

    public void processTransaction(Transaction transaction) {

        // 1. Fetch users
        UserRecord sender =
                userRepository.findById((long) transaction.getSenderId());

        UserRecord recipient =
                userRepository.findById((long) transaction.getRecipientId());

        // 2. Call incentive API (POST transaction)
        IncentiveAmount response =
                restTemplate.postForObject(
                        "http://localhost:8080/incentive",
                        transaction,
                        IncentiveAmount.class
                );

        Float incentive =
                response != null ? response.getAmount() : 0f;

        // 3. Update balances
        sender.setBalance(sender.getBalance() - transaction.getAmount());

        recipient.setBalance(
                recipient.getBalance()
                        + transaction.getAmount()
                        + incentive
        );

        userRepository.save(sender);
        userRepository.save(recipient);

        // 4. Save transaction (ONLY amount, as constructor expects)
        TransactionRecord record = new TransactionRecord(
                sender,
                recipient,
                (double) transaction.getAmount()
        );

        transactionRepository.save(record);

        // 5. Save incentive separately
        IncentiveRecord incentiveRecord = new IncentiveRecord(
                record.getId(),
                incentive.doubleValue()
        );

        incentiveRepository.save(incentiveRecord);
    }
}
