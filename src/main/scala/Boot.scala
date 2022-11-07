import functions.LoadTable
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object Boot {
  def main(args: Array[String]): Unit = {
    println("start")
    Logger.getLogger("org").setLevel(Level.ERROR)
    Logger.getLogger("com").setLevel(Level.ERROR)

    val spark = SparkSession.builder()
      .master("local[8]")
      .appName("parseNewsresponse")
      .getOrCreate()

    LoadTable.load(spark)

  }

}
