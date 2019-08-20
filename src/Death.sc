;;; Sierra Script 1.0 - (do not remove this comment)
;
;	DEATH.SC
;
;	This script handles death scenes. In the EgoDead procedure, you can switch based on of the
;	"reason" parameter to display custom scenes depending on the type of death.
;	The death handler is triggered by calling the EgoDead procedure with a number corresponding to a death reason.
;	You can also pick a specific respawn point that the game reverts to when selecting "Try Again".
;
;

(script# DEATH)
(include game.sh) (include "10.shm")
(use Main)
(use Print)
(use LoadMany)
(use DCIcon)
(use Sound)
(use Window)
(use Motion)
(use System)

(public
	EgoDead 0
)

(local
	local0
	local1
	[local2 16] = [-1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1]
	local18 =  60
	local19
)

(enum
	buttonTryAgain
	buttonRestore
	buttonRestart
	buttonQuit
)

(procedure (EgoDead reason revertState
		&tmp [temp0 20] [messageStr 150] [titleStr 20] oldSysBack oldCur deathView deathLoop deathBackColor
		temp195 printRet temp197 temp198 temp199 temp200 textY temp202 iconX iconY textX buttonY
			)
	;This procedure handles when Ego dies. It closely matches that of LSL6.
	;A revertState can be added to allow you to return to a certain point before death.
	(= local0 -1)
	(sounds eachElementDo: #pause TRUE)
	(switch reason
		;use the cases that you define in the message editor
		(C_GENERIC
			(= deathView vDeathSkull)
			(= deathBackColor 5)
			(= deathLoop 0)
		)
		(else
			;if no reason is given
			(= reason C_GENERIC)
		)
	)
	(if (< (>> (MemoryInfo TotalHunk) $0006) 230)
		(= deathView (= deathView (+ deathView 1000))) ;static view if not enough hunk
	)
	(Load RES_VIEW deathView)
	(if (not (IsObject revertState)) (= revertState 0))

	;set up the death window
	(= oldSysBack (systemWindow back?))
	(systemWindow back: deathBackColor)
	(= oldCur theCursor)
	(theGame setCursor: normalCursor)
	(Message MsgGet DEATH N_DEATH NULL reason 1 @messageStr)
	(Message MsgGet DEATH N_DEATH NULL reason 2 @titleStr)
	(= temp197 80)
	(= temp199 90)
	(= temp198 180)
	(= temp200 90)
	(= textY 10)
	(= temp202 10)
	(= iconX 5)
	(= iconY 10)
	(= buttonY 62)
	(= textX 72)
	(= printRet 0)
	(SetCursor 255 100)
	(while (not printRet)
		(Print
			font: SYSFONT
			addText: @titleStr
			font: userFont
			addText: @messageStr textX textY
			addIcon:
				(deathIcon view: deathView cel: 0 loop: deathLoop yourself:)
				0 0 iconX iconY
		)
		(switch
			(= printRet
				(Print
					addButton: buttonTryAgain N_DEATH 0 C_TRY_AGAIN 1 ((Print dialog?) nsLeft?) buttonY DEATH
					addButton: buttonRestore N_DEATH 0 C_RESTORE 1 80 buttonY DEATH
					addButton: buttonRestart N_DEATH 0 C_RESTART 1 160 buttonY DEATH
					addButton: buttonQuit N_DEATH 0 C_QUIT 1 ((Print dialog?) nsRight?) buttonY DEATH
					init:
				)
			)
			(buttonTryAgain
				(messager say: N_DEATH 0 C_TRY_AGAIN 2 0 DEATH)
				;revert window settings to their old selves
				(systemWindow back: oldSysBack)
				(theGame setCursor: oldCur)
				(sounds eachElementDo: #pause FALSE)
				(if revertState (revertState cue:))
				(= printRet -1)
			)
			(buttonRestore
				(= local19 0)
				(theGame restore:)
				(= printRet 0)
			)
			(buttonRestart
				(= local19 0)
				(theGame restart:)
				(= printRet 0)
			)
			(buttonQuit
				(= quit TRUE)
			)
		)
	)
	(soundFx dispose:)
	(deathMusic dispose:)
	(LoadMany FALSE DCICON DEATH)
)

(instance deathIcon of DCIcon
	(properties
		cycleSpeed 12
	)
	
	(method (init)
		(cond 
			((== local0 -1) ((= cycler (Forward new:)) init: self))
			((== local0 -2) ((= cycler (CycleTo new:)) init: self 10 1))
			(else (= cycler ((EndLoop new:) init: self cueScript yourself:)))
		)
	)
	
	(method (cycle)
		(if (!= [local2 cel] -1)
			(soundFx number: [local2 cel] play:)
		)
		(if
			(and
				(not cycler)
				(not local19)
				(== local0 -2)
				(not (-- local18))
			)
			(= local18 60)
			(= cycler ((EndLoop new:) init: self yourself:))
			(= local19 1)
			(return)
		)
		(super cycle:)
		(if (and cycler (cycler completed?))
			(cycler motionCue:)
		)
	)
)

(instance cueScript of Script
	(properties)
	
	(method (cue)
		(if (!= (deathIcon loop?) local0)
			(deathIcon loop: local0 cel: 0)
			(deathIcon init:)
		)
	)
)

(instance deathMusic of Sound
	(properties
		flags mNOPAUSE
	)
)

(instance SFX of Sound
	(properties
		flags mNOPAUSE
	)
)