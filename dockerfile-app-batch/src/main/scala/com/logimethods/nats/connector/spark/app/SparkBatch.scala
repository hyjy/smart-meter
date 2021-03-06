/*******************************************************************************
 * Copyright (c) 2016 Logimethods
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * http://opensource.org/licenses/MIT
 *******************************************************************************/

package com.logimethods.nats.connector.spark.app

import java.util.Properties;
import java.io.File
import java.io.Serializable

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

import org.apache.log4j.{Level, LogManager, PropertyConfigurator}

import org.apache.spark.sql.SparkSession

//import com.datastax.spark.connector._
//import com.datastax.spark.connector.cql.CassandraConnector

// @see http://stackoverflow.com/questions/39423131/how-to-use-cassandra-context-in-spark-2-0
// @see https://databricks.com/blog/2016/08/15/how-to-use-sparksession-in-apache-spark-2-0.html
// @see https://dzone.com/articles/cassandra-with-spark-20-building-rest-api
object SparkBatch extends App {
	val cassandraUrl = System.getenv("CASSANDRA_URL")
			println("CASSANDRA_URL = " + cassandraUrl)

			val sparkMasterUrl = System.getenv("SPARK_MASTER_URL")
			println("SPARK_MASTER_URL = " + sparkMasterUrl)

			val spark = SparkSession
    			.builder()
    			.master(sparkMasterUrl)
    			.appName("SparkSessionZipsExample")
    			.config("spark.cassandra.connection.host", cassandraUrl)
    			//   .config("spark.sql.warehouse.dir", warehouseLocation)
    			//.enableHiveSupport()
    			.getOrCreate()

			spark
    			.read
    			.format("org.apache.spark.sql.cassandra")
    			.options(Map("keyspace" -> "smartmeter", "table" -> "raw_voltage_data"))
    			.load
    			.createOrReplaceTempView("raw_voltage_data")
    			
    	spark.sql("select * from raw_voltage_data").collect.foreach(println)
}