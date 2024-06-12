package printinghouse.commands;

import java.util.UUID;

import printinghouse.data.Employee;
import printinghouse.data.EmployeeType;
import printinghouse.data.PrintingHouse;

public class EmployeeCommands {
    public static void hireEmployee(PrintingHouse printingHouse) {
        var employeeId = UUID.randomUUID();
        var name = System.console().readLine("Enter the name of the employee: ");
        var type = EmployeeType.valueOf(System.console().readLine("Enter the type (PrinterOperator or Manager) of the employee: "));
        var employee = new Employee(employeeId, name, type);
        printingHouse.addEmployee(employee);
        System.out.println("Employee hired successfully.");
    }

    public static void fireEmployee(PrintingHouse printingHouse) {
        var employeeId = UUID.fromString(System.console().readLine("Enter the ID of the employee: "));
        printingHouse.fireEmployee(employeeId);
        System.out.println("Employee fired successfully.");
    }
}
