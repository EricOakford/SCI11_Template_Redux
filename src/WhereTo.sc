;;; Sierra Script 1.0 - (do not remove this comment)
;	WHERETO.SC
;
;	If debugging is enabled, the game brings up this prompt.
;	You can go immediately to any room in the game.
;
;

(script# WHERE_TO)
(include game.sh) (include "4.shm")
(use Main)
(use Print)
(use Game)
(use System)

(public
	whereTo 0
)

(instance whereTo of Room
	(properties
		picture pSpeedTest
	)
	
	(method (init &tmp [str 10] nextRoom temp11)
		(super init:)
		(= normalCursor ((theIconBar curIcon?) cursor?))
		(theGame setCursor: ARROW_CURSOR TRUE 201 126)
		(= str 0)
		(= nextRoom 0)
		(= nextRoom
			(Print
				addText: N_ROOM 0 C_WHERETO 1 0 0 SPEED_TEST
				addEdit: @str 5 115 0
				init:
			)
		)
		(= nextRoom TITLE)
		(if str
			(= nextRoom (ReadNumber @str))
		)
		(theGame setCursor: normalCursor)
		(self newRoom: nextRoom)
	)
)


