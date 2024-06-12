package printinghouse.data;

import java.io.Serializable;
import java.util.UUID;

public class Employee implements Serializable {
    private String name;
    private UUID id;
    private EmployeeType type;

    public Employee(UUID id, String name, EmployeeType type) {
        this.name = name;
        this.id = id;
        this.type = type;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public EmployeeType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Type: " + type;
    }
}
