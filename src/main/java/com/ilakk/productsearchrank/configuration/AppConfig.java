package com.ilakk.productsearchrank.configuration;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javax.annotation.PostConstruct;

import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;

/**
 *
 * @author Ilakkuvaselvi Manoharan, 2019
 * @copyright GNU General Public License v3
 * No reproduction in whole or part without maintaining this copyright notice
 * and imposing this condition on any subsequent users.
 *
 *
 */

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = { "com.ilakk.*" })
@Component("appconfig")
public class AppConfig {

	@Value("${amazonsearchrank.input.sample.filename}")
	private String sampleFileName;

	@Value("${amazonsearchrank.input.searchword}")
	private String searchString;

	@Value("${amazonsearchrank.amazon.uri.base}")
	private String baseaddr;

	@Value("${amazonsearchrank.amazon.uri.autocomplete}")
	private String autocompleteuri;

	@Value("${amazonsearchrank.apriori.input.file}")
	private String aprioriInputFile;

	@Value("${amazonsearchrank.apriori.input.minsupport}")
	private String minSupport;

	@Value("${amazonsearchrank.apriori.dataset.delimiter}")
	private String aprioriDataDelimiter;

	@Value("${amazonsearchrank.apriori.filepath.splitter}")
	private String aprioriFilepathSplitter;

	private String aprioriDataFileName;

	@PostConstruct
	public void setAprioriDataFileName() throws URISyntaxException {
		Path path = Paths.get(getClass().getResource(this.sampleFileName).toURI());
		String pathString = path.toString();
		String firstSubString = null;
		String secondSubString = null;
		Pattern p = Pattern.compile("(.*?)" + aprioriFilepathSplitter + "(.*)");
		Matcher m = p.matcher(pathString);
		if (m.matches()) {
			firstSubString = m.group(1);
			secondSubString = m.group(2);
		}
		this.aprioriDataFileName = firstSubString + aprioriInputFile;

	}

	@Bean("aprioriDelimiter")
	public String getAprioriDataDelimiter() {
		return aprioriDataDelimiter;
	}

	public String[] getAprioriInputArgs() {

		String[] inputArgs = new String[2];
		inputArgs[0] = this.getAprioriDataFileName();
		inputArgs[1] = this.minSupport;
		return inputArgs;
	}

	@Primary
	@Bean("aprioriFile")
	public String getAprioriDataFileName() {
		return this.aprioriDataFileName;
	}

	public String getSampleFileName() {
		return sampleFileName;
	}
    
	@Bean("searchString")
	public String getsearchString() {
		return searchString;
	}

	public String getBaseAddr() {
		return baseaddr;
	}

	public String getAutocompleteUri() {
		return autocompleteuri;
	}

}
