package com.spark.demo;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class BucketDemo {

	public static void main(String[] args) {
		
		SparkSession spark = SparkSession.builder().appName("Bucket-demo").config("spark.some.config.option", "some-value")
				.master("local").getOrCreate();

		Dataset<Row> stuDF = spark.read().json("stu.json");
		stuDF.createOrReplaceTempView("stu");
		
		spark.sql("SELECT * from stu ORDER BY name").show();
		spark.sql("SELECT * from stu SORT BY name").show();
		spark.sql("SELECT * from stu DISTRIBUTE BY name").show();
		spark.sql("SELECT * from stu CLUSTER BY name").show();

	}

}
