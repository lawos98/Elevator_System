package pl.app

import scala.util.Random
import scala.util.Random.between

case class Person(id: Int, weight: Int, destinationFloor: Int) {
  private var timeForElevator = 0
  private var timeInElevator = 0

  def getTimeForElevator: Int = timeForElevator

  def getTimeInElevator: Int = timeInElevator

  def addTimeForElevator(): Unit = {
    timeForElevator += 1
  }

  def addTimeInElevator(): Unit = {
    timeInElevator += 1
  }

  private def resetTimer(): Unit = {
    timeForElevator = 0
    timeInElevator = 0
  }

  def changeDestinationFloor(): Person = {
    resetTimer()
    copy(destinationFloor = Random.between(0, ElevatorConfig.numberOfFloors))
  }

  override def toString: String = s"Person(id=$id, weight=$weight, destinationFloor=$destinationFloor)"
}

object Person {
  private var idCounter = 0

  private def generateId(): Int = {
    idCounter += 1
    idCounter
  }

  def apply(weight: Int, destinationFloor: Int): Person = {
    val id = generateId()
    Person(id, weight, destinationFloor)
  }

  def generateRandomPerson(): Person = {
    val weight = Random.between(40, 120)
    val destinationFloor = Random.between(1, ElevatorConfig.numberOfFloors)
    apply(weight, destinationFloor)
  }
}