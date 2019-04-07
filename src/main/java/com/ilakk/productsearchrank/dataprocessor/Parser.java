package com.ilakk.productsearchrank.dataprocessor;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

import com.ilakk.productsearchrank.configuration.AppConfig;

/** 
 * 
 * @author Ilakkuvaselvi Manoharan, 2019
 * @copyright GNU General Public License v3
 * No reproduction in whole or part without maintaining this copyright notice
 * and imposing this condition on any subsequent users.
 * 
 * 
 */

@Component("parser")
public class Parser {

	private Set<String> uniqueItems = new HashSet<String>();

	private Map<String, Integer> dataIdMap = new HashMap<String, Integer>();
	
	private Map<String, Integer> wordCountMap = new HashMap<String, Integer>();
	
	private List<String> dataSetList;
	
	private String aprioriFile;
	
	private String aprioriDelimiter;
	
	private List<String> autocompleteDataSets;

	private static int i = 0;

	
	@Autowired
	@Qualifier("aprioriFile")
	private void setAprioriFile(String aprioriFile) {
		this.aprioriFile = aprioriFile;
	}
	
	@Autowired
	@Qualifier("aprioriDelimiter")
	private void setAprioriDelimiter(String aprioriDelimiter) {
		this.aprioriDelimiter = aprioriDelimiter;
	}
	
	@Bean("wordcountmap")
	public Map<String, Integer> getWordCountMap(){
		return this.wordCountMap;
	}
	
	
	public void writeDataSets(List<String> dataSets) {
		   
		  
		   
		   
			
			PrintWriter out = null;

			try {
				
				out = new PrintWriter(aprioriFile);
				
				
				for(String data: dataSets) {
					
					String line = parseItems(data);

					out.println(line);
					
				}

			
				 } catch (IOException e) {

				
				e.printStackTrace();
			} finally {
				
				if(out != null) {
					out.close();
				}
				
			}

		}
			

		public String parseItems(String dataSet) {

			int firstpos = dataSet.indexOf("[", dataSet.indexOf("[") + 1);

			String itemsSubstring = dataSet.substring(firstpos + 1, dataSet.indexOf("]"));
			
			List<String> itemsList = Arrays.asList(itemsSubstring.split(","));

			List<Integer> numDataSet = new ArrayList<Integer>();

			itemsList.forEach(item -> {

				String keyWord = item.replaceAll("^\"|\"$", "").trim();

				
			  if (!(dataIdMap.containsKey(keyWord.trim()))) {

					dataIdMap.put(keyWord, ++i);

				}
			  
			  if (wordCountMap.containsKey(keyWord.trim())) {
				  
				  int value = wordCountMap.get(keyWord.trim())+1;
					
				  wordCountMap.put(keyWord.trim(), value);
				  
				  
			  } else {
					
					wordCountMap.put(keyWord.trim(), 1);
					
			  }

				numDataSet.add(dataIdMap.get(keyWord.trim()));

				numDataSet.forEach(System.out::println);

			});

			
			StringBuilder lineBuilder = new StringBuilder();
			
	        for(Integer city : numDataSet){
	        	
	        lineBuilder.append(city);
	        
	        lineBuilder.append(aprioriDelimiter);
	        
	        }
	        
	        String line = lineBuilder.toString();
	        
			return line;

		}

	

}
