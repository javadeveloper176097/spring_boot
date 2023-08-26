package dev.spring_handsOn.crud_Helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

import dev.spring_handsOn.crud.model.Product;

public class CSVHelperDownload {
	
	public static ByteArrayInputStream tutorialsToCSV(List<Product> tutorials) {
	    final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

	    try (ByteArrayOutputStream out = new ByteArrayOutputStream();
	        CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
	    	csvPrinter.printRecord("id", "Name", "Observation","Uuid","Currency","Quantity","Price");
	    	
	      for (Product tutorial : tutorials) {
	        List<String> data = Arrays.asList(
	              String.valueOf(tutorial.getId()),
	              tutorial.getName(),
	              tutorial.getObservation(),
	              tutorial.getUuid(),
	              tutorial.getCurrency(),
	              String.valueOf(tutorial.getQuantity()),
	              String.valueOf(tutorial.getPrice())
	            );

	        csvPrinter.printRecord(data);
	      }

	      csvPrinter.flush();
	      return new ByteArrayInputStream(out.toByteArray());
	    } catch (IOException e) {
	      throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
	    }
	  }
}
