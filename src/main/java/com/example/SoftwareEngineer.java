package com.example;

import java.util.Objects;

public class SoftwareEngineer {
    private Integer Id;
    private String name;
    private String TechStack;

    public SoftwareEngineer(Integer Id,
                            String name,
                            String TechStack ){
        this.Id = Id;
        this.name = name;
        this.TechStack = TechStack;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SoftwareEngineer that = (SoftwareEngineer) o;
        return Objects.equals(Id, that.Id) && Objects.equals(name, that.name) && Objects.equals(TechStack, that.TechStack);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, name, TechStack);
    }
}
