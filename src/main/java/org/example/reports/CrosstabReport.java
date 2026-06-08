package org.example.reports;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabColumnGroupBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabMeasureBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabRowGroupBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.Calculation;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import org.example.data.HolidaysData;
import org.example.model.Holiday;

import java.awt.Color;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.ctab;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;


public class CrosstabReport {

    private static final Color HEADER_BACK_COLOR = new Color(0x90, 0xA4, 0xAE);

    public JasperReportBuilder build() {
        StyleBuilder borderedCenter = stl.style()
                .setBorder(stl.pen1Point().setLineColor(Color.BLACK).setLineStyle(LineStyle.SOLID))
                .setPadding(stl.padding().setLeft(5).setRight(5))
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                .setVerticalTextAlignment(VerticalTextAlignment.MIDDLE);

        StyleBuilder headerStyle = stl.style(borderedCenter)
                .bold()
                .setBackgroundColor(HEADER_BACK_COLOR);

        StyleBuilder titleStyle = stl.style()
                .setFontName("Segoe UI")
                .setFontSize(18)
                .bold()
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);

        CrosstabRowGroupBuilder<String> rowGroup = ctab.rowGroup("country", String.class)
                .setHeaderStyle(headerStyle)
                .setShowTotal(true)
                .setTotalHeader("Total");

        CrosstabColumnGroupBuilder<Integer> columnGroup = ctab.columnGroup("month", Integer.class)
                .setHeaderStyle(headerStyle)
                .setShowTotal(true)
                .setTotalHeader("Tot");

        CrosstabMeasureBuilder<Integer> countMeasure = ctab.measure(
                "Count", "id", Integer.class, Calculation.COUNT);
        countMeasure.setStyle(borderedCenter);

        CrosstabBuilder crosstab = ctab.crosstab()
                .headerCell(cmp.text("State / Mese").setStyle(headerStyle))
                .rowGroups(rowGroup)
                .columnGroups(columnGroup)
                .measures(countMeasure)
                .setCellStyle(borderedCenter)
                .setDataPreSorted(false);

        return report()
                .title(cmp.text("Holidays — crosstab (anno 2021)").setStyle(titleStyle).setHeight(40))
                .summary(crosstab)
                .pageFooter(cmp.pageXofY())
                .setDataSource(new JRMapCollectionDataSource(buildMapDataSource()));
    }

    public void writePdf(OutputStream out) throws DRException {
        build().toPdf(out);
    }


    private static List<Map<String, ?>> buildMapDataSource() {
        List<Holiday> holidays = HolidaysData.holidays2021();
        List<Map<String, ?>> rows = new ArrayList<>(holidays.size()); // Изменили Object на ?
        int id = 1;
        for (Holiday h : holidays) {
            LocalDate date = h.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Map<String, Object> row = new HashMap<>();
            row.put("id", id++);
            row.put("country", h.getCountry());
            row.put("name", h.getName());
            row.put("month", date.getMonthValue());
            rows.add(row);
        }
        return rows;
    }
}
