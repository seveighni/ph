package printinghouse.commands;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.stream.Collectors;

import printinghouse.data.Employee;
import printinghouse.data.Printer;
import printinghouse.data.PrintingHouse;
import printinghouse.data.Publication;

public class NavigationCommands {

    public static void start(PrintingHouse printingHouse) {
        var wantHelp = true;
        while (true) {
            try {
                if (wantHelp) {
                    NavigationCommands.printHelp();
                    wantHelp = false;
                }
                var choice = System.console().readLine();
                switch (choice) {
                    case "1":
                        EmployeeCommands.hireEmployee(printingHouse);
                        break;
                    case "2":
                        EmployeeCommands.fireEmployee(printingHouse);
                        break;
                    case "3":
                        PrinterCommands.addPrinter(printingHouse);
                        break;
                    case "4":
                        PublicationCommands.printPublication(printingHouse);
                        break;
                    case "5":
                        NavigationCommands.printEmployees(printingHouse);
                        break;
                    case "6":
                        NavigationCommands.printPublicationsPerPrinter(printingHouse);
                        break;
                    case "7":
                        NavigationCommands.printExpenses(printingHouse);
                        break;
                    case "8":
                        NavigationCommands.printIncome(printingHouse);
                        break;
                    case "9":
                        NavigationCommands.save(printingHouse);
                        break;
                    case "10":
                        wantHelp = true;
                        break;
                    case "11":
                        return;
                    default:
                        System.out.println("Invalid choice");
                        wantHelp = true;
                        break;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void printHelp() {
        System.out.println("Enter a command:");
        System.out.println("1. Add an employee");
        System.out.println("2. Fire an employee");
        System.out.println("3. Add a printer");
        System.out.println("4. Print a publication");
        System.out.println("5. Show employees");
        System.out.println("6. Show publications per printer");
        System.out.println("7. Show expenses");
        System.out.println("8. Show income");
        System.out.println("9. Save the printing house");
        System.out.println("10. Show help");
        System.out.println("11. Exit");
    }

    public static void printEmployees(PrintingHouse printingHouse) {
        System.out.println("---Employees---");
        System.out.println("Employees:");
        for (Employee employee : printingHouse.getEmployees()) {
            System.out.println("- " + employee.toString());
        }
        System.out.println();
    }

    public static void printPublicationsPerPrinter(PrintingHouse printingHouse) {
        System.out.println("---Publications per printer---");
        System.out.println("Printers:");
        for (Printer printer : printingHouse.getPrinters()) {
            System.out.println("- " + printer.toString());
            System.out.println("\tPublications:");
            var groupedPublications = printer.getPrintedPublications().stream()
                    .collect(Collectors.groupingBy(Publication::getTitle));
            for (var entry : groupedPublications.entrySet()) {
                System.out.println(
                        "\t- " + entry.getValue().toArray()[0].toString() + ", Number of copies: "
                                + entry.getValue().size());
            }

        }
        System.out.println();
    }

    public static void printExpenses(PrintingHouse printingHouse) {
        System.out.println("---Expenses---");
        System.out.println("Total price of bought paper: " + printingHouse.getTotalBoughtPaperPrice());
        System.out.println("Salaries:");
        var salaries = printingHouse.calculateSalaries();
        for (var entry : salaries.entrySet()) {
            System.out.println(
                    "- Name: " + printingHouse.getEmployee(entry.getKey()).getName() + ", Salary: " + entry.getValue());
        }
        System.out.println();
    }

    public static void printIncome(PrintingHouse printingHouse) {
        System.out.println("---Income---");
        System.out.println("Total income: " + printingHouse.getTotalIncome());
        System.out.println();
    }

    public static void save(PrintingHouse printingHouse) throws FileNotFoundException, IOException {
        var filename = System.console().readLine("Enter the filename: ");
        try (FileOutputStream fileOut = new FileOutputStream(filename);
                ObjectOutputStream out = new ObjectOutputStream(fileOut);) {
            out.writeObject(printingHouse);
        }
        System.out.println("Printing house saved successfully.");
    }

    public static PrintingHouse load(String filename)
            throws FileNotFoundException, IOException, ClassNotFoundException {
        try (FileInputStream fileIn = new FileInputStream(filename);
                ObjectInputStream in = new ObjectInputStream(fileIn);) {
            return (PrintingHouse) in.readObject();
        }
    }
}
