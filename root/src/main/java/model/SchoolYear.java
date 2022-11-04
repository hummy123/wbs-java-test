package model;

public class SchoolYear implements Comparable<SchoolYear> {

    final int start;

    public SchoolYear(int start) {
        this.start = start;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return start + 1;
    }

    @Override
    public String toString() {
        return String.format("%d-%d", getStart(), getEnd());
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof SchoolYear && ((SchoolYear) other).start == start;
    }

    @Override
    public int hashCode() {
        return start;
    }

    @Override
    public int compareTo(SchoolYear other) {
        return Integer.valueOf(start).compareTo(other.start);
    }

}