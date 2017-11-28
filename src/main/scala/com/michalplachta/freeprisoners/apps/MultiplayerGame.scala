package com.michalplachta.freeprisoners.apps

import cats.effect.IO
import cats.~>
import com.michalplachta.freeprisoners.free.algebras.GameOps.Game
import com.michalplachta.freeprisoners.free.algebras.MatchmakingOps.Matchmaking
import com.michalplachta.freeprisoners.free.algebras.PlayerOps.Player
import com.michalplachta.freeprisoners.free.algebras.TimingOps.Timing
import com.michalplachta.freeprisoners.free.interpreters._
import com.michalplachta.freeprisoners.free.programs.Multiplayer
import com.michalplachta.freeprisoners.free.programs.Multiplayer.{
  Multiplayer,
  Multiplayer0,
  Multiplayer1
}

object MultiplayerGame extends App {
  val matchmakingInterpreter = new MatchmakingServerInterpreter
  val gameInterpreter = new GameServerInterpreter
  val interpreter0: Multiplayer0 ~> IO =
    matchmakingInterpreter or gameInterpreter
  val interpreter1
    : Multiplayer1 ~> IO = PlayerConsoleInterpreter or interpreter0
  val interpreter: Multiplayer ~> IO = TimingInterpreter or interpreter1

  Multiplayer
    .program(
      new Player.Ops[Multiplayer],
      new Matchmaking.Ops[Multiplayer],
      new Game.Ops[Multiplayer],
      new Timing.Ops[Multiplayer]
    )
    .foldMap(interpreter)
    .attempt
    .unsafeRunSync()

  matchmakingInterpreter.terminate()
  gameInterpreter.terminate()
}
