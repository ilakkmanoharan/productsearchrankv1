package com.ilakk.productsearchrank.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import reactor.core.publisher.*;

import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

import com.ilakk.productsearchrank.configuration.AppConfig;
import com.ilakk.productsearchrank.dataprocessor.*;

/** 
 * 
 * @author Ilakkuvaselvi Manoharan, 2019
 * @copyright GNU General Public License v3
 * No reproduction in whole or part without maintaining this copyright notice
 * and imposing this condition on any subsequent users.
 * 
 * 
 */

@Component("service")
public class PSRServiceImpl implements IPSRService{
	
    private WebClient client;
    private String searchString;
    private String baseAddr;
    private String autocompleteUri;
    private List<String> dataSets;
	
    @Autowired
    @Qualifier("appconfig")
    private AppConfig appconfig;
	
    @Autowired
    @Qualifier("parser")
    private Parser parser;
	
    @Autowired
    @Qualifier("wordcountranker")
    private WordCountRanker wordcountranker;
	
    @PostConstruct
    public void init(){
		
		this.baseAddr = appconfig.getBaseAddr();
		this.autocompleteUri = appconfig.getAutocompleteUri();
		this.client = WebClient.create(this.baseAddr);
		
    }
	
	

@Override	
public String findRank(String searchString){
	    this.searchString = searchString;
    	
    	fetchDataSets();
    	parser.writeDataSets(dataSets);
    	
    	//new Apriori(this.aprioriInputArgs);
    	
    	return wordcountranker.findRank(searchString);
    	
    }
	
    public void fetchDataSets() {
    	
    	List<String> dataSetList = new ArrayList<String>();
		
		int len = searchString.length();
		
		for(int i = 3; i <= len; i++) {
			String searchStr = searchString.substring(0, i);
			
			Mono<ClientResponse> result = client.get()
					.uri(autocompleteUri + searchString)
					.accept(MediaType.APPLICATION_JSON)
					.exchange();
			
			String result1 = ">> result = " + result.flatMap(res -> res.bodyToMono(String.class)).block();
			dataSetList.add(result1);
			
		}
		
		this.dataSets = dataSetList;
		
     }


	
	
}
