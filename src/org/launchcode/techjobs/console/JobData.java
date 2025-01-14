package org.launchcode.techjobs.console;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * Created by LaunchCode
 */
public class JobData {

    private static final String DATA_FILE = "resources/job_data.csv";
    private static Boolean isDataLoaded = false;

    private static ArrayList<HashMap<String, String>> allJobs;

    /**
     * Fetch list of all values from loaded data,
     * without duplicates, for a given column.
     *
     * @param field The column to retrieve values from
     * @return List of all of the values of the given field
     */
    public static ArrayList<String> findAll(String field) {

        // load data, if not already loaded
        loadData();

        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> valuesLowered = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(field);

            if (!valuesLowered.contains(aValue.toLowerCase())) {
                values.add(aValue);
                valuesLowered.add(aValue.toLowerCase());
            }
        }
        Collections.sort(values, Alphabetical_Order);
        return values;
    }

    private static Comparator<String> Alphabetical_Order = new Comparator<String>() {
        public int compare(String str1, String str2) {
            int result;
            if (!Character.isLetter(str1.charAt(0))) {
                // complete comparison using str.charAt(1) instead of normal comparison
                result = String.CASE_INSENSITIVE_ORDER.compare(str1.substring(1), str2);
                if (result == 0) {
                    result = str1.compareTo(str2);
                }
            } else if (!Character.isLetter(str2.charAt(0))) {
                // complete comparison using str.charAt(1) instead of normal comparison
                result = String.CASE_INSENSITIVE_ORDER.compare(str1, str2.substring(1));
                if (result == 0) {
                    result = str1.compareTo(str2);
                }
            } else {
                // complete with normal compare method below
                result = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
                if (result == 0) {
                    result = str1.compareTo(str2);
                }

            }
            return result;
        }
    };

    public static ArrayList<HashMap<String, String>> findAll() {
        // dummy edit so I can push to LC GitHub Classroom repo...

        // load data, if not already loaded
        loadData();

        ArrayList<HashMap<String, String>> allJobsConsumable = new ArrayList(allJobs);

        return allJobsConsumable;
    }

    /**
     * Returns results of search the jobs data by key/value, using
     * inclusion of the search term.
     *
     * For example, searching for employer "Enterprise" will include results
     * with "Enterprise Holdings, Inc".
     *
     * @param column   Column that should be searched.
     * @param value Value of the field to search for
     * @return List of all jobs matching the criteria
     */
    public static ArrayList<HashMap<String, String>> findByColumnAndValue(String column, String value) {

        // load data, if not already loaded
        loadData();

        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();
        ArrayList<String> jobsLowered = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(column);

            if (aValue.toLowerCase().contains(value.toLowerCase())) {
                    jobs.add(row);
                    jobsLowered.add(row.get(column).toLowerCase());
            }
        }

        return jobs;
    }

    public static ArrayList<HashMap<String, String>> findByValue(String term) {
        // load data if we need to
        loadData();

        ArrayList<HashMap<String, String>> results = new ArrayList<>();

        // do the search
        for (HashMap<String, String> job : allJobs) {
            for (Map.Entry<String, String> data : job.entrySet()) {
                if (data.getValue().toLowerCase().contains(term.toLowerCase())) {
                    if (!results.contains(job)) {
                        results.add(job);
                    }
                }
            }
        }

        // exclude duplicates

        return results;
    }

    /**
     * Read in data from a CSV file and store it in a list
     */
    private static void loadData() {

        // Only load data once
        if (isDataLoaded) {
            return;
        }

        try {

            // Open the CSV file and set up pull out column header info and records
            Reader in = new FileReader(DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> records = parser.getRecords();
            Integer numberOfColumns = records.get(0).size();
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);

            allJobs = new ArrayList<>();

            // Put the records into a more friendly format
            for (CSVRecord record : records) {
                HashMap<String, String> newJob = new HashMap<>();

                for (String headerLabel : headers) {
                    newJob.put(headerLabel, record.get(headerLabel));
                }

                allJobs.add(newJob);
            }

            // flag the data as loaded, so we don't do it twice
            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }
    }

}
