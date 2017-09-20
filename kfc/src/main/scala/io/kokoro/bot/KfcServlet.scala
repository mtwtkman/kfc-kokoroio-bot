package io.kokoro.bot

import org.json4s.{JObject, JField, JString}
import org.json4s.jackson.JsonMethods._
import org.scalatra._
import org.slf4j.{Logger, LoggerFactory}
import scalaj.http.Http
import com.github.nscala_time.time.Imports._

object Tori {
  val TORI_DAY = 28

  def calc_date(): Option[Int] = {
    val now = DateTime.now

    now.getDayOfMonth match {
      case day if day == TORI_DAY => None
      case day if day < TORI_DAY => Some(TORI_DAY - day)
      case day => {
        Some(now.dayOfMonth.getMaximumValue - day + TORI_DAY)
      }
    }
  }
}

class KfcServlet extends KfcStack {
  import Tori.calc_date

  var logger = LoggerFactory.getLogger(getClass)

  DateTimeZone.setDefault(DateTimeZone.forID("Asia/Tokyo"))

  val access_token = sys.env.get("KFC_KOKOROIO_BOT_ACCESS_TOKEN") match {
    case Some(x) => x
    case None => "not found KFC_KOKOROIO_BOT_ACCESS_TOKEN"
  }

  val callback_secret = sys.env.get("KFC_KOKOROIO_BOT_CALLBACK_SECRET") match {
    case Some(x) => x
    case None => "not found KFC_KOKOROIO_BOT_CALLBACK_SECRET"
  }

  get("/") {
    <html>
      <body>
        KFC bot for <a href="https://kokoro.io">kokoro.io</a>
      </body>
    </html>
  }

  val TORI_PTN  = "^![kK][fF][cC]$"
  val API_ENDPOINT = "https://kokoro.io/api/v1/bot/rooms/"

  post("/") {
    val body = parse(request.body)
    logger.info(s"received: $body")
    val authorization = request.getHeader("Authorization") match {
      case x if x == callback_secret => x
      case _ => {
        logger.debug("Invalid callback_secret")
        halt(401, "Invalid callback_secret")
      }
    }
    val parsed: List[(String, String)] = for {
      JObject(elem) <- body
      JField("raw_content", JString(message)) <- elem
      JField("room", JObject(room)) <- elem
      JField("id", JString(room_id)) <- room
    } yield (message, room_id)
    logger.info(s"parsed: $parsed")
    parsed match {
      case List((message, room_id)) if message.matches(TORI_PTN) && room_id != "" => {
        val tori_message = calc_date() match {
          case Some(day) => s"次のとりの日まであと`${day}日`です"
          case None => "今日はとりの日です!今すぐとりの日パックを買いましょう!"
        }
        val req_data = s"""{
          "message": "$tori_message",
          "display_name": "KFC"
        }"""
        val url = s"${API_ENDPOINT}${room_id}/messages"
        logger.info(s"post ${tori_message} to kokoro.io")
        val resp = Http(url)
          .postData(req_data)
          .header("Content-Type", "application/json")
          .header("X-Access-Token", access_token)
          .asString
        logger.info(s"response: $resp")
      }
      case _ => {
        logger.debug(s"Not matched with `${TORI_PTN}`")
        halt(401, "Not matched")
      }
    }
  }
}
