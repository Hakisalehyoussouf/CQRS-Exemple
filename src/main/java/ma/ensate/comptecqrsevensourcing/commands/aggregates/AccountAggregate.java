package ma.ensate.comptecqrsevensourcing.commands.aggregates;


import ma.ensate.comptecqrsevensourcing.commonapi.commands.CreateAccountCommand;
import ma.ensate.comptecqrsevensourcing.commonapi.commands.CreditAccountCommand;
import ma.ensate.comptecqrsevensourcing.commonapi.commands.DebitAccountCommand;
import ma.ensate.comptecqrsevensourcing.commonapi.enums.AccountStatus;
import ma.ensate.comptecqrsevensourcing.commonapi.events.AccountActivatedEvent;
import ma.ensate.comptecqrsevensourcing.commonapi.events.AccountCreatedEvent;
import ma.ensate.comptecqrsevensourcing.commonapi.events.AccountCreditedEvent;
import ma.ensate.comptecqrsevensourcing.commonapi.events.AccountDebitedEvent;
import ma.ensate.comptecqrsevensourcing.commonapi.exceptions.AmountNegativeException;
import ma.ensate.comptecqrsevensourcing.commonapi.exceptions.BalanceNotSufficientException;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class AccountAggregate {


    @AggregateIdentifier
    private String accountID;
    private double balance;
    private String currency;
    private AccountStatus status;

    public  AccountAggregate(){

        //Required by AxonFramework

    }

    @CommandHandler
    public AccountAggregate(CreateAccountCommand command){

        if(command.getInitialBalance()<0) throw new RuntimeException("Impossible de creer un compte avec une balance initile negative!");

        AggregateLifecycle.apply(new AccountCreatedEvent(
                command.getId(),
                command.getInitialBalance(),
                command.getCurrency(),
                AccountStatus.CREATED));
    }



    @EventSourcingHandler
    public  void on(AccountCreatedEvent event){
        this.accountID = event.getId();
        this.balance = event.getInitialBalance();
        this.currency = event.getCurrency();
        this.status = event.getStatus();

        AggregateLifecycle.apply(new AccountActivatedEvent(
           event.getId(),
           AccountStatus.ACTIVATED
        ));
    }


    @EventSourcingHandler
    public  void  on(AccountActivatedEvent event){
        this.status = event.getStatus();
    }



    @CommandHandler
    public void  handle(CreditAccountCommand command){
        if(command.getAmount()<0) throw  new AmountNegativeException("Amount can't be negative");


        AggregateLifecycle.apply(new AccountCreditedEvent(
                command.getId(),
                command.getAmount(),
                command.getCurrency()
        ));
    }


    @CommandHandler
    public void handle(DebitAccountCommand command){
        if(command.getAmount()<0) throw new AmountNegativeException("Amount can't be negative!");

        if(this.balance < command.getAmount()) throw  new BalanceNotSufficientException("Balance doesn't sufficient " + this.balance);

        AggregateLifecycle.apply(new AccountDebitedEvent(
           command.getId(),
           command.getAmount(),
           command.getCurrency()
        ));

    }

    @EventSourcingHandler
    public void on(AccountDebitedEvent event){
        this.balance-=event.getAmount();
    }

    @EventSourcingHandler
    public void on(AccountCreditedEvent event){
        this.balance +=event.getAmount();
    }





}
