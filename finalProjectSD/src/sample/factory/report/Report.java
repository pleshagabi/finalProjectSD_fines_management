package sample.factory.report;

import data.server.model.Driver;
import data.server.model.Fine;

import java.io.IOException;
import java.util.List;

/**
 * Created by plesha on 09-Apr-18.
 */
public interface Report {
    void createReportFile(List<Fine> fineList, List<Driver> driverList) throws IOException;
}
