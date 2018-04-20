package com.spark.demo;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.expressions.MutableAggregationBuffer;
import org.apache.spark.sql.expressions.UserDefinedAggregateFunction;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

public class MyAvg extends UserDefinedAggregateFunction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1950054033805321680L;

	private StructType inputSchema;
	private StructType bufferSchema;

	public MyAvg() {
		List<StructField> inputFields = new ArrayList<>();
		inputFields.add(DataTypes.createStructField("inputColumn", DataTypes.DoubleType, true));
		inputSchema = DataTypes.createStructType(inputFields);

		List<StructField> bufferFields = new ArrayList<>();
		bufferFields.add(DataTypes.createStructField("sum", DataTypes.DoubleType, true));
		bufferFields.add(DataTypes.createStructField("count", DataTypes.DoubleType, true));
		bufferSchema = DataTypes.createStructType(bufferFields);
	}

	@Override
	public StructType bufferSchema() {
		return bufferSchema;
	}

	@Override
	public DataType dataType() {
		return DataTypes.DoubleType;
	}

	@Override
	public boolean deterministic() {
		return true;
	}

	@Override
	public Object evaluate(Row buffer) {
		return buffer.getDouble(0) / buffer.getDouble(1);
	}

	@Override
	public void initialize(MutableAggregationBuffer buffer) {
		buffer.update(0, 0D);
		buffer.update(1, 0D);
	}

	@Override
	public StructType inputSchema() {
		return inputSchema;
	}

	@Override
	public void merge(MutableAggregationBuffer buffer1, Row buffer2) {
		double mergeSum = buffer1.getDouble(0) + buffer2.getDouble(0);
		double mergeCount = buffer1.getDouble(1) + buffer2.getDouble(1);
		buffer1.update(0, mergeSum);
		buffer1.update(1, mergeCount);
	}

	@Override
	public void update(MutableAggregationBuffer buffer, Row input) {
		if (!input.isNullAt(0)) {
			double updateSum = buffer.getDouble(0) + input.getDouble(0);
			double updateCount = buffer.getDouble(1) + 1;
			buffer.update(0, updateSum);
			buffer.update(1, updateCount);
		}
	}

}
