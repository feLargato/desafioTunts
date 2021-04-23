package com.desafio.controller;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.desafio.DesafioApplication;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;


@RestController
@RequestMapping("/readSheet")
public class StudentController {
	
	private static String SPREADSHEET_ID = "17uTrxMYAneyh87L9_LL1X2kIwLhMXsRz8hPmE52URBw";
	private static Sheets sheetsService;
	DesafioApplication desafio;
	
	@GetMapping
	public void readSheet() throws IOException, GeneralSecurityException {
		int index = 4; 
		int average;
		int naf;
		
		sheetsService = desafio.getSheetsService();
		
		System.out.println("------------------------------------------------------\n" +  
		                   "Specifying reading range A4:F27");
		
		String range = "engenharia_de_software!A4:F27";
		
		System.out.println("------------------------------------------------------\n" + 
		                   "Accessing sheet's data");
		ValueRange response = desafio.getSheetsService().spreadsheets().values()
				.get(SPREADSHEET_ID, range).execute();
		

		List<List<Object>> values = response.getValues();
		
		System.out.println("------------------------------------------------------\n" + 
		                   "Calculating student's grades");
		for(List row : values) {
			average = calculator(row.get(3).toString(), row.get(4).toString(), row.get(5).toString());
			
			if(Integer.parseInt(row.get(2).toString()) > 15) {
				updateSheet("Reprovado por Falta", 0, "G" + index + ":H" + index);
			}
			else if(average >= 70) {
				updateSheet("Aprovado", 0,  "G" + index);
			}
			else if(average < 50) {
				updateSheet("Reprovado por Nota", 0, "G" + index + ":H" + index);
			}
			else if(5 <= average && average < 70) {

				naf = (50 * 2 ) - average;
				updateSheet("Exame Final", naf, "G" + index + ":H" + index);
			}
			index++;
		}

		System.out.println("------------------------------------------------------\n" + 
		                   "Application finished\n" + 
				           "------------------------------------------------------");
	}
	
	// It calculates the average of the grades
	public int calculator (String p1, String p2, String p3) {
		
		return ((Integer.parseInt(p1)) + (Integer.parseInt(p2)) + (Integer.parseInt(p3)))/3;
	}
	
	// Updates google sheets
	public void updateSheet(String status, float naf, String range) throws IOException, GeneralSecurityException {
		ValueRange body = new ValueRange()
				.setValues(Arrays.asList(Arrays.asList(status, naf)));
		
		UpdateValuesResponse result = sheetsService.spreadsheets().values()
				.update(SPREADSHEET_ID, range, body).
				setValueInputOption("RAW").execute();
	}
	
	


}
