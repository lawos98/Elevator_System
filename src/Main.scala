package pl.app

object Main {
  def main(args: Array[String]): Unit = {
    val building = Building
    for(_ <- 1 to ElevatorConfig.numberOfPeople){
      building.getFloor(0).spawnPerson()
    }
    while(true){
      building.step()
      Thread.sleep(1000)
    }
  }
}