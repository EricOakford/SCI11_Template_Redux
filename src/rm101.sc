;;; Sierra Script 1.0 - (do not remove this comment)
(script# 101)
(include game.sh) (include "110.shm") (include "10.shm")
(use Main)
(use DeathRoom)
(use LoadMany)
(use Sound)
(use Motion)
(use Game)
(use Actor)
(use System)
(use Print)
(use Polygon)

(public
	rm110 0
)

(instance rm110 of Room
	(properties
		picture pRoom101
		style FADEOUT
		horizon 50
		vanishingX 130
		vanishingY 50
		noun N_ROOM
	)
	
	(method (init)
		(super init:)
		(switch prevRoomNum
			; Add room numbers here to set up the ego when coming from different directions.
			(else 
				(ego posn: 150 130)
			)
		)
		(ego init: normalize:)
		; We just came from the title screen, so we need to call this to give control
		; to the player.
		(theGame handsOn:)
		(box init: posn: 200 150)
		(deathBox init: posn: 100 150)
	)

	(method (doVerb theVerb)
		(switch theVerb
			(else 
				(super doVerb: theVerb)
			)
		)
	)
)

(instance box of View
	(properties
		view vBox
	)
	
	(method (doVerb theVerb)
		(switch theVerb
			(else
				(super doVerb: theVerb)
			)
		)
	)
)

(instance deathBox of View
	(properties
		view vDeathSkull
	)
	
	(method (doVerb theVerb)
		(switch theVerb
			(V_DO
				(EgoDead C_GENERIC)
			)
			(else
				(super doVerb: theVerb)
			)
		)
	)
)