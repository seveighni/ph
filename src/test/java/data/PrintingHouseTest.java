package data;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import printinghouse.data.Employee;
import printinghouse.data.EmployeeType;
import printinghouse.data.PageSize;
import printinghouse.data.PaperStore;
import printinghouse.data.PaperType;
import printinghouse.data.PricingModel;
import printinghouse.data.PrintColor;
import printinghouse.data.Printer;
import printinghouse.data.PrintingHouse;
import printinghouse.data.Publication;
import printinghouse.data.PublicationType;
import printinghouse.exceptions.PrintingFailure;

public class PrintingHouseTest {
    private PrintingHouse printingHouse;

    @BeforeEach
    private void init() {
        HashMap<PaperType, BigDecimal> basePricesHouse = new HashMap<>();
        basePricesHouse.put(PaperType.Newspaper, new BigDecimal("0.5"));
        basePricesHouse.put(PaperType.Standard, new BigDecimal("1.0"));
        basePricesHouse.put(PaperType.Glossy, new BigDecimal("1.5"));
        PricingModel pricingModelHouse = new PricingModel(basePricesHouse, new BigDecimal("0.1"), 100,
                new BigDecimal("0.1"));

        HashMap<PaperType, BigDecimal> basePricesStore = new HashMap<>();
        basePricesStore.put(PaperType.Newspaper, new BigDecimal("0.2"));
        basePricesStore.put(PaperType.Standard, new BigDecimal("0.3"));
        basePricesStore.put(PaperType.Glossy, new BigDecimal("0.5"));
        PricingModel pricingModelStore = new PricingModel(basePricesStore, new BigDecimal("0.1"), 2000,
                new BigDecimal("0.1"));
        var store = new PaperStore("Paper Store", pricingModelStore);

        printingHouse = new PrintingHouse("Printing House", pricingModelHouse, store, new BigDecimal("300"),
                new BigDecimal("2000"), new BigDecimal("0.2"));
    }

    @Test
    void testAddEmployee() {
        assertEquals(0, printingHouse.getEmployees().size());
        var employeeId = UUID.randomUUID();
        printingHouse.addEmployee(new Employee(employeeId, "John", EmployeeType.PrinterOperator));
        var employees = printingHouse.getEmployees();
        assertEquals(1, employees.size());
        assertEquals("John", employees.get(0).getName());
        assertEquals(EmployeeType.PrinterOperator, employees.get(0).getType());
        assertEquals(employeeId, employees.get(0).getId());
    }

    @Test
    void testFireEmployee() {
        var employeeId = UUID.randomUUID();
        printingHouse.addEmployee(new Employee(employeeId, "John", EmployeeType.PrinterOperator));
        assertEquals(1, printingHouse.getEmployees().size());
        printingHouse.fireEmployee(employeeId);
        assertEquals(0, printingHouse.getEmployees().size());
    }

    @Test
    void testGetEmployee() {
        var employeeId = UUID.randomUUID();
        printingHouse.addEmployee(new Employee(employeeId, "John", EmployeeType.PrinterOperator));
        var employee = printingHouse.getEmployee(employeeId);
        assertEquals("John", employee.getName());
        assertEquals(EmployeeType.PrinterOperator, employee.getType());
        assertEquals(employeeId, employee.getId());
    }

    @Test
    void testAddPrinter() {
        var printerId = UUID.randomUUID();
        var printer = new Printer(printerId, 300, 30, PrintColor.BlackAndWhite, PageSize.A5, PaperType.Standard);
        assertEquals(0, printingHouse.getPrinters().size());
        printingHouse.addPrinter(printer);
        var printers = printingHouse.getPrinters();
        assertEquals(1, printers.size());
        assertEquals(printerId, printers.get(0).getId());
    }

    @Test
    void testPrintPublication() {
        var printerId = UUID.randomUUID();
        var printer = new Printer(printerId, 300, 30, PrintColor.BlackAndWhite, PageSize.A5, PaperType.Standard);
        printingHouse.addPrinter(printer);
        var publication = new Publication(PublicationType.Book, "A book", PageSize.A5, 50);
        assertDoesNotThrow(() -> {
            var totalPrice = printingHouse.printPublication(publication, 20, PaperType.Standard,
                    PrintColor.BlackAndWhite);
            assertEquals(new BigDecimal("1000.00"), totalPrice);
        });
        assertEquals(1000, printer.getNumberOfPrintedPages());
        assertEquals(new BigDecimal("1000.00"), printingHouse.getTotalIncome());
        assertEquals(new BigDecimal("300.00"), printingHouse.getTotalBoughtPaperPrice());
    }

    @Test
    void testCalculateSalaries() {
        var employeeId1 = UUID.randomUUID();
        var employeeId2 = UUID.randomUUID();
        printingHouse.addEmployee(new Employee(employeeId1, "John", EmployeeType.PrinterOperator));
        printingHouse.addEmployee(new Employee(employeeId2, "Jane", EmployeeType.Manager));
        var salaries = printingHouse.calculateSalaries();
        assertEquals(2, salaries.size());
        assertEquals(new BigDecimal("300"), salaries.get(employeeId1));
        assertEquals(new BigDecimal("300"), salaries.get(employeeId2));
    }

    @Test
    void testCalculateSalariesWithManagerIncrease() {
        var employeeId1 = UUID.randomUUID();
        var employeeId2 = UUID.randomUUID();
        printingHouse.addEmployee(new Employee(employeeId1, "John", EmployeeType.PrinterOperator));
        printingHouse.addEmployee(new Employee(employeeId2, "Jane", EmployeeType.Manager));
        var printerId = UUID.randomUUID();
        var printer = new Printer(printerId, 300, 30, PrintColor.BlackAndWhite, PageSize.A5, PaperType.Standard);
        printingHouse.addPrinter(printer);
        var publication = new Publication(PublicationType.Book, "A book", PageSize.A5, 50);
        assertDoesNotThrow(() -> {
            var totalPrice = printingHouse.printPublication(publication, 50, PaperType.Standard,
                    PrintColor.BlackAndWhite);
            assertEquals(new BigDecimal("2500.00"), totalPrice);
        });
        var salaries = printingHouse.calculateSalaries();
        assertEquals(2, salaries.size());
        assertEquals(new BigDecimal("300"), salaries.get(employeeId1));
        assertEquals(new BigDecimal("360.0"), salaries.get(employeeId2));
    }

    @Test
    void testPrintingFailureBecauseOfWrongPaperSize() {
        var printerId = UUID.randomUUID();
        var printer = new Printer(printerId, 300, 30, PrintColor.BlackAndWhite, PageSize.A5, PaperType.Standard);
        printingHouse.addPrinter(printer);
        var publication = new Publication(PublicationType.Book, "A book", PageSize.A4, 50);
        assertThrows(PrintingFailure.class, ()-> printingHouse.printPublication(publication, 50, PaperType.Standard, PrintColor.BlackAndWhite));
    }
}
