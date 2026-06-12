public class Range {
    public double start;
    public double end;
    public String name;

    public Range(double start, double end, String name) {
        if (start >= end) {
            throw new IllegalArgumentException();
        }
        this.start = start;
        this.end = end;
        this.name = name;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (this.getClass() != other.getClass()) {
            return false;
        }
        Range otherRange = (Range) other;
        return this.start == otherRange.start && this.end == otherRange.end && this.name.equals(otherRange.name);
    }

    public String toString() {
        return name;
    }
}
