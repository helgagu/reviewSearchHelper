package is.hgo2.reviewSearchHelper;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import is.hgo2.reviewSearchHelper.amazonMessages.Arguments;
import is.hgo2.reviewSearchHelper.amazonMessages.ItemSearchResponse;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CsvFileMaker {
    static DateFormat dateStamp = new SimpleDateFormat("yyyyMMddHHmm");
    final BinSearch binSearch = new BinSearch(null);

    public static void closeCsvFile(CSVWriter pw) throws Exception {
        pw.flush();
        pw.close();
    }

    public static String[] addTimestampAndSearchParametersToRow(String[] row, ItemSearchResponse response) {

        for (Arguments.Argument argument : response.getOperationRequest().getArguments().getArgument()) {
            if (argument.getName().equalsIgnoreCase(Constants.TIMESTAMP_PARAMETER)) {
                row[Constants.RESULTTIMESTAMP_ARRAYINDEX_BINLIST_CSV] = argument.getValue();
            } else if (argument.getName().equalsIgnoreCase(Constants.SEARCHINDEX_PARAMETER)) {
                row[Constants.SEARCHINDEX_ARRAYINDEX_BINLIST_CSV] = argument.getValue();
            } else if (argument.getName().equalsIgnoreCase(Constants.SORT_PARAMETER)) {
                row[Constants.SORT_ARRAYINDEX_BINLIST_CSV] = argument.getValue();
            } else if (argument.getName().equalsIgnoreCase(Constants.POWER_PARAMETER)) {
                row[Constants.POWERSEARCH_ARRAYINDEX_BINLIST_CSV] = argument.getValue();
            } else if (argument.getName().equalsIgnoreCase(Constants.AVAILABILITY_PARAMETER)) {
                row[Constants.AVAILABILITY_ARRAYINDEX_BINLIST_CSV] = argument.getValue();
            } else if (argument.getName().equalsIgnoreCase(Constants.MERCHANTID_PARAMETER)) {
                row[Constants.MERCHANTID_ARRAYINDEX_BINLIST_CSV] = argument.getValue();
            }
        }

        return row;
    }

    public static Map<String, String[]> getCsvRows() throws Exception {

        Map<String, String[]> csvRow = new HashMap<String, String[]>();
        CSVReader reader = new CSVReader(new FileReader(Constants.PATH_TO_SAVE_CSV + Constants.BINLIST_FILENAME));
        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            csvRow.put(nextLine[Constants.BROWSENODEID_ARRAYINDEX_BINLIST_CSV], nextLine);
        }

        return csvRow;
    }

    public static Map<String, String[]> updateCsvFile() throws Exception {

        Boolean isFile = new File(Constants.PATH_TO_SAVE_CSV + Constants.BINLIST_FILENAME).isFile();
        if (isFile) {
            return getCsvRows();
        }

        return null;
    }

    public static CSVWriter getCsvWriter() throws Exception {
        CSVWriter pw = new CSVWriter(new FileWriter(Constants.PATH_TO_SAVE_CSV + Constants.BINLIST_FILENAME + "_" + dateStamp.format(new Date()) + Constants.CSV_FILEEND));

        return pw;
    }
}