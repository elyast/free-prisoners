package com.michalplachta.freeprisoners.freestyle.algebras

import com.michalplachta.freeprisoners.PrisonersDilemma.{
  Decision,
  Prisoner,
  Verdict
}
import freestyle._

@free trait Player {
  def meetPrisoner(introduction: String): FS[Prisoner]

  def getPlayerDecision(prisoner: Prisoner,
                        otherPrisoner: Prisoner): FS[Decision]

  def giveVerdict(prisoner: Prisoner, verdict: Verdict): FS[Unit]
}
