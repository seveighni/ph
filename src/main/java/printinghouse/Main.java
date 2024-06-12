package printinghouse;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.UUID;

import printinghouse.commands.NavigationCommands;
import printinghouse.data.Employee;
import printinghouse.data.EmployeeType;
import printinghouse.data.PageSize;
import printinghouse.data.PaperStore;
import printinghouse.data.PaperType;
import printinghouse.data.PricingModel;
import printinghouse.data.PrintColor;
import printinghouse.data.Printer;
import printinghouse.data.PrintingHouse;

public class Main {
    public static void main(String[] args) {
        System.out.println("Do you want to create a new printing house or load an existing one?");
        System.out.println("1. Create a new printing house");
        System.out.println("2. Load an existing printing house");
        var choice = System.console().readLine();
        if (choice.equals("1")) {
            HashMap<PaperType, BigDecimal> paperStoreBasePrices = new HashMap<>();
            paperStoreBasePrices.put(PaperType.Newspaper, new BigDecimal("0.2"));
            paperStoreBasePrices.put(PaperType.Standard, new BigDecimal("0.3"));
            paperStoreBasePrices.put(PaperType.Glossy, new BigDecimal("0.5"));
            var paperStorePricing = new PricingModel(paperStoreBasePrices, new BigDecimal("0.1"), 2000,
                    new BigDecimal("0.1"));
            var store = new PaperStore("Paper Store", paperStorePricing);

            HashMap<PaperType, BigDecimal> printingHouseBasePrices = new HashMap<>();
            printingHouseBasePrices.put(PaperType.Newspaper, new BigDecimal("0.5"));
            printingHouseBasePrices.put(PaperType.Standard, new BigDecimal("0.7"));
            printingHouseBasePrices.put(PaperType.Glossy, new BigDecimal("1.0"));
            var printingHousePricing = new PricingModel(printingHouseBasePrices, new BigDecimal("0.2"), 2,
                    new BigDecimal("0.05"));
            var name = System.console().readLine("Enter the name of the printing house: ");
            var printingHouse = new PrintingHouse(name, printingHousePricing, store, new BigDecimal("300"),
                    new BigDecimal("5000"), new BigDecimal("0.2"));
            var operator1 = new Employee(UUID.randomUUID(), "John Doe Jr", EmployeeType.PrinterOperator);
            var operator2 = new Employee(UUID.randomUUID(), "Jane Doe", EmployeeType.PrinterOperator);
            var manager = new Employee(UUID.randomUUID(), "John Doe", EmployeeType.Manager);
            printingHouse.addEmployee(operator1);
            printingHouse.addEmployee(operator2);
            printingHouse.addEmployee(manager);
            var printer1 = new Printer(UUID.randomUUID(), 300, 30, PrintColor.BlackAndWhite, PageSize.A5, PaperType.Standard);
            var printer2 = new Printer(UUID.randomUUID(), 200, 20, PrintColor.BlackAndWhite, PageSize.A4, PaperType.Newspaper);
            printingHouse.addPrinter(printer1);
            printingHouse.addPrinter(printer2);
            NavigationCommands.start(printingHouse);
        } else if (choice.equals("2")) {
            var filename = System.console().readLine("Enter the filename: ");
            try {
                var printingHouse = NavigationCommands.load(filename);
                NavigationCommands.start(printingHouse);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return;
            }
        } else {
            System.out.println("Invalid choice");
            return;
        }
    }
}