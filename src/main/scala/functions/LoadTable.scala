package functions

import org.apache.spark.sql._
import system.Settings
import org.apache.spark.sql.functions._
import system.Settings.StopList

object LoadTable {
  def load(spark: SparkSession): Unit = {
    import spark.implicits._

    val DFInitial = spark.read
      .jdbc(Settings.urlPG, Settings.rawTablePG, Settings.propertiesPG)
      .filter(to_date('response_time) > lit(date_sub(current_date(), 1)))
      .withColumn("JSON", split(col("response_body"), "}, "))
      .withColumn("exploded", explode('JSON))
      .drop("response_body", "JSON")
      .withColumn("replaced_once", regexp_replace('exploded, "^\\[\\{", "{"))
      .withColumn("replaced_twice", regexp_replace('replaced_once, "\\}\\]$", ""))
      .withColumn("replaced_three", concat('replaced_twice, lit("}")))
      .withColumn("replaced_four", regexp_replace('replaced_three, """"""", """'"""))
      .withColumn("replaced", regexp_replace('replaced_three, """\n""", ""))
      .withColumn("parsed", from_json('replaced, Settings.SingleNews))
      .drop("exploded", "replaced_once", "replaced_twice", "replaced_three", "replaced_four")
      .select("parsed.*")
      .na.drop("all")


    //DFInitial.printSchema()
    DFInitial.show(10, false)

    val DFTopic = DFInitial.select("title")
      .withColumn("words", explode(split(col("title"), " ")))
      .filter(!$"words".isin(StopList: _*))
      .groupBy("words")
      .count()
      .orderBy('count.desc)

    DFTopic.createOrReplaceTempView("TopWords")

    val DFTop =  spark.sql("select * from TopWords limit 10")


    DFTopic.printSchema()
    DFTopic.show(50, truncate = false)

    DFTop.show(50, false)
  }
}
