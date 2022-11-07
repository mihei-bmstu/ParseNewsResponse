package system

import org.apache.spark.sql.types._

import scala.io.{BufferedSource, Source}
import java.util.Properties

object Settings {
  val userPG = "airflow"
  val passPG = "airflow"
  val urlPG = "jdbc:postgresql://localhost:5432/airflow"
  val rawTablePG = "news_raw"
  val TablePGMin = "news_processed"

  val propertiesPG = new Properties()
  propertiesPG.setProperty("user", userPG)
  propertiesPG.setProperty("password", passPG)
  propertiesPG.setProperty("driver", "org.postgresql.Driver")

  val SingleQuote: StructType = StructType(
    StructField("datetime", TimestampType, nullable = false) ::
      StructField("open", FloatType, nullable = false) ::
      StructField("high", FloatType, nullable = false) ::
      StructField("low", FloatType, nullable = false) ::
      StructField("close", FloatType, nullable = false) ::
      Nil
  )

  val SingleNews: StructType = new StructType()
    .add("id", StringType)
    .add("title", StringType)
    .add("coverImage", StringType)
    .add("excerpt", StringType)
    .add("time", StringType)


  val PGSchema: StructType = StructType(
    StructField("status", StringType, nullable = true) ::
    StructField("totalResults", IntegerType, nullable = true) ::
    StructField("results",
      StringType,
      nullable = false) ::
    StructField("nextPage", IntegerType, nullable = true) ::
    Nil
  )

  val NewsSchema: StructType = StructType(
    StructField("results", ArrayType(MapType(StringType, StringType))) :: Nil
  )

  val stopListFileName = "src/main/resources/words.txt"
  val sourceFile: BufferedSource = Source.fromFile(stopListFileName)
  val StopList: Array[String] = sourceFile.getLines.mkString(sep=" ").split(", ")
  sourceFile.close()

}
