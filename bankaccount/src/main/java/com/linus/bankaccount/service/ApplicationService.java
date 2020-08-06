package com.linus.bankaccount.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.linus.bankaccount.constants.ErrorCodes;
import com.linus.bankaccount.constants.ErrorMessage;
import com.linus.bankaccount.controller.MainController;
import com.linus.bankaccount.model.repo.TransactionRepository;
import com.linus.bankaccount.validation.PatternChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;


@Service
public class ApplicationService {
    @Autowired
    private TransactionRepository repository;

    private static final Logger _LOGGER = LoggerFactory.getLogger(MainController.class);
    private static ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    @Transactional
    public JsonNode makeTransactionAction(JsonNode jsonNode) throws JsonProcessingException {
        JsonNode jNode = mapper.createObjectNode();
        if(jsonNode.has("fromAccountId") && jsonNode.has("toAccountId") && jsonNode.has("amount")) {
            String fromAccountId = jsonNode.get("fromAccountId").asText();
            String toAccountId = jsonNode.get("toAccountId").asText();
            String amount = jsonNode.get("amount").asText();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            if(amount.contains("INR") || amount.contains("Rs")){
                ((amount.replace("Rs","")).replace("INR","")).replace("Rs.","");
            }

            if (!PatternChecker.validateInput(fromAccountId, PatternChecker.CHECK_DIGIT) ||
                    !PatternChecker.validateInput(toAccountId, PatternChecker.CHECK_DIGIT) ||
                    !PatternChecker.validateInput(amount, PatternChecker.CHECK_ADS)) {
                ((ObjectNode) jNode).put("errorCode", ErrorCodes._INVALID);
                ((ObjectNode) jNode).put("errorMessage", ErrorMessage._INVALID);
            }else if(fromAccountId.equals(toAccountId)){
                ((ObjectNode) jNode).put("errorCode", ErrorCodes._FAILED);
                ((ObjectNode) jNode).put("errorMessage", "Transactions cannot be carried out on same account.");
            }else if(fromAccountId.length() != 16){
                ((ObjectNode) jNode).put("errorCode", ErrorCodes._FAILED);
                ((ObjectNode) jNode).put("errorMessage", "Provide a proper sender account number");
            }else if(toAccountId.length() != 16){
                ((ObjectNode) jNode).put("errorCode", ErrorCodes._FAILED);
                ((ObjectNode) jNode).put("errorMessage", "Provide a proper receiver account number");
            }else if(Double.parseDouble(amount) <= 0.0){
                ((ObjectNode) jNode).put("errorCode", ErrorCodes._FAILED);
                ((ObjectNode) jNode).put("errorMessage", "Amount cannot be zero or negative number.");
            }else if(repository.validSender(fromAccountId) == 0){
                ((ObjectNode) jNode).put("errorCode", ErrorCodes._NOT_FOUND);
                ((ObjectNode) jNode).put("errorMessage", "Sender details not found.");
            }else if(repository.validReciever(toAccountId) == 0){
                ((ObjectNode) jNode).put("errorCode", ErrorCodes._NOT_FOUND);
                ((ObjectNode) jNode).put("errorMessage", "Receiver details not found.");
            }else if(repository.checksameuser(fromAccountId, toAccountId) == 1){
                ((ObjectNode) jNode).put("errorCode", ErrorCodes._FAILED);
                ((ObjectNode) jNode).put("errorMessage", "Transfer between accounts belonging to the same user is not allowed.");
            }else if(repository.checkBalance(fromAccountId,amount) < 0.0){
                ((ObjectNode) jNode).put("errorCode", ErrorCodes._FAILED);
                ((ObjectNode) jNode).put("errorMessage", "Insufficient amount on the account.");
            }else if(repository.maxBalancePolicy(toAccountId,amount) > 50000.00){
                ((ObjectNode) jNode).put("errorCode", ErrorCodes._FAILED);
                ((ObjectNode) jNode).put("errorMessage", "Account will cross the maximum balance policy, cannot transfer the requested amount.");
            }else{
                int scount = repository.updateSenderBalance(fromAccountId,amount,timestamp);
                int rcount = repository.updateReceiverBalance(toAccountId,amount,timestamp);

                ((ObjectNode) jNode).put("newSrcBalance", repository.newSenderBalance(fromAccountId));
                ((ObjectNode) jNode).put("totalDestBalance", repository.totReceiverBalance(toAccountId));
                ((ObjectNode) jNode).put("transferedAt", String.valueOf(timestamp));

                repository.insertTransaction(fromAccountId,toAccountId,amount,timestamp,jNode.toPrettyString());
                return jNode;
            }

            repository.insertTransaction(fromAccountId,toAccountId,amount,timestamp,jNode.toPrettyString());
            return jNode;
        }else{
            ((ObjectNode) jNode).put("errorCode", ErrorCodes._NOT_FOUND);
            ((ObjectNode) jNode).put("errorMessage", ErrorMessage._NOT_FOUND);
        }
        return jNode;
    }


}
