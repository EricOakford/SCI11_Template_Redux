;;; Sierra Script 1.0 - (do not remove this comment)
(script# ROOM101)
(include game.sh) (include "101.shm") (include "10.shm")
(include "101.shp")
(use Main)
(use Death)
(use LoadMany)
(use Sound)
(use Motion)
(use Game)
(use Actor)
(use System)
(use Print)
(use Polygon)

(public
	rm101 0
)

(instance rm101 of Room
	(properties
		picture scriptNumber
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
				(ego posn: 160 130)
			)
		)
		;set up polygons
		;room polygons
		(curRoom addObstacle:
			((Polygon new:)
				type: PContainedAccess
				init:		
					61 101
					2 189
					319 189
					259 101
				yourself:
			)
		)
		;lamp base
		(curRoom addObstacle:
			((Polygon new:)
				type: PBarredAccess
				init:		
					232 151
					249 151
					249 161
					232 161
				yourself:
			)
		)
		;table
		(curRoom addObstacle:
			((Polygon new:)
				type: PBarredAccess
				init:
					85 129
					133 128
					134 146
					83 146
				yourself:
			)
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