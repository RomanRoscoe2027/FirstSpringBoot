package com.example;


import java.util.Objects;
import java.util.List;


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
    public SoftwareEngineer(){}

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SoftwareEngineer that = (SoftwareEngineer) o;
        return Objects.equals(Id, that.Id) && Objects.equals(name, that.name) && Objects.equals(TechStack, that.TechStack);
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return Id;
    }

    public String getTechStack() {
        return TechStack;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, name, TechStack);
    }
}
