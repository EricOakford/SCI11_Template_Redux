;;; Sierra Script 1.0 - (do not remove this comment)
(script# PROCS)
(include game.sh) (include "0.shm")
(use Main)
(use Print) 

(public
	Bset 0
	Bclr 1
	Btst 2
	Face 3
	EgoDead 4
	YesNoDialog 5
)
;These will be replaced with macro defines once those are supported
(procedure (Bset flagEnum)
;;;	(|= [gameFlags (/ flagEnum 16)] (>> $8000 (mod flagEnum 16))
	(gameFlags set: flagEnum)
)

(procedure (Bclr flagEnum)
;;;	(&= [gameFlags (/ flagEnum 16)] (~ (>> $8000 (mod flagEnum 16))))
	(gameFlags clear: flagEnum)
)

(procedure (Btst flagEnum)
;;;	(return
;;;		(&
;;;			[gameFlags (/ flagEnum 16)]
;;;			(>> $8000 (mod flagEnum 16))
;;;		)
;;;	)
	(gameFlags test: flagEnum)
)

(procedure (Face actor1 actor2 both whoToCue &tmp ang1To2 theX theY i)
	;This makes one actor face another.
	(= i 0)
	(if (IsObject actor2)
		(= theX (actor2 x?))
		(= theY (actor2 y?))
		(if (== argc 3) (= i both))
	else
		(= theX actor2)
		(= theY both)
		(if (== argc 4) (= i whoToCue))
	)
	(= ang1To2
		(GetAngle (actor1 x?) (actor1 y?) theX theY)
	)
	(actor1
		setHeading: ang1To2 (if (IsObject i) i else 0)
	)
)

(procedure (EgoDead theReason)
	;This procedure handles when ego dies. It closely matches that of SQ4, SQ5 and KQ6.
	;If a specific message is not given, the game will use a default message.
	(if (not argc)
		(= deathReason deathGENERIC)
	else
		(= deathReason theReason)
	)
	(curRoom newRoom: DEATH)
)

(procedure (YesNoDialog question &tmp oldCur)
	;this brings up a "yes or no" dialog choice.
	(= oldCur ((theIconBar curIcon?) cursor?))
	(theGame setCursor: normalCursor)
	(if modelessDialog
		(modelessDialog dispose:)
	)
	(return
		(Print
			font:		userFont
			width:		100
			mode:		teJustCenter
			addText:	question NULL NULL 1 0 0 MAIN
			addButton:	TRUE N_YESORNO NULL NULL 1 0 25 MAIN
			addButton:	FALSE N_YESORNO NULL NULL 2 75 25 MAIN
			init:
		)
	)
	(theGame setCursor: oldCur)
)

