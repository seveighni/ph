package data;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import printinghouse.data.PageSize;
import printinghouse.data.PaperType;
import printinghouse.data.PrintColor;
import printinghouse.data.Printer;
import printinghouse.data.Publication;
import printinghouse.data.PublicationType;
import printinghouse.exceptions.InsufficientPaper;
import printinghouse.exceptions.PageSizeNotSupported;
import printinghouse.exceptions.PaperTypeNotSupported;
import printinghouse.exceptions.PrintColorNotSupported;

public class PrinterTest {
    @Test
    void testPrintNotSupportedColor() {
        var printer = new Printer(UUID.randomUUID(), 100, 10, PrintColor.BlackAndWhite, PageSize.A5,
                PaperType.Standard);
        assertDoesNotThrow(() -> printer.loadPaper(100));

        var publication = new Publication(PublicationType.Book, "A book", PageSize.A5, 50);

        assertThrows(PrintColorNotSupported.class,
                () -> printer.print(publication, PrintColor.Color, PaperType.Standard));
    }

    @Test
    void testPrintNotSupportedPaperType() {
        var printer = new Printer(UUID.randomUUID(), 100, 10, PrintColor.BlackAndWhite, PageSize.A5,
                PaperType.Standard);
        assertDoesNotThrow(() -> printer.loadPaper(100));

        var publication = new Publication(PublicationType.Book, "A book", PageSize.A5, 50);

        assertThrows(PaperTypeNotSupported.class,
                () -> printer.print(publication, PrintColor.BlackAndWhite, PaperType.Glossy));
    }

    @Test
    void testPrintNotEnoughPaper() {
        var printer = new Printer(UUID.randomUUID(), 100, 10, PrintColor.BlackAndWhite, PageSize.A5,
                PaperType.Standard);
        assertDoesNotThrow(() -> printer.loadPaper(30));

        var publication = new Publication(PublicationType.Book, "A book", PageSize.A5, 50);

        assertThrows(InsufficientPaper.class,
                () -> printer.print(publication, PrintColor.BlackAndWhite, PaperType.Standard));
    }

    @Test
    void testPrintPageSizeNotSupported() {
        var printer = new Printer(UUID.randomUUID(), 100, 10, PrintColor.BlackAndWhite, PageSize.A5,
                PaperType.Standard);
        assertDoesNotThrow(() -> printer.loadPaper(100));

        var publication = new Publication(PublicationType.Book, "A book", PageSize.A3, 50);

        assertThrows(PageSizeNotSupported.class,
                () -> printer.print(publication, PrintColor.BlackAndWhite, PaperType.Standard));
    }

    @Test
    void testPrint() {
        var printer = new Printer(UUID.randomUUID(), 100, 10, PrintColor.BlackAndWhite, PageSize.A5,
                PaperType.Standard);
        assertDoesNotThrow(() -> printer.loadPaper(100));

        var publication = new Publication(PublicationType.Book, "A book", PageSize.A5, 50);

        assertDoesNotThrow(() -> printer.print(publication, PrintColor.BlackAndWhite, PaperType.Standard));
        assertEquals(50, printer.getCurrentPaperAmount());
        assertEquals(50, printer.getNumberOfPrintedPages());
    }
}
