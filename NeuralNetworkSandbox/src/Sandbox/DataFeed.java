package Sandbox;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DataFeed {

    public static List<List<String>> data(String fileName) {
        String delimiter = ",";
        String line;
        List<List<String>> lines = new ArrayList<>();
        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(Objects.requireNonNull(DataFeed.class.getResourceAsStream("/Resources/Data/" + fileName))))) {
            while((line = reader.readLine()) != null){
                List<String> values = Arrays.asList(line.split(delimiter));
                lines.add(values);
            }

        } catch (Exception e){
            System.out.println(e);
        }
        return lines;
    }

}
