package org.example.reports;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.chart.BarChartBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.data.JRResultSetDataSource;
import org.example.data.H2Database;

import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import static net.sf.dynamicreports.report.builder.DynamicReports.cht;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

/**
 * Bar chart report (#3) — number of Holidays per month, series by country.
 * Источник данных: JRResultSetDataSource из H2.
 *
 * Исходный SQL из задания:
 *   SELECT country, name, data FROM specialdate WHERE TO_CHAR(data, 'YYYY') = '2021'
 * Для агрегированного chart используем эквивалентный сгруппированный запрос
 * (Jasper умеет агрегировать и сырые данные, но pre-aggregation короче и нагляднее).
 */
public class BarChartReport {

    private static final String CHART_SQL = """
            SELECT MONTHNAME(data)  AS month_name,
                   MONTH(data)      AS month_no,
                   country          AS country,
                   COUNT(*)         AS cnt
            FROM specialdate
            WHERE YEAR(data) = 2021
            GROUP BY MONTH(data), MONTHNAME(data), country
            ORDER BY MONTH(data), country
            """;

    public JasperReportBuilder build(H2Database db) throws SQLException {
        ResultSet rs = db.executeQuery(CHART_SQL);

        StyleBuilder titleStyle = stl.style()
                .setFontSize(16)
                .bold()
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);

        TextColumnBuilder<String>  monthColumn   = col.column("Month",   "MONTH_NAME", type.stringType());
        TextColumnBuilder<String>  countryColumn = col.column("Country", "COUNTRY",    type.stringType());
        TextColumnBuilder<Integer> countColumn   = col.column("Count",   "CNT",        type.integerType());

        BarChartBuilder chart = cht.barChart()
                .setTitle("Holidays")
                .setTitleFont(stl.fontArialBold().setFontSize(14))
                .setCategory(monthColumn)
                .series(cht.serie(countColumn).setSeries(countryColumn))
                .setShowLegend(true)
                .setCategoryAxisFormat(cht.axisFormat().setLabel("Country"))
                .setValueAxisFormat(cht.axisFormat().setLabel(""))
                .setShowValues(true);

        return report()
                .title(cmp.text("Holidays per month (2021)").setStyle(titleStyle).setHeight(40))
                .summary(chart)
                .pageFooter(cmp.pageXofY())
                .setDataSource(new JRResultSetDataSource(rs));
    }

    public void writePdf(OutputStream out, H2Database db) throws DRException, SQLException {
        build(db).toPdf(out);
    }
}
