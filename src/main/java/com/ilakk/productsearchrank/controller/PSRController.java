package com.ilakk.productsearchrank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiParam;
import org.json.simple.JSONObject;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ilakk.productsearchrank.service.IPSRService;

/** 
 * 
 * @author Ilakkuvaselvi Manoharan, 2019
 * 
 * @copyright GNU General Public License v3
 * No reproduction in whole or part without maintaining this copyright notice
 * and imposing this condition on any subsequent users.
 * 
 * 
 */

@RestController
@Api(value = "", description = "Method to find the score of the keyword")
public class PSRController {

	@Autowired
    @Qualifier("service")
	private IPSRService iPSRService;

	@GetMapping("/estimate")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Returns the search-volume score of the keyword", notes = "The range is from 1 to 100")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Returned the score", response = String.class),
			@ApiResponse(code = 500, message = "Internal Server Error"),
			@ApiResponse(code = 404, message = "No result returned") })

	public JSONObject findRank(
			@ApiParam(name = "keyword", value = "keyword", defaultValue = "") @RequestParam("keyword") String keyword) {
				
				String score = iPSRService.findRank(keyword.toLowerCase());
				
				JSONObject outputJsonObj = new JSONObject();
				
				outputJsonObj.put("Keyword", keyword);
				
				outputJsonObj.put("Score", score);
				
				return outputJsonObj;
	}

}


