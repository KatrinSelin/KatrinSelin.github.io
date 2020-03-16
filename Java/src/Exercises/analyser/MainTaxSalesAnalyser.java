package Exercises.analyser;

import java.util.HashMap;
import java.util.List;

public abstract class MainTaxSalesAnalyser {

    public abstract double tax(SalesRecord product);

    private List<SalesRecord> records;

    public MainTaxSalesAnalyser(List<SalesRecord> records) {

        this.records = records;
    }

    public Double getTotalSales() {

        double totalPrice = 0.0;

        for (SalesRecord product : records) {

            double total = (product.getProductPrice() / tax(product)) * product.getItemsSold();

            totalPrice += total;
        }
        return totalPrice;
    }

    public Double getTotalSalesByProductId(String id) {

        double totalPrice = 0.0;

        for (SalesRecord product : records) {

            if (id.equals(product.getProductId())) {

                double total = (product.getProductPrice() / tax(product)) * product.getItemsSold();

                totalPrice += total;
            }
        }

        return totalPrice;
    }

    public String getIdOfMostPopularItem() {

        double counter = 0;
        String popularId = "";
        HashMap<String, Double> dictionaryWithIdAndAmountSold = dictionary("Sold");

        for (SalesRecord product : records) {


            if (dictionaryWithIdAndAmountSold.get(product.getProductId()) > counter) {
                counter = dictionaryWithIdAndAmountSold.get(product.getProductId());
                popularId = product.getProductId();
            }
        }
        return popularId;
    }

    public HashMap<String, Double> dictionary(String type) {

        HashMap<String, Double> dictionaryWithIdAndAmountSold = new HashMap<>();

        for (SalesRecord product : records) {

            double value;

            if (type.equals("Sold")) {
                value = product.getItemsSold() * 1.0;
            } else {
                value = (product.getItemsSold() * product.getProductPrice()) / tax(product);
            }

            if (dictionaryWithIdAndAmountSold.containsKey(product.getProductId())) {
                dictionaryWithIdAndAmountSold.replace(
                        product.getProductId(),
                        dictionaryWithIdAndAmountSold.get(product.getProductId()),
                        dictionaryWithIdAndAmountSold.get(product.getProductId()) + value);
            } else {
                dictionaryWithIdAndAmountSold.put(product.getProductId(), value);
            }
        }
        return dictionaryWithIdAndAmountSold;
    }

    public String getIdOfItemWithLargestTotalSales() {

        double counter = 0;
        String idWithLargestSales = "";
        HashMap<String, Double> dictionaryWithIdAndAmountSold = dictionary("");

        for (SalesRecord product : records) {

            if (dictionaryWithIdAndAmountSold.get(product.getProductId()) > counter) {
                counter = dictionaryWithIdAndAmountSold.get(product.getProductId());
                idWithLargestSales = product.getProductId();
            }
        }
        return idWithLargestSales;
    }
}
