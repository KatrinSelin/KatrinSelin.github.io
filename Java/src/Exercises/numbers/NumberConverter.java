package Exercises.numbers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class NumberConverter {

    private String hundredWithDelimiterBlock = "";
    private String hundredNoDelimiterBlock = "";
    private String tensWithDelimiterBlock = "";
    private String tensNoDelimiterBlock = "";
    private String oneOneBlock = "";
    private String oneTeenBlock = "";


    private String lang;
    private Properties properties = new Properties();

    public NumberConverter(String lang) {

        this.lang = lang;

        FileInputStream fileInputStream = null;
        try {
            String pathToProperties = "src/exceptions/numbers/numbers_%s.properties";
            fileInputStream = new FileInputStream(String.format(pathToProperties, this.lang));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

            properties.load(inputStreamReader);
        } catch (IllegalArgumentException e) {
            throw new BrokenLanguageFileException(lang, e);
        } catch (Exception e) {
            throw new MissingLanguageFileException(lang, e);
        } finally {
            close(fileInputStream);
        }
    }

    public String numberInWords(Integer number) {

        String pathToTxt = "src/exceptions/numbers/expected-%s.txt";
        File expected  = new File(String.format(pathToTxt, lang));
        if (expected.length() == 0) {
            throw new MissingTranslationException(lang);
        }

        if (properties.containsKey(String.valueOf(number))) {
            return properties.getProperty(String.valueOf(number));
        }

        String stringNumber = "";

        int ones = number % 10;
        int tens = number % 100 / 10;
        int hundreds = number / 100;

        String tensInString = "";
        if (properties.containsKey(String.valueOf(tens) + 0)) {
            tensInString = properties.getProperty(String.valueOf(tens) + 0);
        } else {
            tensInString = properties.getProperty(String.valueOf(tens)) +
                    properties.getProperty("tens-suffix");
        }


        hundredNoDelimiterBlock =
                properties.getProperty(String.valueOf(hundreds)) +
                        properties.getProperty("hundreds-before-delimiter") +
                        properties.getProperty("hundred");

        hundredWithDelimiterBlock =
                hundredNoDelimiterBlock +
                        properties.getProperty("hundreds-after-delimiter");

        tensNoDelimiterBlock = tensInString;

        tensWithDelimiterBlock = tensInString + properties.getProperty("tens-after-delimiter");

        oneOneBlock = properties.getProperty(String.valueOf(ones));


        if (!properties.containsKey(String.valueOf(1) + ones)) {
            oneTeenBlock = properties.getProperty(String.valueOf(ones)) + properties.getProperty("teen");
        } else {
            oneTeenBlock = properties.getProperty(String.valueOf(1) + ones);
        }

        if (hundreds == 0) {
            stringNumber = getStringNumberNoHundred(tens, ones);
        } else {
            stringNumber = getStringNumberWithHundred(tens, ones);
        }

        return stringNumber;
    }

    private String getStringNumberNoHundred(int tens, int ones) {
        String stringNumber = "";

        if (tens == 0) {
            stringNumber = oneOneBlock;
        }

        if (tens == 1) {
            stringNumber = oneTeenBlock;
        }

        if (tens > 1 && ones != 0) {
            stringNumber = tensWithDelimiterBlock + oneOneBlock;
        }

        if (tens > 1 && ones == 0) {
            stringNumber = tensNoDelimiterBlock;
        }

        return stringNumber;
    }

    private String getStringNumberWithHundred(int tens, int ones) {
        String stringNumber = "";

        if (tens == 0 && ones == 0) {
            stringNumber = hundredNoDelimiterBlock;
        }

        if (tens == 0 && ones != 0) {
            stringNumber = hundredWithDelimiterBlock + oneOneBlock;
        }

        if (tens == 1) {
            stringNumber = hundredWithDelimiterBlock + oneTeenBlock;
        }

        if (tens > 1 && ones != 0) {
            stringNumber = hundredWithDelimiterBlock + tensWithDelimiterBlock + oneOneBlock;
        }

        if (tens > 1 && ones == 0) {
            stringNumber = hundredWithDelimiterBlock + tensNoDelimiterBlock;
        }

        return stringNumber;
    }

    private static void close(FileInputStream is) {
        if (is == null) {
            return;
        }

        try {
            is.close();
        } catch (IOException ignore) {}
    }
}
