package pl.app

import scala.collection.immutable.HashSet

class Elevator(val id: Int,var currentFloor: Int,var direction:State, val maxCapacity: Int, val maxWeight: Int) {
  var passengersList: List[Person] = Nil
  var requestList: List[Int] = Nil

  def addRequest(floor: Int): Unit = {
    requestList = requestList :+ floor
  }

  def takeOutPeople(): Unit = {
    val passengersToTakeOut = passengersList.filter(_.destinationFloor == currentFloor)
    passengersToTakeOut.foreach(person => {
      ColorText.printBlue(s"Person ${person.id} left elevator $id on floor $currentFloor")
      Statistics.addWaitingTimeInElevator(person.getTimeToExitElevator())
    })
    passengersList = passengersList.filter(_.destinationFloor != currentFloor)
    requestList = requestList.filter(_ != currentFloor)
  }

  def takeInPeople():Unit = {
    val newPassengers = Building.getFloor(currentFloor).selectPassengers(this)
    newPassengers.foreach(person => {
      ColorText.printBlue(s"Person ${person.id} entered elevator $id on floor $currentFloor")
      Statistics.addWaitingTimeForElevator(person.getTimeToEnterElevator())
    })
    passengersList ++= newPassengers
    if(Building.getFloor(currentFloor).getPeopleNotTaken(direction).nonEmpty) Building.requestElevator(currentFloor,direction)
  }

  def step(): Unit = {
    ColorText.printMagenta(s"Elevator $id is on floor $currentFloor and has direction $direction")
    if(passengersList.exists(_.destinationFloor == currentFloor) || checkConditionToTakeAdditionalPeople()){
      takeOutPeople()
      takeInPeople()
    }

    val floorsAboveExist = Building.floorList.exists { case (floor) =>
      floor.number > currentFloor && (requestList.exists(_>currentFloor) || passengersList.exists(_.destinationFloor > currentFloor))
    }

    val floorsBelowExist = Building.floorList.exists { case (floor) =>
      floor.number < currentFloor && (requestList.exists(_<currentFloor) || passengersList.exists(_.destinationFloor < currentFloor))
    }

    passengersList.foreach(_.addTimeToExitElevator())

    direction = (floorsAboveExist, floorsBelowExist, direction) match {
      case (false, false, _) => Idle
      case (false, true, Up) => Down
      case (true, false, Down) => Up
      case (true, false, Idle) => Up
      case (false, true, Idle) => Down
      case _ => direction
    }
    if (direction == Up) {
      currentFloor += 1
      Statistics.addMoveUp(currentWeight)
    }
    else if (direction == Down) {
      currentFloor -= 1
      Statistics.addMoveDown(currentWeight)
    }
  }

  def currentWeight: Int = {
    passengersList.foldLeft(0)(_ + _.weight)
  }

  def currentCapacity: Int = {
    passengersList.length
  }

  def checkConditionToTakeAdditionalPeople():Boolean = {
    currentCapacity/maxCapacity.toDouble < ElevatorConfig.maxPercentageOfPeople && currentWeight/maxWeight.toDouble < ElevatorConfig.maxPercentageOfWeight
  }
}