package com.hnp.simplerestclient;

import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("/client")
public class RestClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestClientApplication.class, args);
    }

    private final Logger logger =
            LoggerFactory.getLogger(RestClientApplication.class);

    @Autowired
    RestClient restClient;

    @GetMapping("{actionType}")
    public void customerEndpoint(@PathVariable("actionType") int actionType) {
        switch (actionType) {
            case 1 -> getAllCustomers();
            case 2 -> getCustomerById();
            case 3 -> createCustomer();
            case 4 -> updateCustomer();
            case 5 -> deleteCustomer();

        }
    }

    public void getAllCustomers() {
        logger.info("getAllCustomers");
        List<Customer> customers = restClient.get()
                .uri("/customers")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<List<Customer>>() {});
        customers.forEach(c -> System.out.println(c));
    }

    public void getCustomerById() {
        logger.info("getCustomerById");
        int id = 1;
        Customer customer = restClient.get()
                .uri("/customers/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(status -> status.value() == 404, (request, response) ->
                    logger.info("404 not found"))
                .body(Customer.class);
        System.out.println(customer);
    }

    public void createCustomer() {
        logger.info("createCustomer");
        Customer customer = new Customer();
        customer.setFirstName("new customer");
        customer.setLastName("new customer");

        ResponseEntity responseEntity = restClient.post()
                .uri("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .body(customer)
                .retrieve()
                .toBodilessEntity();
        logger.info("response code=" + responseEntity.getStatusCode());

    }

    public void updateCustomer() {
        logger.info("updateCustomer");
        Customer customer = new Customer();
        customer.setId(10);
        customer.setFirstName("update customer");
        customer.setLastName("update customer");
        ResponseEntity responseEntity = restClient.put()
                .uri("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .body(customer)
                .retrieve()
                .onStatus(status -> status.value() == 404, (request, response) ->
                        logger.info("404 not found"))
                .toBodilessEntity();
        logger.info("response code=" + responseEntity.getStatusCode());
    }

    public void deleteCustomer() {
        logger.info("deleteCustomer");
        int id = 2;
        ResponseEntity responseEntity = restClient.delete()
                .uri("/customers/{id}", id)
                .retrieve()
                .toBodilessEntity();
        logger.info("response code=" + responseEntity.getStatusCode());
    }
}
