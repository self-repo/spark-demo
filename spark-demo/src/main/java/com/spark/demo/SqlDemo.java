package com.spark.demo;

import java.util.concurrent.TimeUnit;

import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.SparkSession;
import org.jboss.netty.util.HashedWheelTimer;

//vm : -Xms256m -Xmx1024m
public class SqlDemo {

	static final HashedWheelTimer timer = new HashedWheelTimer();

	public static void main(String[] args) throws AnalysisException {

		SparkSession spark = SparkSession.builder().appName("sql-demo").config("spark.some.config.option", "some-value")
				.master("local").getOrCreate();

		spark.udf().register("myAverage", new MyAvg());
		
		timer.newTimeout(new JobTimerTask(spark, timer), 0, TimeUnit.SECONDS);

	}

}
