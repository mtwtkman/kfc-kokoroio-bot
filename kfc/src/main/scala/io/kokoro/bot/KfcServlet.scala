package io.kokoro.bot

import org.scalatra._


class KfcServlet extends KfcStack {
  val access_token = sys.env.get("KFC_KOKOROIO_BOT_ACCESS_TOKEN") match {
    case Some(x) => x
    case None => "not found KFC_KOKOROIO_BOT_ACCESS_TOKEN"
  }

  val callback_secret = sys.env.get("KFC_KOKOROIO_BOT_CALLBACK_SECRET") match {
    case Some(x) => x
    case None => "not found KFC_KOKOROIO_BOT_CALLBACK_SECRET"
  }

  get("/") {
    println(access_token)
    <html>
      <body>
        KFC bot for <a href="https://kokoro.io">kokoro.io</a>
      </body>
    </html>
  }

  post("/") {
    if (params.getOrElse("Authorization", "") != callback_secret) {
      return
    }
  }
}
