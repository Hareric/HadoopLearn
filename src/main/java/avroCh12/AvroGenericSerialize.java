package avroCh12;
import java.io.*;

import org.apache.avro.Schema;
import org.apache.avro.file.*;
import org.apache.avro.generic.*;
import org.apache.avro.io.*;

public class AvroGenericSerialize {
    public static void main(String args[]) throws IOException {
        Schema schema = new Schema.Parser().parse(new File("./src/main/resources/avro/User.avsc"));
        File file = new File("./src/main/avro/user_generic.avro");
        DatumReader<GenericRecord> datumReader = new GenericDatumReader<GenericRecord>(schema);
        DataFileReader<GenericRecord> dataFileReader = new DataFileReader<GenericRecord>(file, datumReader);
        GenericRecord user = null;
        while (dataFileReader.hasNext()) {
            user = dataFileReader.next(user);
            System.out.println(user);
        }
    }
}
