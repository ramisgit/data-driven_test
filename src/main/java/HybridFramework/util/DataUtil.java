package HybridFramework.util;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DataUtil {
	static int jobserveExtractedValues = 12;
	static XSSFWorkbook workbook;
	public static Object[][] getTestData(String testName, Xls_Reader xls){
		String DATA_SHEET = Constants.DATA_SHEET;
		int testStartRowNum = 1;
		while (!xls.getCellData(DATA_SHEET, 0, testStartRowNum).equals(testName)) {
			testStartRowNum++;
		}
		System.out.println("Row number of test is " + testStartRowNum);
		int colStartRowNum = testStartRowNum + 1;
		int totalCols = 0;
		while (!xls.getCellData(DATA_SHEET, totalCols, colStartRowNum).equals("")) {
			totalCols++;
		}
		int dataStartRowNumber = testStartRowNum + 2;
		int totalRows = 0;
		while (!xls.getCellData("Data", 0, dataStartRowNumber).equals("")) {
			totalRows++;
			dataStartRowNumber++;
		}
		System.out.println("Total rows - " + totalRows);
		//read the data
		dataStartRowNumber = testStartRowNum + 2;
		Hashtable <String, String> table = null;
		int finalRows = dataStartRowNumber+totalRows;
		Object[][] myData = new Object[totalRows][1];
		int i = 0;
		for (int rNum = dataStartRowNumber; rNum < finalRows; rNum++) {
			table = new Hashtable<String, String>();
			for (int cNum = 0; cNum < totalCols; cNum++) {
				String data = xls.getCellData(DATA_SHEET, cNum, rNum);
				String key = xls.getCellData(DATA_SHEET, cNum, colStartRowNum);
				//System.out.println(key + " --- " + data);
				table.put(key, data);
			}
			System.out.println(table);
			myData[i][0] = table;
			i++;
			System.out.println("-----------------");
		}
		return myData;
	}
	public static boolean isSkip(String testName, Xls_Reader xls) {
		int rows = xls.getRowCount(Constants.TESTCASES_SHEET);
		for (int rNum = 2; rNum <= rows; rNum++) {
			String tcid = xls.getCellData(Constants.TESTCASES_SHEET, Constants.TCID_COL, rNum);
			if (tcid.equals(testName)) {
				//test is found
				String runmode = xls.getCellData(Constants.TESTCASES_SHEET, Constants.RUNMODE_COL, rNum);
				if (runmode.equals("N"))
					return true;
				else
					return false;
			}
		}
		return true;
	}
	
	public static void writeToXls(Map<String, Object[]> dataMap, String resultsXlsx) throws FileNotFoundException, IOException {
		workbook = new XSSFWorkbook();
    	XSSFSheet sheet = workbook.createSheet("Data");
    	Object[][] dataToStore = new Object[dataMap.size()][jobserveExtractedValues];
    	for (int i = 0; i < dataMap.size(); i++) {
    		Object[] object = dataMap.get(Integer.toString(i+1));
    		for (int j = 0; j < object.length; j++) {
    			String input = null;
				if (object[j] != null) {
					input = object[j].toString();
					dataToStore[i][j] = input.toString();
				}else {		}
    		}
    	}

    	int rowCount = 0;
     
    	for (Object[] aBook : dataToStore) {
    		Row row = sheet.createRow(rowCount++);
    		
    		int columnCount = 0;
         
    		for (Object field : aBook) {
    			Cell cell = row.createCell(columnCount++);
    			if (field instanceof String) {
    				cell.setCellValue((String) field);
    			} else if (field instanceof Integer) {
    				cell.setCellValue((Integer) field);
    			}
    		}
         
    	}
  
    	try (FileOutputStream outputStream = new FileOutputStream(resultsXlsx)) {
    		rearrangeXls(sheet);
    		workbook.write(outputStream);
    		System.out.println("Excel written successfully..");
    	}
	
	}
	public static void rearrangeXls(XSSFSheet sheet) {
		
		
		for (int numberOfSentences = 2; sheet.getPhysicalNumberOfRows() < numberOfSentences; numberOfSentences++) {
			int height = sheet.getRow(numberOfSentences).getCell(3).getStringCellValue().split("\r\n|\r|\n").length;
			sheet.getRow(numberOfSentences).setHeight((short)(height * sheet.getDefaultRowHeight()));
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
