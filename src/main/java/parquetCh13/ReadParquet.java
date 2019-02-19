package parquetCh13;

import java.io.IOException;

import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.hadoop.ParquetReader;

public class ReadParquet {
	public static void main(String[] args) throws IllegalArgumentException, IOException {

		ParquetReader<GenericRecord> reader = AvroParquetReader.<GenericRecord>builder(new Path("/parquet/my-file.parquet"))
				.build();
		GenericRecord record;

		while ((record = reader.read()) != null) {
			System.out.println(record);
		}
	}
}