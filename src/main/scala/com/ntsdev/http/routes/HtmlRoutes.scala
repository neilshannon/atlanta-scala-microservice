package com.ntsdev.http.routes

import java.nio.file.{Files, Path, Paths}

import akka.http.scaladsl.model.{HttpRequest, StatusCodes, Uri}
import akka.http.scaladsl.server.Directives._
import com.softwaremill.session.SessionManager
import com.softwaremill.session.SessionDirectives._
import com.softwaremill.session.SessionOptions._

import scala.concurrent.ExecutionContext

class HtmlRoutes(implicit executionContext: ExecutionContext, sessionManager: SessionManager[Map[String, String]]) {

  val staticContentDir = calculateStaticPath()
  val staticPath = "site"

  val route = pathPrefix(staticPath) {
    entity(as[HttpRequest]) { requestData =>
      val fullPath = requestData.uri.path
      encodeResponse {
        if (Files.exists(staticContentDir.resolve(fullPath.toString().replaceFirst(s"/$staticPath/", "")))) {
          getFromBrowseableDirectory(staticContentDir.toString)
        } else {
          getFromResourceDirectory("site")
        }
      }
    }
  } ~
  pathSingleSlash {
    invalidateSession(oneOff, usingCookies) {
      redirect(Uri("/site/index.html"), StatusCodes.Found)
    }
  }

  private def calculateStaticPath(): Path = {
    val workingDirectory = System.getProperty("user.dir")
    Paths.get(workingDirectory + "/site")
  }

}
