package com.miltos.research.test;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

public class TestClient {
	
	private ArrayList<String> resultPages;  

	public static void main(String[] args) {
		
		// 0. Create a JAX-RS Client for submitting requests
		Client client = ClientBuilder.newClient();
		
		// 1. Set the target URL of the client to which the requests will be submitted
		Response response = client.target("https://api.github.com/search/repositories?q=Java+language:Java&sort=stars&order=desc&per_page=100&page=1")
			  .request().get();
		
		// 2. Print the response to the screen
		System.out.println(response.readEntity(String.class));
		System.out.println(response.getHeaderString("Link"));
		
		// 3. Get the total number of pages
		int totalPages = retrieveTotalPagesNumber(response);
		
		//TODO: Remove this print
		System.out.println("TOTAL PAGES NUMBER: " + totalPages);
	}

	private static int retrieveTotalPagesNumber(Response response) {
		
		// 1. Get the "Link" header and split it into three strings
		String[] splittedText = response.getHeaderString("Link").split("&page=");
		
		//TODO: Remove these prints
		System.out.println(splittedText[0]);
		System.out.println(splittedText[1]);
		System.out.println(splittedText[2]);
		
		// 2. Get the first integer of the third string
	    Pattern pattern = Pattern.compile("^([0-9]+)");
	    Matcher matcher = pattern.matcher(splittedText[2]);
	    if (matcher.find()) {
	        System.out.println(matcher.group(0));
	    }
	    
	    // 3. Return the retieved value
		return Integer.parseInt(matcher.group(0));
	}

}
