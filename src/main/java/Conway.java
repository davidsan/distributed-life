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

/**
 * Implementation of Conway's Game of Life using Hadoop MapReduce
 * ============================================================================
 * The input file contains all cells alive at the current generation. The
 * generated output contains all cells alive in the next generation. The main
 * function takes an argument (depth) which states how many generation must be
 * calculated. For each generation, one MapReduce operation will be executed.
 * 
 */
public class Conway {

	/**
	 * The mapper will output the coordinate of each neighbour of the
	 * alive-cells, with a value of one (1). The mapper will also output the
	 * coordinate of the alive-cells at the current generation, with a value of
	 * zero (0).
	 * 
	 */
	public static class ConwayMapper extends
			Mapper<Object, Text, Text, IntWritable> {

		private final static IntWritable zero = new IntWritable(0);
		private final static IntWritable one = new IntWritable(1);

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

	/**
	 * The reducer will sum the values associated with a given coordinate and
	 * output the coordinate if the rules to be alive are met. The rules of
	 * Conway's Game of Life states that a cell will be alive at the next
	 * generation if the cell is surrounded by exactly 3 cells alive or if the
	 * cell is alive at the current generation and surrounded by 2 cells alive.
	 * 
	 */
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

	/**
	 * The main function takes one argument : the number of generation to
	 * compute. The input file must be located at depth_0/
	 */
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