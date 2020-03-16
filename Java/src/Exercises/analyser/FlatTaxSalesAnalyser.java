package Exercises.analyser;

import java.util.List;

public class FlatTaxSalesAnalyser extends MainTaxSalesAnalyser{

    @Override
    public double tax(SalesRecord product) {
        return 1.2;
    }

    public FlatTaxSalesAnalyser(List<SalesRecord> records) {

        super(records);
    }
}
