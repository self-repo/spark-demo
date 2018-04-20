package com.spark.demo;

import java.util.Arrays;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQueryException;

public class JavaStructuredNetworkWordCount {

	public static void main(String[] args) throws StreamingQueryException {

		String host = "10.199.201.102";
		int port = 9999;

		SparkSession spark = SparkSession.builder().appName("JavaStructuredNetworkWordCount")
				.config("spark.sql.warehouse.dir", "file:///E:/temp/spark-warehouse").master("local").getOrCreate();

		// Create DataFrame representing the stream of input lines from
		// connection to host:port
		Dataset<Row> lines = spark.readStream().format("socket").option("host", host).option("port", port).load();

		// Split the lines into words
		Dataset<String> words = lines.as(Encoders.STRING()).flatMap(
				(FlatMapFunction<String, String>) x -> Arrays.asList(x.split(" ")).iterator(), Encoders.STRING());

		// Generate running word count
		Dataset<Row> wordCounts = words.groupBy("value").count();

		// Start running the query that prints the running counts to the console
		// StreamingQuery query =
		// wordCounts.writeStream().outputMode("complete").format("console").start();

		// query.awaitTermination();

		wordCounts.printSchema();
		wordCounts.join(wordCounts).show();
	}

}
