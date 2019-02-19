package avroCh12;
import java.io.*;

import org.apache.avro.file.*;
import org.apache.avro.io.*;
import org.apache.avro.specific.*;
public class AvroDeserialize {
    public static void main(String args[]) {
        DatumReader<User> reader = new SpecificDatumReader<>(User.class);
        DataFileReader<User> fileReader = null;
        try {
            fileReader = new DataFileReader<User>(new File("./src/main/resources/avro/user.avro"), reader);
            User user = null;
            while (fileReader.hasNext()) {
                user = fileReader.next(user);
                System.out.println(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
