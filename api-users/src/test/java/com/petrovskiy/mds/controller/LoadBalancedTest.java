package com.petrovskiy.mds.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.petrovskiy.mds.config.RibbonConfigurationTest;
import com.petrovskiy.mds.service.CompanyFeignClient;
import com.petrovskiy.mds.service.dto.CompanyDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.logging.Logger;

import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.moreThan;
import static com.petrovskiy.mds.CompanyMocks.setupMockCompanyResponse;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@EnableConfigurationProperties
@Slf4j
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { RibbonConfigurationTest.class })
public class LoadBalancedTest {

    @Autowired
    private WireMockServer mockBooksService;

    @Autowired
    private WireMockServer secondMockBooksService;

    @Autowired
    private CompanyFeignClient clientService;

    private Logger logger;

    @BeforeEach
    void setUp() throws IOException {
        setupMockCompanyResponse(mockBooksService);
        setupMockCompanyResponse(secondMockBooksService);
    }

    @Test
    void whenGetCompanies_thenRequestsAreLoadBalanced() {
        for (int k = 0; k < 10; k++) {
            logger.info(clientService.getCompanies().toString());
            clientService.getCompanies();
        }

        mockBooksService.verify(
                moreThan(0), getRequestedFor(WireMock.urlEqualTo("/companies")));
        secondMockBooksService.verify(
                moreThan(0), getRequestedFor(WireMock.urlEqualTo("/companies")));
    }

    @Test
    public void whenGetCompanies_thenTheCorrectCompaniesShouldBeReturned() {
        assertTrue(clientService.getCompanies()
                .containsAll(asList(
                        new CompanyDto(),
                        new CompanyDto())));
    }
}

