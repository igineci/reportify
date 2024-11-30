package melany.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class responsible for making reports for FTP.
 * Features: creating directories, moving files, generating files.
 *
 * @author andjela.djekic
 */
public class FileManager {

    private static final String EXPORT_DIR = "C:\\YOUR\\EXPORT\\DIR";
    private static final String SENT_DIR = "C:\\YOUR\\SENT\\DIR";

    public FileManager() {
        createDirectories();
    }

    /**
     * Generating file name based on report prefix and current date and time
     *
     * @param prefix      prefix for recognizing different reports (for example CUS for Customer Report, ..)
     * @param jsonContent Report Content
     * @return {@link File} - file in which report will be saved
     */
    public File generateFile(String prefix, String jsonContent) {

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm");

        String timeStamp = now.format(formatter);
        String fileName = prefix + "_" + timeStamp + ".json";

        File file = new File(EXPORT_DIR + fileName);

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(jsonContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    /**
     * Method for saving JSON content - Report data into a file.
     *
     * @param jsonContent JSON Report for sending on FTP
     * @param fileName    name of the file
     * @throws IOException
     */
    public void saveJsonToFile(String jsonContent, String fileName) throws IOException {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(jsonContent);
        }
    }

    /**
     * Creating directories for FTP.
     */
    private void createDirectories() {
        new File(EXPORT_DIR).mkdirs();
        new File(SENT_DIR).mkdirs();
    }

    /**
     * Moving created JSON file to directory "Sent".
     *
     * @param file the file that needs to be moved.
     * @throws IOException
     */
    public void moveFileToSent(File file) throws IOException {
        Path sourcePath = file.toPath();
        Path destinationPath = Path.of(SENT_DIR + file.getName());
        Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
    }

}
