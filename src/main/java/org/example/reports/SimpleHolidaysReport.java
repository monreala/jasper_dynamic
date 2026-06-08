package org.example.reports;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalCellComponentAlignment;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.constant.VerticalCellComponentAlignment;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.example.data.HolidaysData;
import org.example.model.Holiday;

import java.awt.Color;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;
import net.sf.dynamicreports.report.constant.HorizontalImageAlignment;
import net.sf.dynamicreports.report.constant.VerticalImageAlignment;

public class SimpleHolidaysReport {

    private static final Color HEADER_BACK_COLOR = new Color(0x90, 0xA4, 0xAE);

    public JasperReportBuilder build() {

        StyleBuilder borderedStyle = stl.style()
                .setBorder(stl.pen1Point().setLineColor(Color.BLACK).setLineStyle(LineStyle.SOLID))
                .setPadding(stl.padding().setLeft(5).setRight(5))
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                .setVerticalTextAlignment(VerticalTextAlignment.MIDDLE);

        StyleBuilder imageStyle = stl.style()
                .setHorizontalImageAlignment(HorizontalImageAlignment.RIGHT)
                .setVerticalImageAlignment(VerticalImageAlignment.MIDDLE);

        StyleBuilder columnHeaderStyle = stl.style(borderedStyle)
                .bold()
                .setForegroundColor(Color.BLACK)
                .setBackgroundColor(HEADER_BACK_COLOR);

        StyleBuilder titleStyle = stl.style()
                .setFontName("Segoe UI")
                .setFontSize(24)
                .bold()
                .setHorizontalTextAlignment(HorizontalTextAlignment.LEFT)
                .setVerticalTextAlignment(VerticalTextAlignment.MIDDLE);

        StyleBuilder summaryStyle = stl.style()
                .setFontName("Segoe UI")
                .setFontSize(20)
                .bold()
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                .setVerticalTextAlignment(VerticalTextAlignment.MIDDLE);

        StyleBuilder pageHeaderStyle = stl.style()
                .setFontSize(12)
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
                .setVerticalTextAlignment(VerticalTextAlignment.MIDDLE);


        TextColumnBuilder<String> countryCol = col.column("Country", "country", type.stringType());
        TextColumnBuilder<String> nameCol    = col.column("Name",    "name",    type.stringType());
        TextColumnBuilder<Date>   dateCol    = col.column("Date",    "date",    type.dateType())
                .setPattern("d/M/yy");

        List<Holiday> data = HolidaysData.holidays2021();


        InputStream logoStream = Objects.requireNonNull(
                SimpleHolidaysReport.class.getResourceAsStream("/logo.png"),
                "logo.png not found on classpath");

        var titleBand = cmp.horizontalList()
                .add(cmp.text("Holydays").setStyle(titleStyle))
                .add(cmp.image(logoStream)
                        .setFixedDimension(120, 50)
                        .setStyle(imageStyle))
                .setFixedHeight(80);

        return report()
                .setColumnTitleStyle(columnHeaderStyle)
                .setColumnStyle(borderedStyle)
                .setColumnHeaderStyle(columnHeaderStyle)
                .highlightDetailEvenRows()
                .columns(countryCol, nameCol, dateCol)
                .title(titleBand)
                .pageHeader(cmp.text("Report Details").setStyle(pageHeaderStyle).setHeight(40))
                .pageFooter(cmp.pageXofY())
                .summary(cmp.text("End of Report").setStyle(summaryStyle).setHeight(50))
                .setDataSource(new JRBeanCollectionDataSource(data));
    }

    public void writePdf(OutputStream out) throws DRException {
        build().toPdf(out);
    }
}
