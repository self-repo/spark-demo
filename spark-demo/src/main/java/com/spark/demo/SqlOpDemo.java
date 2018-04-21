package com.spark.demo;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import scala.Function1;

// https://blog.csdn.net/u010592112/article/details/73730796
// https://blog.csdn.net/feloxx/article/details/72819964
public class SqlOpDemo {

	public static void main(String[] args) {

		SparkSession spark = SparkSession.builder().appName("sql-op-demo")
				.config("spark.some.config.option", "some-value").master("local").getOrCreate();

		String stuSchemaStr = "Sno Sname Ssex Sbirthday SClass";
		List<StructField> stuFields = new ArrayList<>();
		for (String fieldName : stuSchemaStr.split(" ")) {
			StructField field = DataTypes.createStructField(fieldName, DataTypes.StringType, true);
			stuFields.add(field);
		}
		StructType stuSchema = DataTypes.createStructType(stuFields);

		// String courseSchemaStr = "Cno Cname Tno";
		// List<StructField> courseFields = new ArrayList<>();
		// for (String fieldName : courseSchemaStr.split(" ")) {
		// StructField field = DataTypes.createStructField(fieldName,
		// DataTypes.StringType, true);
		// courseFields.add(field);
		// }
		// StructType courseSchema = DataTypes.createStructType(courseFields);
		//
		// String scoreSchemaStr = "Sno Cno Degree";
		// List<StructField> scoreFields = new ArrayList<>();
		// for (String fieldName : scoreSchemaStr.split(" ")) {
		// StructField field = DataTypes.createStructField(fieldName,
		// DataTypes.StringType, true);
		// scoreFields.add(field);
		// }
		// StructType scoreSchema = DataTypes.createStructType(scoreFields);
		//
		// String teacherSchemaStr = "Tno Tname Tsex Tbirthday Prof Depart";
		// List<StructField> teacherFields = new ArrayList<>();
		// for (String fieldName : teacherSchemaStr.split(" ")) {
		// StructField field = DataTypes.createStructField(fieldName,
		// DataTypes.StringType, true);
		// teacherFields.add(field);
		// }
		// StructType teacherSchema = DataTypes.createStructType(teacherFields);

		JavaRDD<String> studentSource = spark.read().textFile("student.txt").javaRDD();
		JavaRDD<Row> rowRDD = studentSource.map(line -> {
			String[] parts = line.split(",");
			String Sno = parts[0];
			String Sname = parts[1];
			String Ssex = parts[2];
			String Sbirthday = parts[3];
			String SClass = parts[4];
			return RowFactory.create(Sno, Sname, Ssex, Sbirthday, SClass);
		});
		Dataset<Row> stuDF = spark.createDataFrame(rowRDD, stuSchema);
		stuDF.createOrReplaceTempView("Student");

		spark.sql("SELECT sname, ssex, sclass FROM Student").show();

		JavaRDD<String> courseSource = spark.read().textFile("course.txt").javaRDD();
		JavaRDD<Course> courseRowRDD = courseSource.map(line -> {
			String[] parts = line.split(",");
			return new Course(parts[0], parts[1], parts[2]);
		});
		Dataset<Row> courseDF= spark.createDataFrame(courseRowRDD, Course.class);
		courseDF.createOrReplaceTempView("Course");
		spark.sql("SELECT * FROM Course").show();
		
		spark.sql("SELECT sname FROM Student UNION SELECT Cname FROM Course").show();

	}

}
