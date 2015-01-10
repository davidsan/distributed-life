import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Conway {

	public static class ConwayMapper extends
			Mapper<Object, Text, Text, IntWritable> {

		private final static IntWritable zero = new IntWritable(0);
		private final static IntWritable one = new IntWritable(1);

		// private Case position = new Case();

		public void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {
			String[] parts = value.toString().split(",");
			Long x = Long.parseLong(parts[0]);
			Long y = Long.parseLong(parts[1]);

			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					Long neighbourX = x + i;
					Long neighbourY = y + j;
					if (neighbourX < 0 || neighbourY < 0) {
						continue;
					}
					if (i == 0 && j == 0) {
						context.write(new Text(neighbourX + "," + neighbourY),
								zero);
					} else {
						context.write(new Text(neighbourX + "," + neighbourY),
								one);
					}
				}
			}
		}
	}

	public static class ConwayReducer extends
			Reducer<Text, IntWritable, Text, NullWritable> {

		public void reduce(Text key, Iterable<IntWritable> values,
				Context context) throws IOException, InterruptedException {
			String[] parts = key.toString().split(",");
			Long x = Long.parseLong(parts[0]);
			Long y = Long.parseLong(parts[1]);
			boolean wasAlive = false;
			int sum = 0;
			for (IntWritable val : values) {
				if (val.get() == 0) {
					wasAlive = true;
				}
				sum += val.get();
			}
			if (sum == 3 || (sum == 2 && wasAlive)) {
				context.write(new Text(x + "," + y), NullWritable.get());
			}
		}
	}

	public static void main(String[] args) throws Exception {

		if (args.length != 1) {
			System.out
					.println("usage : hadoop jar target/conway-0.0.1-SNAPSHOT.jar Conway <depth>");
			System.exit(0);
		}

		// Create configuration
		Configuration conf = new Configuration();

		// Create job
		Job job = Job.getInstance(conf);

		// HDFS
		FileSystem fs = FileSystem.get(conf);

		for (int depth = 0; depth < Integer.parseInt(args[0]); depth++) {

			conf = new Configuration();
			job = Job.getInstance(conf, "conway " + depth);
			job.setJarByClass(Conway.class);

			// Setup MapReduce
			job.setMapperClass(ConwayMapper.class);
			job.setReducerClass(ConwayReducer.class);

			// Specify key / value for the mapper
			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(IntWritable.class);
			// Specify key / value for the reducer
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(NullWritable.class);

			// Input
			FileInputFormat.addInputPath(job, new Path("depth_" + (depth)));

			// Output
			Path output = new Path("depth_" + (depth + 1));
			if (fs.exists(output)) {
				fs.delete(output, true);
			}
			FileOutputFormat.setOutputPath(job, output);

			// Execute job and wait for its completion
			job.waitForCompletion(true);

		}

	}
}