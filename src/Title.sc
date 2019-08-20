;;; Sierra Script 1.0 - (do not remove this comment)
;
;	TITLE.SC
;
;	Show the title screen, then the startup menu.
;
;

(script# TITLE)
(include game.sh) (include "100.shm")
(use Main)
(use Game)
(use Print)
(use Motion)
(use Actor)
(use System)

(public
	titleRm			0
)

(enum
	buttonStart
	buttonRestore
	buttonQuit
)

(instance titleRm of Room
	(properties
		style			PIXELDISSOLVE
		picture		pTITLE
	)
	(method (init)
		(super init: &rest)
		(self setScript:	sTitle)
	)
)

(instance sTitle of Script
	(method (changeState ns &tmp str nextRoom)
		(switchto (= state ns)
			(
				(= cycles 2)
			)
			(			
				(= seconds 3)
				(SetCursor normalCursor TRUE)
				(repeat
					(switch
						((= str (Print new:))
							posn: 9 165
							font: SYSFONT
							addButton: buttonStart N_ROOM 0 0 1 0 0 TITLE
							addButton: buttonRestore N_ROOM 0 0 2 115 0 TITLE
							addButton: buttonQuit N_ROOM 0 0 3 230 0 TITLE
							init:
						)
						(buttonStart
							(curRoom newRoom: TESTROOM)
							(break)
						)
						(buttonRestore
							(theGame restore:)
						)
						(buttonQuit
							(= quit TRUE)
							(break)
						)
					)
				)
			)
		)
	)
)