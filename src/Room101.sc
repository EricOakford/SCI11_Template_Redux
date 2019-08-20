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
		;set up polygons
		(AddPolygonsToRoom @P_room)
		(AddPolygonsToRoom @P_lampBase)
		(AddPolygonsToRoom @P_table)
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
	)

	(method (doVerb theVerb)
		(switch theVerb
			(else 
				(super doVerb: theVerb)
			)
		)
	)
)