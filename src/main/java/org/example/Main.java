package org.example;

import org.example.data.H2Database;
import org.example.reports.BarChartReport;
import org.example.reports.CrosstabReport;
import org.example.reports.SimpleHolidaysReport;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws Exception {
        Path outputDir = Paths.get("output");
        Files.createDirectories(outputDir);

        writePdf(outputDir.resolve("01-holidays.pdf"),         new SimpleHolidaysReport()::writePdf);
        writePdf(outputDir.resolve("02-holidays-crosstab.pdf"), new CrosstabReport()::writePdf);

        try (H2Database db = new H2Database()) {
            try (OutputStream out = Files.newOutputStream(outputDir.resolve("03-holidays-barchart.pdf"))) {
                new BarChartReport().writePdf(out, db);
            }
        }

        System.out.println("Reports written to " + outputDir.toAbsolutePath());
    }

    @FunctionalInterface
    private interface PdfWriter {
        void writePdf(OutputStream out) throws Exception;
    }

    private static void writePdf(Path file, PdfWriter writer) throws Exception {
        try (OutputStream out = Files.newOutputStream(file)) {
            writer.writePdf(out);
        }
    }
}
