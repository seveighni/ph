package printinghouse.commands;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

import printinghouse.data.PageSize;
import printinghouse.data.PaperType;
import printinghouse.data.PrintColor;
import printinghouse.data.Printer;
import printinghouse.data.PrintingHouse;

public class PrinterCommands {

    public static void addPrinter(PrintingHouse printingHouse) {
        var id = UUID.randomUUID();
        var paperCapacity = Integer.parseInt(System.console().readLine("Enter the paper capacity of the printer: "));
        var maxPrintedPagesPerMinute = Integer
                .parseInt(System.console().readLine("Enter the maximum printed pages per minute of the printer: "));
        var colorTypes = Arrays.asList(PrintColor.values()).stream().map(t -> t.toString())
                .collect(Collectors.joining(", "));
        var printColor = PrintColor.valueOf(System.console()
                .readLine("Enter the supported color (" + colorTypes + ") of the printer: "));
        var pageSizes = Arrays.asList(PageSize.values()).stream().map(t -> t.toString())
                .collect(Collectors.joining(", "));
        var pageSize = PageSize.valueOf(
                System.console().readLine("Enter the supported page size (" + pageSizes + ") of the printer: "));
        var paperTypes = Arrays.asList(PaperType.values()).stream().map(t -> t.toString())
                .collect(Collectors.joining(", "));
        var paperType = PaperType.valueOf(System.console()
                .readLine("Enter the supported paper type (" + paperTypes + ") of the printer: "));
        var printer = new Printer(id, paperCapacity, maxPrintedPagesPerMinute, printColor,
                pageSize, paperType);
        printingHouse.addPrinter(printer);
        System.out.println("Printer added successfully.");
    }
}
