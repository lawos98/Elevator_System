package pl.app

sealed trait State
case object Up extends State
case object Down extends State
case object Idle extends State