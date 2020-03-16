package Exercises.analyser;

import java.util.List;

public class TaxFreeSalesAnalyser extends MainTaxSalesAnalyser {

    @Override
    public double tax(SalesRecord product) {
        return 1.0;
    }

    public TaxFreeSalesAnalyser(List<SalesRecord> records) {

        super(records);
    }
}
