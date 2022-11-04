package model;

public class Student {

    private final String id;
    private final String name;
    private final SchoolYear startYear;
    private final SchoolYear endYear;
    private final String countryCode;

    public Student(
        String id, String name, 
        SchoolYear startYear, SchoolYear endYear,
        String countryCode) {
        
        this.id = id;
        this.name = name;
        this.startYear = startYear;
        this.endYear = endYear;
        this.countryCode = countryCode;

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public SchoolYear getStartYear() {
        return startYear;
    }

    public SchoolYear getEndYear() {
        return endYear;
    }

    public String getCountryCode() {
        return countryCode;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Student && ((Student) other).id == id;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, id);
    }

}