package ma.ensate.comptecqrsevensourcing.commands.web;


import antlr.collections.List;
import lombok.AllArgsConstructor;
import ma.ensate.comptecqrsevensourcing.commonapi.commands.CreateAccountCommand;
import ma.ensate.comptecqrsevensourcing.commonapi.commands.CreditAccountCommand;
import ma.ensate.comptecqrsevensourcing.commonapi.commands.DebitAccountCommand;
import ma.ensate.comptecqrsevensourcing.commonapi.dtos.CreateAccountRequestDTO;
import ma.ensate.comptecqrsevensourcing.commonapi.dtos.CreditAccountRequestDTO;
import ma.ensate.comptecqrsevensourcing.commonapi.dtos.DebitAccountRequestDTO;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/commands/accounts")
public class AccountCommandController {

    private CommandGateway commandGateway;
    private EventStore eventStore;


    @PostMapping(path = "/create")
    public CompletableFuture<String> creatAccount(@RequestBody CreateAccountRequestDTO request){

        CompletableFuture<String> commandResponse = commandGateway.send(new CreateAccountCommand(
                UUID.randomUUID().toString(),
                request.getInitialBalance(),
                request.getCurrency()
        ));

        return commandResponse;
    }


    @PutMapping(path = "/credit")
    public CompletableFuture<String> creditAccount(@RequestBody CreditAccountRequestDTO request){

        CompletableFuture<String> commandResponse = commandGateway.send(new CreditAccountCommand(
                request.getAccountId(),
                request.getAmount(),
                request.getCurrency()
        ));

        return commandResponse;
    }

    @PutMapping(path = "/debit")
    public CompletableFuture<String> debitAccount(@RequestBody DebitAccountRequestDTO request){

        CompletableFuture<String> commandResponse = commandGateway.send(new DebitAccountCommand(
                request.getAccountId(),
                request.getAmount(),
                request.getCurrency()
        ));

        return commandResponse;
    }



    @GetMapping(path = "/{accountId}")
    public Stream getEventStore(@PathVariable String accountId){
        return eventStore.readEvents(accountId).asStream();
    }







}

