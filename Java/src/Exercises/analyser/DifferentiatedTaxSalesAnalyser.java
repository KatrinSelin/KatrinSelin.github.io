package Exercises.analyser;

import java.util.List;

public class DifferentiatedTaxSalesAnalyser extends MainTaxSalesAnalyser {

    @Override
    public double tax(SalesRecord product) {

        if (product.hasReducedRate()) {
            return 1.1;
        } else {
            return 1.2;
        }
    }

    public DifferentiatedTaxSalesAnalyser(List<SalesRecord> records) {

        super(records);
    }
}
