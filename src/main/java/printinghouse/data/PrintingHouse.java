package printinghouse.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import printinghouse.exceptions.PrintingFailure;

public class PrintingHouse implements Serializable {
    private String name;
    private PricingModel pricingModel;
    private BigDecimal baseSalary;
    private BigDecimal bonusThreshold;
    private BigDecimal bonusPercentage;

    private HashMap<UUID, Employee> employees;
    private HashMap<UUID, Printer> printers;
    private PaperStore paperStore;

    private BigDecimal totalBoughtPaperPrice;

    private BigDecimal totalIncome;

    public PrintingHouse(String name, PricingModel pricingModel, PaperStore store, BigDecimal baseSalary,
            BigDecimal bonusThreshold, BigDecimal bonusPercentage) {
        this.name = name;
        this.pricingModel = pricingModel;
        this.paperStore = store;
        if (baseSalary.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Base salary cannot be negative");
        }
        this.baseSalary = baseSalary;
        if (bonusThreshold.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Bonus threshold cannot be negative");
        }
        this.bonusThreshold = bonusThreshold;
        if (bonusPercentage.compareTo(BigDecimal.ZERO) < 0 || bonusPercentage.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException("Bonus percentage must be between 0 and 1");
        }
        this.bonusPercentage = bonusPercentage;

        employees = new HashMap<>();
        printers = new HashMap<>();
        totalBoughtPaperPrice = BigDecimal.ZERO;
        totalIncome = BigDecimal.ZERO;
    }

    public void addEmployee(Employee employee) {
        employees.put(employee.getId(), employee);
    }

    public void fireEmployee(UUID id) {
        employees.remove(id);
    }

    public void addPrinter(Printer printer) {
        printers.put(printer.getId(), printer);
    }

    public BigDecimal printPublication(Publication publication, int count, PaperType paperType, PrintColor printColor)
            throws PrintingFailure {
        // check if there is a suitable printer
        var printerOpt = printers.values().stream().filter(p -> p.getSupportedPageSize() == publication.getPageSize() &&
                p.getPaperCapacity() >= publication.getPageCount() &&
                p.getSupportedPaperType() == paperType &&
                p.getSupportedPrintColor() == printColor).findFirst();

        if (printerOpt.isEmpty()) {
            throw new PrintingFailure("The printing house does not have a suitable printer for the publication");
        }
        var applyDiscount = count > pricingModel.getOrderDiscountThreshold();
        BigDecimal orderPrice = BigDecimal.ZERO;
        try {
            var singlePagePrice = pricingModel.calculatePrizePerPage(paperType, publication.getPageSize(),
                    applyDiscount);
            orderPrice = singlePagePrice.multiply(BigDecimal.valueOf(publication.getPageCount()))
                    .multiply(BigDecimal.valueOf(count));
        } catch (Exception e) {
            throw new PrintingFailure("The printing house cannot calculate the price for printing the publication", e);
        }

        var printer = printerOpt.get();

        var sheetsToBuy = (publication.getPageCount() * count) - printer.getCurrentPaperAmount();
        BigDecimal boughtPaperPrice;
        try {
            boughtPaperPrice = paperStore.buyPaper(paperType, publication.getPageSize(), sheetsToBuy);
        } catch (Exception e) {
            throw new PrintingFailure("The printing house cannot buy the needed paper for the publication", e);
        }
        totalBoughtPaperPrice = totalBoughtPaperPrice.add(boughtPaperPrice);

        for (int i = 0; i < count; i++) {
            var sheetsToLoad = printer.getPaperCapacity() - printer.getCurrentPaperAmount();
            try {
                printer.loadPaper(sheetsToLoad);
            } catch (Exception e) {
                throw new PrintingFailure("Unexpected failure while loading paper in the printer", e);
            }

            try {
                printer.print(publication, printColor, paperType);
            } catch (Exception e) {
                throw new PrintingFailure("Unexpected failure while printing the publication", e);
            }
        }

        totalIncome = totalIncome.add(orderPrice);
        return orderPrice;
    }

    public List<Employee> getEmployees() {
        return new ArrayList<>(employees.values());
    }

    public Employee getEmployee(UUID id) {
        return employees.get(id);
    }

    public List<Printer> getPrinters() {
        return new ArrayList<>(printers.values());
    }

    public BigDecimal getTotalBoughtPaperPrice() {
        return totalBoughtPaperPrice;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public HashMap<UUID, BigDecimal> calculateSalaries() {
        var salaries = new HashMap<UUID, BigDecimal>();
        for (var employee : employees.values()) {
            var salary = baseSalary;
            if (employee.getType() == EmployeeType.Manager && bonusThreshold.compareTo(totalIncome) < 0) {
                salary = salary.multiply(new BigDecimal("1").add(bonusPercentage));
            }
            salaries.put(employee.getId(), salary);
        }
        return salaries;
    }
}
