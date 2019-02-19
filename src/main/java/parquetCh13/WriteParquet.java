package parquetCh13;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.avro.Schema;
import org.apache.avro.Schema.Field;
import org.apache.avro.Schema.Type;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericData.Record;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;

public class WriteParquet {
	public static void main(String[] args) throws IllegalArgumentException, IOException {

		List<Field> fields = new ArrayList<Field>();
		Object defaultValue = null;
		fields.add(new Field("x", Schema.create(Type.INT), "x", defaultValue));
		fields.add(new Field("y", Schema.create(Type.INT), "y", defaultValue));

		Schema schema = Schema.createRecord("name", "doc", "namespace", false, fields);

		try (ParquetWriter<GenericData.Record> writer = AvroParquetWriter.<GenericData.Record>builder(
				new Path("/parquet/my-file.parquet")).withSchema(schema).withCompressionCodec(CompressionCodecName.SNAPPY)
				.build()) {
                              
			// 模拟10000行数据
			for (int r = 0; r < 10000; ++r) {
				Record record = new Record(schema);
				record.put(0, r);
				record.put(1, r * 3);
				writer.write(record);
			}
		}
	}
}
