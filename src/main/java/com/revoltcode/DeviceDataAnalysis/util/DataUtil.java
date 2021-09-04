package com.revoltcode.DeviceDataAnalysis.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.revoltcode.DeviceDataAnalysis.model.GeoLocation;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String getDate(){
        return new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").format(Timestamp.from(ZonedDateTime.now().toInstant()));
    }

    public static String generateIMEI(){
        int pos;
        int[] str = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int sum = 0;
        int final_digit;
        int t;
        int len_offset;
        int len = 15;
        String imei = "";

        String[] rbi = new String[]{"01", "10", "30", "33", "35", "44", "45", "49", "50", "51", "52", "53", "54", "86", "91", "98", "99"};
        String[] arr = rbi[(int) Math.floor(Math.random() * rbi.length)].split("");
        str[0] = Integer.parseInt(arr[0]);
        str[1] = Integer.parseInt(arr[1]);
        pos = 2;

        while (pos < len - 1) {
            str[pos++] = (int) (Math.floor(Math.random() * 10) % 10);
        }

        len_offset = (len + 1) % 2;
        for (pos = 0; pos < len - 1; pos++) {
            if ((pos + len_offset) % 2 != 0) {
                t = str[pos] * 2;
                if (t > 9) {
                    t -= 9;
                }
                sum += t;
            } else {
                sum += str[pos];
            }
        }

        final_digit = (10 - (sum % 10)) % 10;
        str[len - 1] = final_digit;

        for (int d : str) {
            imei += String.valueOf(d);
        }

        return imei;
    }

    /**
     * @Description: Randomly generate latitude and longitude within a rectangle
     * @param MinLon: minimum longitude
     * @param MaxLon: maximum longitude
     * @param MinLat: minimum latitude
     * @param MaxLat: maximum latitude
     * @return @throws
     */
    public static GeoLocation getRandomGeoLocation(double MinLon, double MaxLon, double MinLat, double MaxLat) {
        BigDecimal db = new BigDecimal(Math.random() * (MaxLon - MinLon) + MinLon);
        String longitude = db.setScale(6, BigDecimal.ROUND_HALF_UP).toString();// 6 digits after the decimal

        db = new BigDecimal(Math.random() * (MaxLat - MinLat) + MinLat);
        String latitude = db.setScale(6, BigDecimal.ROUND_HALF_UP).toString();

        return GeoLocation.builder().longitude(Double.valueOf(longitude)).latitude(Double.valueOf(latitude)).build();
    }

    public static void saveAsCSV(String fileName, String jsonData){
        try {
            JsonNode jsonTree = objectMapper.readTree(jsonData);
            CsvSchema.Builder csvSchemaBuilder = CsvSchema.builder();
            JsonNode firsJsonNode = jsonTree.elements().next();
            firsJsonNode.fieldNames().forEachRemaining(fieldName -> {csvSchemaBuilder.addColumn(fieldName);});
            CsvSchema csvSchema = csvSchemaBuilder.build().withHeader();

            CsvMapper csvMapper = new CsvMapper();
            csvMapper.writerFor(JsonNode.class)
                    .with(csvSchema)
                    .writeValue(new File("/home/revolt/Documents/CST sample data/generated data/"+fileName+".csv"), jsonTree);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String generateNumberRef(int n) {
        int value = (int) Math.pow(10, n - 1);
        return String.valueOf(value + new Random().nextInt(9 * value));
    }

    public static String generatePhoneNumber(){
        String nc = null;
        switch (new Random().nextInt(2)){
            case 0:
                nc = "070";
                break;

            case 1:
                nc = "080";
                break;

            case 2:
                nc = "090";
                break;

            case 3:
                nc = "081";
                break;

            default:
        }
        return nc+generateNumberRef(8);
    }

    public static String getSha54(String text){
        return DigestUtils.sha256Hex(text);
    }
}
