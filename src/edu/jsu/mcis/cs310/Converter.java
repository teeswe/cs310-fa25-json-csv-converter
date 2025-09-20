package edu.jsu.mcis.cs310;

import com.github.cliftonlabs.json_simple.*;
import com.opencsv.*;
import java.io.*;
import java.util.List;


public class Converter {
    
    /*
        
        Consider the following CSV data, a portion of a database of episodes of
        the classic "Star Trek" television series:
        
        "ProdNum","Title","Season","Episode","Stardate","OriginalAirdate","RemasteredAirdate"
        "6149-02","Where No Man Has Gone Before","1","01","1312.4 - 1313.8","9/22/1966","1/20/2007"
        "6149-03","The Corbomite Maneuver","1","02","1512.2 - 1514.1","11/10/1966","12/9/2006"
        
        (For brevity, only the header row plus the first two episodes are shown
        in this sample.)
    
        The corresponding JSON data would be similar to the following; tabs and
        other whitespace have been added for clarity.  Note the curly braces,
        square brackets, and double-quotes!  These indicate which values should
        be encoded as strings and which values should be encoded as integers, as
        well as the overall structure of the data:
        
        {
            "ProdNums": [
                "6149-02",
                "6149-03"
            ],
            "ColHeadings": [
                "ProdNum",
                "Title",
                "Season",
                "Episode",
                "Stardate",
                "OriginalAirdate",
                "RemasteredAirdate"
            ],
            "Data": [
                [
                    "Where No Man Has Gone Before",
                    1,
                    1,
                    "1312.4 - 1313.8",
                    "9/22/1966",
                    "1/20/2007"
                ],
                [
                    "The Corbomite Maneuver",
                    1,
                    2,
                    "1512.2 - 1514.1",
                    "11/10/1966",
                    "12/9/2006"
                ]
            ]
        }
        
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity.
        
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and json-simple.  See the "Data
        Exchange" lecture notes for more details, including examples.
        
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String result = "{}"; // default return value; replace later!
        
        try {
        
            
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> rows = reader.readAll();

            
            String[] headers = rows.get(0);
            JsonArray colHeadings = new JsonArray();
            for (String header : headers) {
                colHeadings.add(header);
            }

            
            JsonArray prodNums = new JsonArray();
            JsonArray data = new JsonArray();

            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);
                prodNums.add(row[0]); // first column is ProdNum

                JsonArray rowData = new JsonArray();
                for (int j = 1; j < row.length; j++) {
                    String value = row[j];
                    try {
                        
                        int intValue = Integer.parseInt(value);
                        rowData.add(intValue);
                    } catch (NumberFormatException e) {
                        rowData.add(value); 
                    }
                }
                data.add(rowData);
            }

          
            JsonObject json = new JsonObject();
            json.put("ProdNums", prodNums);
            json.put("ColHeadings", colHeadings);
            json.put("Data", data);

           
            result = json.toJson();
           
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result.trim();
        
    }
    
    @SuppressWarnings("unchecked")
    public static String jsonToCsv(String jsonString) {
        
        String result = ""; // default return value; replace later!
        
        try {
            
           JsonObject json = (JsonObject) Jsoner.deserialize(jsonString);
            JsonArray colHeadings = (JsonArray) json.get("ColHeadings");
            JsonArray prodNums = (JsonArray) json.get("ProdNums");
            JsonArray data = (JsonArray) json.get("Data");

            StringWriter sw = new StringWriter();
            CSVWriter writer = new CSVWriter(sw);

            
            writer.writeNext(colHeadings.toArray(new String[0]));

            
            for (int i = 0; i < data.size(); i++) {
                JsonArray rowArray = (JsonArray) data.get(i);
                String[] rowStrings = new String[rowArray.size() + 1];
                rowStrings[0] = prodNums.get(i).toString(); // ProdNum first
                for (int j = 0; j < rowArray.size(); j++) {
                    rowStrings[j + 1] = rowArray.get(j).toString();
                }
                writer.writeNext(rowStrings);
            }

            writer.close();
            result = sw.toString();
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result.trim();
        
    }
    
}
