package com.spark.demo;

import java.util.concurrent.TimeUnit;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timeout;
import org.jboss.netty.util.TimerTask;

public class JobTimerTask implements TimerTask {

	private SparkSession spark;
	private HashedWheelTimer timer;

	public JobTimerTask(SparkSession spark, HashedWheelTimer timer) {
		this.spark = spark;
		this.timer = timer;
	}

	@Override
	public void run(Timeout timeout) throws Exception {

		// Dataset<Row> appDF = spark.read().json("app.json");
		// Dataset<Row> pcDF = spark.read().json("pc.json");
		// appDF.createOrReplaceTempView("app");
		// pcDF.createOrReplaceTempView("pc");
		// spark.sql("select distinct name FROM (SELECT name FROM app union all
		// SELECT name FROM pc)").show();

		Dataset<Row> stuDF = spark.read().json("stu.json");
		stuDF.createOrReplaceTempView("stu");
		spark.sql("SELECT name,myAverage(scores) as avg_salary FROM stu group by name").show();

		this.timer.newTimeout(this, 10, TimeUnit.SECONDS);
	}

}
