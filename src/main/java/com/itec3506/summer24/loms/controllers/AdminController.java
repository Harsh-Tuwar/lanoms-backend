package com.itec3506.summer24.loms.controllers;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private HikariDataSource hikariDataSource;

    String database = null;

    @PreAuthorize("hasAuthority('ROLE_SUPER_USER') || hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_SUPER_ADMIN')")
    @PostMapping("/backup/create")
    public ResponseEntity<HashMap<String, Object>> createBackup(
            HttpServletRequest request
    ) {
        HashMap<String, Object> resp = new HashMap<>();
        String userHome = System.getProperty("user.home");
        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();

        // Create a formatter for a filename-safe format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String backupName = "lanoms_backup_" + now.format(formatter) + ".sql";
        String fileName = userHome + File.separator + "/lanoms-backups/" + backupName;
        Path sqlFile = Path.of(fileName);

        File file = new File(fileName);

        try {
            file.getParentFile().mkdirs();

            if (file.createNewFile()) {
                System.out.println("File created successfully: " + file.getAbsolutePath());
            } else {
                System.out.println("File already exists: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating the file.");
            e.printStackTrace();
        }

        try {
            // Execute SQL to get the name of the current database
            try (Connection connection = hikariDataSource.getConnection()) {
                try (ResultSet resultSet = connection.createStatement().executeQuery("SELECT DATABASE();")) {
                    if (resultSet.next()) {
                        database = resultSet.getString(1);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            // stdErr
            ByteArrayOutputStream stdErr = new ByteArrayOutputStream();


            // stdOut
            OutputStream stdOut = new BufferedOutputStream(Files.newOutputStream(sqlFile, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING));

            try (stdErr; stdOut) {

                // Watchdog. The execution timeout is 1 hour.
                ExecuteWatchdog watchdog = new ExecuteWatchdog(TimeUnit.HOURS.toMillis(1));

                DefaultExecutor defaultExecutor = new DefaultExecutor();
                defaultExecutor.setWatchdog(watchdog);
                defaultExecutor.setStreamHandler(new PumpStreamHandler(stdOut, stdErr));

                CommandLine commandLine = new CommandLine("mysqldump");
                commandLine.addArgument("-u" + hikariDataSource.getUsername()); // username
                commandLine.addArgument("-p" + hikariDataSource.getPassword()); // password
                commandLine.addArgument(database); // database

                System.out.println("Exporting SQL data...");

                // Synchronous execution. Blocking until the execution of the child process is complete.
                int exitCode = defaultExecutor.execute(commandLine);

                if(defaultExecutor.isFailure(exitCode) && watchdog.killedProcess()) {
                    System.out.println("timeout...");
                }

                resp.put("done", "SQL data export completed: exitCode= " + exitCode + ", sqlFile= " + sqlFile.toString());
            } catch (Exception e) {
                System.out.println("SQL data export exception: " + e.getMessage());
                System.out.println("std err: " + stdErr.toString());
            }

            resp.put("os", System.getProperty("os.name"));
            resp.put("status", HttpStatus.OK.value());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            resp.put("error", e.getMessage());
            resp.put("causedBy", e.getCause());
        }

        return ResponseEntity.ok(resp);
    }
}
