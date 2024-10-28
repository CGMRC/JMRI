package jmri.jmrit.operations.rollingstock.cars.tools;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jmri.jmrit.XmlFile;
import jmri.jmrit.operations.rollingstock.cars.Car;
import jmri.jmrit.operations.setup.OperationsSetupXml;
import jmri.jmrit.operations.setup.Setup;

/**
 * Exports the car roster into a comma delimited file (CSV).
 *
 * @author Daniel Boudreau Copyright (C) 2010, 2011, 2016
 *
 */
public class ExportCars extends XmlFile {

    protected static final String LOCATION_TRACK_SEPARATOR = "-";
    List<Car> _carList;

    public ExportCars(List<Car> carList) {
        _carList = carList;
    }

    /**
     * Create CSV file based on the car list.
     */
    public void writeOperationsCarFile() {
        writeCars(defaultOperationsFilename());
        // if a removable drive is installed, save file there too
        if (Files.isDirectory(Path.of("/media/pi/"))) {
            writeCars("/media/pi/" + operationsFileName);
        }
        if (Files.isDirectory(Path.of("/Volumes/NO NAME/"))) {
            writeCars("/Volumes/NO NAME/" + operationsFileName);
        }
        if (Files.isDirectory(Path.of("/media/standards/HP v150w/"))) {
            writeCars("/media/standards/HP v150w/" + operationsFileName);
        }
    }

    private void writeCars(String path) {
        makeBackupFile(path);
        try {
            if (!checkFile(path)) {
                // The file does not exist, create it before writing
                java.io.File file = new java.io.File(path);
                java.io.File parentDir = file.getParentFile();
                if (!parentDir.exists()) {
                    if (!parentDir.mkdir()) {
                        log.error("Directory wasn't created");
                    }
                }
                if (file.createNewFile()) {
                    log.debug("File created");
                }
            }
            writeFile(path);
        } catch (IOException e) {
            log.error("Exception while writing the new CSV operations file at " +
                      path + ", may not be complete", e.gotLocalizedMessage());
        }
    }

    /**
     * Any changes to the column order should also be made to the ImportCars.java
     * file.
     *
     * @param name file name
     */
    private void writeFile(String name) {
        log.debug("writeFile {}", name);
        // This is taken in large part from "Java and XML" page 368
        File file = findFile(name);
        if (file == null) {
            file = new File(name);
        }

        try (CSVPrinter fileOut = new CSVPrinter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)),
                CSVFormat.DEFAULT)) {

            // create header
            fileOut.printRecord(Bundle.getMessage("Number"),
                    Bundle.getMessage("Road"),
                    Bundle.getMessage("Type"),
                    Bundle.getMessage("Length"),
                    Bundle.getMessage("Weight"),
                    Bundle.getMessage("Color"),
                    Bundle.getMessage("Owner"),
                    Bundle.getMessage("Built"),
                    Bundle.getMessage("Location"),
                    LOCATION_TRACK_SEPARATOR,
                    Bundle.getMessage("Track"),
                    Bundle.getMessage("Load"),
                    Bundle.getMessage("Kernel"),
                    Bundle.getMessage("Moves"),
                    Setup.getValueLabel(),
                    Bundle.getMessage("Comment"),
                    Bundle.getMessage("Miscellaneous"),
                    Bundle.getMessage("Extensions"),
                    Bundle.getMessage("Wait"),
                    Bundle.getMessage("Pickup"),
                    Bundle.getMessage("Last"),
                    Bundle.getMessage("RWELocation"),
                    LOCATION_TRACK_SEPARATOR,
                    Bundle.getMessage("Track"),
                    Bundle.getMessage("RWELoad"),
                    Bundle.getMessage("RWLLocation"),
                    LOCATION_TRACK_SEPARATOR,
                    Bundle.getMessage("Track"),
                    Bundle.getMessage("RWLLoad"),
                    Bundle.getMessage("Division"),
                    Bundle.getMessage("Train"),
                    Bundle.getMessage("Destination"),
                    LOCATION_TRACK_SEPARATOR,
                    Bundle.getMessage("Track"),
                    Bundle.getMessage("FinalDestination"),
                    LOCATION_TRACK_SEPARATOR,
                    Bundle.getMessage("Track"),
                    Bundle.getMessage( "RFID_Tag"));

            // store car attributes
            for (Car car : _carList) {
                fileOut.printRecord(car.getNumber(),
                        car.getRoadName(),
                        car.getTypeName(),
                        car.getLength(),
                        car.getWeight(),
                        car.getColor(),
                        car.getOwnerName(),
                        car.getBuilt(),
                        car.getLocationName(),
                        LOCATION_TRACK_SEPARATOR,
                        car.getTrackName(),
                        car.getLoadName(),
                        car.getKernelName(),
                        car.getMoves(),
                        car.getValue(),
                        car.getComment(),
                        car.isOutOfService() ? Bundle.getMessage("OutOfService") : "",
                        car.getTypeExtensions(),
                        car.getWait(),
                        car.getPickupScheduleName(),
                        car.getSortDate(),
                        car.getReturnWhenEmptyDestinationName(),
                        LOCATION_TRACK_SEPARATOR,
                        car.getReturnWhenEmptyDestTrackName(),
                        car.getReturnWhenEmptyLoadName(),
                        car.getReturnWhenLoadedDestinationName(),
                        LOCATION_TRACK_SEPARATOR,
                        car.getReturnWhenLoadedDestTrackName(),
                        car.getReturnWhenLoadedLoadName(),
                        car.getDivision(),
                        car.getTrainName(),
                        car.getDestinationName(),
                        LOCATION_TRACK_SEPARATOR,
                        car.getDestinationTrackName(),
                        car.getFinalDestinationName(),
                        LOCATION_TRACK_SEPARATOR,
                        car.getFinalDestinationTrackName(),
                        car.getRfid());
            }
            fileOut.flush();
            fileOut.close();
            log.info("Exported {} cars to file {}", _carList.size(), name);
            JOptionPane.showMessageDialog(null, MessageFormat.format(Bundle.getMessage("ExportedCarsToFile"), new Object[]{
                _carList.size(), name}), Bundle.getMessage("ExportComplete"),
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
<<<<<<< HEAD
            log.error("Can not open export cars CSV file: {}", file.getName());
            JOptionPane.showMessageDialog(null,
                    MessageFormat.format(Bundle.getMessage("ExportedCarsToFile"),
                            new Object[] { 0, name }),
                    Bundle.getMessage("ExportFailed"), JOptionPane.ERROR_MESSAGE);
=======
            log.error("Can not open export cars CSV file: {}", e.getLocalizedMessage());
            JmriJOptionPane.showMessageDialog(null,
                    Bundle.getMessage("ExportedCarsToFile",
                            0, defaultOperationsFilename()),
                    Bundle.getMessage("ExportFailed"), JmriJOptionPane.ERROR_MESSAGE);
>>>>>>> c5f7dcf85710f7fa0de1107ba2f546ae6311355d
        }
    }

    // Operation files always use the same directory
    public static String defaultOperationsFilename() {
        return OperationsSetupXml.getFileLocation()
                + OperationsSetupXml.getOperationsDirectoryName()
                + File.separator
                + getOperationsFileName();
    }

    public static void setOperationsFileName(String name) {
        operationsFileName = name;
    }

    public static String getOperationsFileName() {
        return operationsFileName;
    }

    private static String operationsFileName = "ExportOperationsCarRoster.csv"; // NOI18N

    private final static Logger log = LoggerFactory.getLogger(ExportCars.class);

}
