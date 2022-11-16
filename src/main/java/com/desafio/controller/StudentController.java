package com.desafio.controller;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import com.desafio.model.Student;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.desafio.DesafioApplication;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;


@RestController
@RequestMapping("/readSheet")
public class StudentController {
	
	private static final String SPREADSHEET_ID = "17uTrxMYAneyh87L9_LL1X2kIwLhMXsRz8hPmE52URBw";
	private static Sheets sheetsService;
	private static final String RANGE = "engenharia_de_software!A4:F27";
	DesafioApplication desafio;
	

	@GetMapping
	public void manipulateSheet() throws GeneralSecurityException, IOException {
		List<List<Object>> sheetResponse = readSheet();

		ClassRoom classRoom = calculatesStudentsGrades(sheetResponse);

		updateSheet(classRoom);
	}

	public ClassRoom calculatesStudentsGrades(List<List<Object>> sheetResponse) {
		ClassRoom classroom = new ClassRoom();
		classroom.buildClassRoom(sheetResponse);

		classroom.getClassRoom().stream()
				.forEach( student -> {
					student.setAverage();
					student.setSituacao();
				});

		return classroom;
	}

	public List<List<Object>> readSheet() throws IOException, GeneralSecurityException {
		sheetsService = desafio.getSheetsService();

		return desafio.getSheetsService().spreadsheets().values()
				.get(SPREADSHEET_ID, RANGE).execute().getValues();
	}

	public void updateSheet(ClassRoom classRoom) {
		int index = 4;
		for (Student student : classRoom.getClassRoom()) {
			ValueRange body = new ValueRange()
					.setValues(Arrays.asList(Arrays.asList(student.getSituacao(), student.getNotaAprovacao())));
			try {
				sheetsService.spreadsheets().values()
						.update(SPREADSHEET_ID, "G" + index + ":H" + index, body)
						.setValueInputOption("RAW").execute();
			} catch (IOException e) {
				e.printStackTrace();
			}
			index++;
		}
	}
}
