;;; Sierra Script 1.0 - (do not remove this comment)
(script# rTestRoom)
(include game.sh) (include "110.shm")
(include "110.shp")
(use Main)
(use Print)
(use Polygon)
(use Game)
(use Sound)
(use Actor)
(use System)

(public
	testRoom 0
)

(instance testRoom of Room
	(properties
		picture pWhite
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
		;set up polygons
		;room poly
		(curRoom addObstacle:
			(&getpoly {Room})
		)
		(ego init: normalize:)
		; We just came from the title screen, so we need to call this to give control
		; to the player.
		(theGame handsOn:)
	)
	
	(method (doVerb theVerb)
		(switch theVerb
			(else
				(super doVerb: theVerb)
			)
		)
	)
)