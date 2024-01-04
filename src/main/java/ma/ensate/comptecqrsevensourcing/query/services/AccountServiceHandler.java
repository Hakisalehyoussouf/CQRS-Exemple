package ma.ensate.comptecqrsevensourcing.query.services;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ensate.comptecqrsevensourcing.commonapi.enums.OperationType;
import ma.ensate.comptecqrsevensourcing.commonapi.events.AccountActivatedEvent;
import ma.ensate.comptecqrsevensourcing.commonapi.events.AccountCreatedEvent;
import ma.ensate.comptecqrsevensourcing.commonapi.events.AccountCreditedEvent;
import ma.ensate.comptecqrsevensourcing.commonapi.events.AccountDebitedEvent;
import ma.ensate.comptecqrsevensourcing.commonapi.queries.GetAllAccountsQuery;
import ma.ensate.comptecqrsevensourcing.query.entites.Account;
import ma.ensate.comptecqrsevensourcing.query.entites.Operation;
import ma.ensate.comptecqrsevensourcing.query.repositories.AccountRepository;
import ma.ensate.comptecqrsevensourcing.query.repositories.OperationRepository;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class AccountServiceHandler{

    private final AccountRepository accountRepository;
    private final OperationRepository operationRepository;

    @EventHandler
    public void on(AccountCreatedEvent event){
        log.info("*****************************************************");
        log.info("AccountCreatedEvent received!");
        Account account = new Account();
        account.setId(event.getId());
        account.setBalance(event.getInitialBalance());
        account.setCurrency(event.getCurrency());
        account.setStatus(event.getStatus());

        accountRepository.save(account);
    }


    @EventHandler
    public void on(AccountActivatedEvent event){
        log.info("*****************************************************");
        log.info("AccountActivatedEvent received!");
        Account account = accountRepository.findById(event.getId()).get();
        account.setStatus(event.getStatus());
        accountRepository.save(account);
    }

    @EventHandler
    public void on(AccountDebitedEvent event){
        log.info("*****************************************************");
        log.info("AccountDebitedEvent received!");
        Account account = accountRepository.findById(event.getId()).get();
        Operation operation = new Operation();
        operation.setAmount(event.getAmount());
        operation.setType(OperationType.DEBIT);
        operation.setAccount(account);
        operation.setDate(new Date()); //Normalement sa valeur vient de l'evenement!!!!!!!1
        operationRepository.save(operation);
        account.setBalance(account.getBalance()-event.getAmount());
        accountRepository.save(account);
    }


    @EventHandler
    public void on(AccountCreditedEvent event){
        log.info("*****************************************************");
        log.info("AccountCreditedEvent received!");
        Account account = accountRepository.findById(event.getId()).get();
        Operation operation = new Operation();
        operation.setAmount(event.getAmount());
        operation.setType(OperationType.CREDIT);
        operation.setAccount(account);
        operation.setDate(new Date()); //Normalement sa valeur vient de l'evenement!!!!!!!1
        operationRepository.save(operation);
        account.setBalance(account.getBalance()+event.getAmount());
        accountRepository.save(account);
    }


    @QueryHandler
    public List<Account> on(GetAllAccountsQuery query){
        return  accountRepository.findAll();
    }


}
