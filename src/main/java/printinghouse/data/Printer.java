package printinghouse.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import printinghouse.exceptions.InsufficientPaper;
import printinghouse.exceptions.PageSizeNotSupported;
import printinghouse.exceptions.PaperCapacityExceeded;
import printinghouse.exceptions.PaperTypeNotSupported;
import printinghouse.exceptions.PrintColorNotSupported;

public class Printer implements Serializable {
    private UUID id;
    private int paperCapacity;
    private int currentPaperAmount;
    private int maxPrintedPagesPerMinute;
    private PrintColor supportedPrintColor;
    private PageSize supportedPageSize;
    private PaperType supportedPaperType;

    private List<Publication> printedPublications;

    public Printer(
            UUID id,
            int paperCapacity,
            int maxPrintedPagesPerMinute,
            PrintColor supportedPrintColor,
            PageSize supportedPageSize,
            PaperType supportedPaperType) {
        this.id = id;
        if (paperCapacity < 0) {
            throw new IllegalArgumentException("Paper capacity cannot be negative");
        }
        this.paperCapacity = paperCapacity;
        this.currentPaperAmount = 0;
        if (maxPrintedPagesPerMinute <= 0) {
            throw new IllegalArgumentException("Max printed pages per minute must be greater than 0");
        }
        this.maxPrintedPagesPerMinute = maxPrintedPagesPerMinute;
        this.supportedPrintColor = supportedPrintColor;
        this.supportedPageSize = supportedPageSize;
        this.supportedPaperType = supportedPaperType;
        this.printedPublications = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public int getCurrentPaperAmount() {
        return currentPaperAmount;
    }

    public int getPaperCapacity() {
        return paperCapacity;
    }

    public void loadPaper(int amount) throws PaperCapacityExceeded {
        if (this.getCurrentPaperAmount() + amount > this.getPaperCapacity()) {
            throw new PaperCapacityExceeded("Cannot load more paper than the printer's capacity");
        }
        currentPaperAmount += amount;
    }

    public void print(Publication publication, PrintColor printColor, PaperType paperType)
            throws PrintColorNotSupported, PageSizeNotSupported, PaperTypeNotSupported, InsufficientPaper {
        if (printColor != this.supportedPrintColor) {
            throw new PrintColorNotSupported("Printer does not support the requested print color");
        }

        if (publication.getPageSize() != this.supportedPageSize) {
            throw new PageSizeNotSupported("Printer does not support the requested page size");
        }

        if (paperType != this.supportedPaperType) {
            throw new PaperTypeNotSupported("Printer does not support the requested paper type");
        }

        if (publication.getPageCount() > this.getCurrentPaperAmount()) {
            throw new InsufficientPaper("Not enough paper in the printer to print the publication");
        }

        this.currentPaperAmount -= publication.getPageCount();
        this.printedPublications.add(publication);
    }

    public int getNumberOfPrintedPages() {
        int count = 0;
        for (Publication publication : printedPublications) {
            count += publication.getPageCount();
        }
        return count;
    }

    public PageSize getSupportedPageSize() {
        return supportedPageSize;
    }

    public PrintColor getSupportedPrintColor() {
        return supportedPrintColor;
    }

    public PaperType getSupportedPaperType() {
        return supportedPaperType;
    }

    public List<Publication> getPrintedPublications() {
        return printedPublications;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Paper capacity: " + paperCapacity
                + ", Pages per minute: " + maxPrintedPagesPerMinute + ", Print color: "
                + supportedPrintColor + ", Page size: " + supportedPageSize + ", Paper type: "
                + supportedPaperType + ", Total printed pages: " + getNumberOfPrintedPages();
    }
}
