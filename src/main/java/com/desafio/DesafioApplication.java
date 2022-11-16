package com.desafio;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;

@SpringBootApplication
public class DesafioApplication { 
	
	private static String SPREADSHEET_ID = "17uTrxMYAneyh87L9_LL1X2kIwLhMXsRz8hPmE52URBw";
	private static Sheets sheetsService;
	private static String APPLICATION_NAME = "Desafio tunts";
	
	
	private static Credential authorize() throws IOException, GeneralSecurityException {
		System.out.println("------------------------------------------------------\n" + 
	                       "Creating oath exchange to grant access to google api \n" + 
				           "------------------------------------------------------");
		InputStream in = DesafioApplication.class.getResourceAsStream("/credentials.json"); 
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
				JacksonFactory.getDefaultInstance(), new InputStreamReader(in));
		
		
		List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);
		
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(),
				clientSecrets, scopes)
				.setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
				.setAccessType("offline").build();
		
		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
		
		System.out.println("------------------------------------------------------\n" + 
		                   "Triggering the oath flow");
		return credential;
	}
	
	public static Sheets getSheetsService() throws IOException,  GeneralSecurityException {
		Credential credential = authorize();
		System.out.println("------------------------------------------------------\n" + 
		                   "Creating google Sheets object");
		return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
				JacksonFactory.getDefaultInstance(), credential).setApplicationName(APPLICATION_NAME).build();
	}

	public static void main(String[] args) {
		SpringApplication.run(DesafioApplication.class, args);
	}

}
