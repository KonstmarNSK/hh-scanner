package utils.dataobjects.vacancy;

public class Salary {
    public final Long to;
    public final Long from;
    public final String currency;
    public final boolean gross;

    public Salary() {
        to = -1L;
        from = -1L;
        currency = "NA";
        gross = false;
    }
}
