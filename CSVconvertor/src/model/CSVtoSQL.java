package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

public class CSVtoSQL {	
	private static File CSVFile = new File("sample.csv");
	private static BufferedReader read;
	private static BufferedWriter write;
	
	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub
		CSVtoSQL parse = new CSVtoSQL();
        parse.convert();

        System.exit(0);
		    
	}
	private void convert() throws ParseException{
		  try {
	          read = new BufferedReader(new FileReader(CSVFile));
	          String outputName = CSVFile.toString().substring(0, CSVFile.toString().lastIndexOf(".")) + ".sql"; 
	          write = new BufferedWriter(new FileWriter(new File(outputName)));
	          
	          String line;
	          String columns[]; //contains column names
	          int num_cols;
	          String tokens[];
	          String timeTokens[];
	          
	          int progress = 0; //check progress
	          
	          //initialize columns
	          line = read.readLine(); 
	          columns = line.split(",");
	          num_cols = columns.length;        
	          line = read.readLine();
	          write.write("CREATE TABLE melbourne_parking ("
	            		+ "deviceId int NOT NULL, "
	            		+ "arrivalTime TIMESTAMP DEFAULT NULL,"
	            		+ "departureTime TIMESTAMP DEFAULT NULL,"
	            		+ "durationSeconds BIGINT DEFAULT NULL,"
	            		+ "streetMarker VARCHAR(50) DEFAULT NULL,"
	            		+ "sign VARCHAR(50) DEFAULT NULL,"
	            		+ "area VARCHAR(50) DEFAULT NULL,"
	            		+ "streetId BIGINT DEFAULT NULL,"
	            		+ "streetName VARCHAR(50) DEFAULT NULL,"
	            		+ "betweenStreet1 VARCHAR(50) DEFAULT NULL,"
	            		+ "betweenStreet2 VARCHAR(50) DEFAULT NULL,"
	            		+ "sideOfStreet int DEFAULT NULL,"
	            		+ "inViolation VARCHAR(50) DEFAULT NULL"
	            		+ ");"+"\r\n");
	            
	          write.write("INSERT INTO melbourne_parking (deviceId, arrivalTime, departureTime, durationSeconds, streetMarker, sign, area, streetId, streetName, betweenStreet1, betweenStreet2, sideOfStreet, inViolation) VALUES"+"\r\n");
        	 
	          
	          int rowNum =1;

	          while(true) {
	        	  tokens = line.split(",",-1);
	        	  
	        	 
	        	  if (tokens.length == num_cols){
	        		  write.write("(");
	        		  for (int k = 0; k < 3; ++k){ //for each column
	        			  
	        			  if (tokens[k].length()==0) {
	        				  write.write("\'none\',");
	        			  }
	        			  else if (tokens[k].matches("^-?[0-9]*\\.?[0-9]*$")){ //if a number
	        				  write.write(tokens[k]);
	        				  if (k < num_cols - 1) write.write(", "); 
	        			  }
	                      else { //if a string
	                    	  if (k==2||k==1) {
	                    		  DateFormat  twentyFour = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	                    		  DateFormat  twelve = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
	                    	      Date date = new Date();
	                    	      String dateString = tokens[k];
	                    	      date = twelve.parse(dateString);	                    	    
	                    	      tokens[k] = twentyFour.format(date);
	                    	     // date = twentyFour.parse(tokens[k]);
	                    	      write.write("\'"+ tokens[k] + "\'");
	                    	  }
	                    	  else {
	                    		  write.write("\'" + tokens[k] + "\'");	  
	                    	  }
	                          if (k < num_cols - 1) write.write(", ");
	                      }
	        		  }
	        		  ++progress; //progress update
	        		  if (progress % 10000 == 0) 
	        			  System.out.println(progress); //print progress           


	        		  if((line = read.readLine()) != null){//if not last line
	        			  write.write("),");
	        			  write.newLine();
	        		  }
	        		  else{
	        			  write.write(");");//if last line
	        			  write.newLine();
	        			  break;
	        		  }
	        		  
	             
	        	  }
	              
        		  else{
                      //line = read.readLine(); //read next line if wish to continue parsing despite error 
                              
                      System.exit(-1); //error message
                  }
	          }
	          write.close();
	          read.close();          
		  }catch(IOException e) {
			  e.printStackTrace();
		  }

	}
}
