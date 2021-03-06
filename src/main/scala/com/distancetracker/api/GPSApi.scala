package com.distancetracker.api

import com.distancetracker.dao.GenericDao
import com.distancetracker.model.Coordinates
import com.distancetracker.persistence.GpsDataEntity
import com.distancetracker.swagger.GpsApiDescription
import org.scalatra.{NotFound, Ok, Created}


class GPSApi(implicit var coordDS: GenericDao[GpsDataEntity,String]) extends BaseController with GpsApiDescription {

  put("/:deviceId",operation(updateDeviceCoordinates)) {
    val deviceCoord = coordDS.update(new GpsDataEntity(getDeviceIdFromUrl, getCordFromBody))
    Created(deviceCoord)
  }

  get("/:deviceId", operation(readDeviceCoordinates)){
    coordDS.getById(getDeviceIdFromUrl) match {
      case None => NotFound("device not found")
      case Some(coord) => Ok(coord)
    }
  }

  def getCordFromBody = parsedBody.extractOrElse[Coordinates](halt(400))

  def getDeviceIdFromUrl = params.getOrElse("deviceId", halt(400))
}
