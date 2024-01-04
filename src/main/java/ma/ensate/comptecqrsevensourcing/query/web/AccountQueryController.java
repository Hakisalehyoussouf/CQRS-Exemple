package ma.ensate.comptecqrsevensourcing.query.web;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ensate.comptecqrsevensourcing.commonapi.queries.GetAllAccountsQuery;
import ma.ensate.comptecqrsevensourcing.query.entites.Account;
import org.axonframework.messaging.responsetypes.ResponseType;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/query")
@AllArgsConstructor
@Slf4j
public class AccountQueryController {

    private QueryGateway queryGateway;




    @GetMapping(path = "/accounts") //Normalement les DTO sont utilisees
    public List<Account>  allAccounts(){
        List<Account> response = queryGateway.query(new GetAllAccountsQuery(), ResponseTypes.multipleInstancesOf(Account.class)).join();

        return response;
    }


}
