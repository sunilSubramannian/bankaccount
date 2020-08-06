package com.linus.bankaccount.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linus.bankaccount.service.ApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(path = "/v1/apt")
public class MainController {
    private static final Logger _LOGGER = LoggerFactory.getLogger(MainController.class);
    private static ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    @Autowired
    ApplicationService applicationService;

    @PostMapping(path = "/transaction")
    public JsonNode makeTransaction(@RequestBody JsonNode jsonNode) throws IOException {
        return applicationService.makeTransactionAction(jsonNode);
    }
}
