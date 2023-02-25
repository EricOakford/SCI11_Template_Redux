;;; Sierra Script 1.0 - (do not remove this comment)
;
;	GAMEEGO.SC
;
;	 GameEgo is a game-specific subclass of Ego. Here, you can put default answers for
;	 looking, talking and performing actions on yourself.
;
;
(script# GAME_EGO)
(include game.sh) (include "7.shm")
(use Main)
(use Intrface)
(use StopWalk)
(use Grooper)
(use User)
(use Invent)
(use Procs)
(use Ego)

(public
	GameEgo 0
	stopGroop 1
)

(class GameEgo of Ego
	(properties
		name {ego}
		noun N_EGO
		modNum GAME_EGO
		view vEgo
	)
	
	(method (doVerb theVerb)
		;add interactivity with the player character here
		(switch theVerb
			(else 
				(super doVerb: theVerb &rest)
			)
		)
	)
	
	(method (normalize theLoop theView stopView &tmp sView)
		;normalizes ego's animation
		(= stopView 0)
		(if (> argc 0)
			(ego loop: theLoop)
			(if (> argc 1)
				(ego view: theView)
				(if (> argc 2)
					(= stopView sView)
				)
			)
		)
		(if (not stopView)
			(= stopView vEgoStand)
		)
		(ego
			setLoop: -1
			setLoop: stopGroop
			setPri: -1
			setMotion: FALSE
			setCycle: egoStopWalk stopView
			setStep: 4 2
			illegalBits: 0
			ignoreActors: FALSE
			ignoreHorizon: TRUE
		)
	)

	(method (showInv &tmp oldCur)
		;bring up the inventory
		(theIconBar hide:)
		(inventory showSelf: ego)
	)
)

(instance stopGroop of GradualLooper)

(instance egoStopWalk of StopWalk)