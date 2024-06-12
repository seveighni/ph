package printinghouse.commands;

import java.util.Arrays;
import java.util.stream.Collectors;

import printinghouse.data.PageSize;
import printinghouse.data.PaperType;
import printinghouse.data.PrintColor;
import printinghouse.data.PrintingHouse;
import printinghouse.data.Publication;
import printinghouse.data.PublicationType;
import printinghouse.exceptions.PrintingFailure;

public class PublicationCommands {
    public static void printPublication(PrintingHouse printingHouse) throws PrintingFailure {
        var pubTypes = Arrays.asList(PublicationType.values()).stream().map(t -> t.toString())
                .collect(Collectors.joining(", "));
        var publicationType = PublicationType
                .valueOf(System.console().readLine("Enter the type (" + pubTypes + ") of the publication: "));
        var title = System.console().readLine("Enter the title of the publication: ");
        var pageSizes = Arrays.asList(PageSize.values()).stream().map(t -> t.toString())
                .collect(Collectors.joining(", "));
        var pageSize = PageSize
                .valueOf(System.console().readLine("Enter the page size (" + pageSizes + ") of the publication: "));
        var page = Integer.parseInt(System.console().readLine("Enter the number of pages of the publication: "));
        var publication = new Publication(publicationType, title, pageSize, page);

        var paperTypes = Arrays.asList(PaperType.values()).stream().map(t -> t.toString())
                .collect(Collectors.joining(", "));
        var paperType = PaperType.valueOf(
                System.console().readLine("Enter the wanted paper type (" + paperTypes + ") for the publication: "));

        var printColors = Arrays.asList(PrintColor.values()).stream().map(t -> t.toString())
                .collect(Collectors.joining(", "));
        var printColor = PrintColor.valueOf(
                System.console().readLine("Enter the wanted print color (" + printColors + ") for the publication: "));
        var copies = Integer.parseInt(System.console().readLine("Enter the number of copies: "));
        printingHouse.printPublication(publication, copies, paperType, printColor);
        System.out.println("Publication printed successfully.");
    }
}
