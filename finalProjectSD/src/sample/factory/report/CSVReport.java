package sample.factory.report;

import data.server.model.Driver;
import data.server.model.Fine;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by plesha on 09-Apr-18.
 */

public class CSVReport implements Report {
    @Override
    public void createReportFile(List<Fine> fineList, List<Driver> driverLis){

        //CSV file header
        final String DRIVER_HEADER = "\n\n\nID DRIVER, NAME, AGE, ADDRESS, E-MAIL, PHONE NUMBER\n";
        final String FINE_HEADER = "\nID FINE, DATE CRIME COMMITTED, CRIME TYPE, PRICE, DEADLINE DATE, ID DRIVER, IS PAID\n";

        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter("E:\\Police Office Report.csv");

            java.util.Date utilDate = new java.util.Date();
            java.sql.Date sqlCurrentDate = new java.sql.Date(utilDate.getTime());

            Boolean printOnce;

            for (Driver driver : driverLis) {
                printOnce = false;
                for (Fine fine : fineList) {
                    if (fine.getIsPayd() == 0 && (fine.getDriver_idDriver() == driver.getIdDriver())) {
                        if (fine.getDeadlineDate().compareTo(sqlCurrentDate) < 0) {
                            if (!printOnce) {
                                fileWriter.append(DRIVER_HEADER);
                                fileWriter.append(Integer.toString(driver.getIdDriver()) + ",");
                                fileWriter.append(driver.getName() + ",");
                                fileWriter.append(Integer.toString(driver.getAge()) + ",");
                                fileWriter.append(driver.getAddress() + ",");
                                fileWriter.append(driver.getEmail() + ",");
                                fileWriter.append(driver.getPhoneNumber() + ",");
                                fileWriter.append(FINE_HEADER);
                                printOnce = true;
                            }
                            fileWriter.append(Integer.toString(fine.getIdFine()) + ",");
                            fileWriter.append((fine.getDateFineCommited()).toString() + ",");
                            fileWriter.append(fine.getCrimeType() + ",");
                            fileWriter.append(Integer.toString(fine.getPrice()) + ",");
                            fileWriter.append((fine.getDeadlineDate()).toString() + ",");
                            fileWriter.append(Integer.toString(fine.getDriver_idDriver()) + ",");
                            fileWriter.append("\n");

                           // fileWriter.append("\n_____________________________________________________\n");
                        }
                    }
                }
            }


        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {

            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }

        }
    }

}
