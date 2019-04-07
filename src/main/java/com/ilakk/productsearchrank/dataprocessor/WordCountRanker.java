package com.ilakk.productsearchrank.dataprocessor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Comparator;
import static java.util.stream.Collectors.*;
import static java.util.Map.Entry.*;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/** 
 * 
 * @author Ilakkuvaselvi Manoharan, 2019
 * @copyright GNU General Public License v3
 * No reproduction in whole or part without maintaining this copyright notice
 * and imposing this condition on any subsequent users.
 * 
 * 
 */

@Component("wordcountranker")
public class WordCountRanker {
	
	private Map<String, Integer> wordCountMap = new HashMap<String, Integer>();
	
	private Map<String, Integer> sortedRankMap = new LinkedHashMap<String, Integer>();
	
	@Autowired
	@Qualifier("wordcountmap")
	private void setWordCountMap(Map<String, Integer> wordCountMap) {
		this.wordCountMap = wordCountMap;
	}
	
	
	public String findRank(String keyword) {
        
        sortByCount();
        
        List<String> keysList = new ArrayList<String>(sortedRankMap.keySet());
		
		int rank = keysList.indexOf(keyword) + 1;
		
		int totCandidates = keysList.size();
		
		int percentRank = (rank * 100)/totCandidates;
		
		String rankStr = String.valueOf(percentRank);
		
		return rankStr;
	}
	
	
	
	private void sortByCount() {
		
		
	    sortedRankMap = wordCountMap
	        .entrySet()
	        .stream()
	        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
	        .collect(
	            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
	                LinkedHashMap::new));
		
	}
	

}
