package com.employeepayroll;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.IntStream;

public class EmployeePayrollServiseTest
{
    private  static String HOME = System.getProperty("user.dir");
    private  static String PLAY_WITH_NIO = "TempPlayGround";


    @Test
    public void givenPathCheckedThenConfirm() throws IOException
    {
        //check file exists
        Path homePath = Paths.get(HOME);
        Assertions.assertTrue(Files.exists(homePath));

        //Delete File an Check File Not Exist
        Path playPath = Paths.get(HOME + "/" + PLAY_WITH_NIO);
        if (Files.exists(playPath)) Files.delete(playPath);
        Assertions.assertTrue(Files.notExists(playPath));

        //Create Directory
        Files.createDirectories(playPath);
        Assertions.assertTrue(Files.exists(playPath));

        //create File
        IntStream.range(1, 10).forEach(cntr -> {
            Path tempFile = Paths.get(playPath + "/temp" + cntr);
            Assertions.assertTrue(Files.notExists(tempFile));
            try
            {
                Files.createFile(tempFile);
            }
            catch (IOException e) {}
            Assertions.assertTrue(Files.exists(tempFile));
        });

        //List Files, Directories as well as Files with Extension
        Files.list(playPath).filter(Files::isRegularFile).forEach(System.out::println);
        Files.newDirectoryStream(playPath).forEach(System.out::println);
        Files.newDirectoryStream(playPath, path -> path.toFile().isFile() && path.toString().startsWith("temp")).forEach(System.out::println);
    }

    @Test
    public void givenDirectoryWhenWatchedListTheActivities() throws IOException
    {
        Path dir = Paths.get(HOME+ "/" + PLAY_WITH_NIO);
        Files.list(dir).filter(Files::isRegularFile)
                .forEach(System.out::println);
        new EmployeeWatchServices(dir).processEvents();

    }
    @Test
    public void given3EmployeesWhenWrittenToFileShouldMatchEmployeeEntries()
    {
        EmployeePayrollData[] arrayOfEmps = {
                new EmployeePayrollData(1,"jelf Bezos", 108008.0),
                new EmployeePayrollData(2, "Bill Gates", 208008.0),
                new EmployeePayrollData(3, " Mark Zuckerbarg", 30800.0)
        };
        EmployeePayrollServices employeePayrollServices;
        employeePayrollServices = new EmployeePayrollServices(Arrays.asList(arrayOfEmps));
        employeePayrollServices.writeEmployeePayrollData(EmployeePayrollServices.IOServices.FILE_IO);
        employeePayrollServices.printData(EmployeePayrollServices.IOServices.FILE_IO);
        long entries = employeePayrollServices.countEntries(EmployeePayrollServices.IOServices.FILE_IO);
        Assertions.assertEquals(3, entries);
    }
}
