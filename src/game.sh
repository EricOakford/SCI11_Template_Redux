;;; Sierra Script 1.0 - (do not remove this comment)
;**************************************************************
;***
;***	GAME.SH--
;***
;**************************************************************


(include pics.sh) (include views.sh) ;graphical defines
(include system.sh) (include sci2.sh) ;system and kernel functions
(include talkers.sh) (include verbs.sh) ;message defines

(define	cBlack			%0000000000000001) ;**	Bit mapped controls
(define	cBlue			%0000000000000010)
(define	cGreen			%0000000000000100)
(define	cCyan			%0000000000001000)
(define	cRed			%0000000000010000)
(define	cMagenta		%0000000000100000)
(define	cBrown			%0000000001000000)
(define	cWhite			%0000000010000000)
(define	cLtBlack		%0000000100000000)
(define	cLtBlue			%0000001000000000)
(define	cLtGreen		%0000010000000000)
(define	cLtCyan			%0000100000000000)
(define	cLtRed			%0001000000000000)
(define	cLtMagenta		%0010000000000000)
(define	cLtBrown		%0100000000000000)
(define	cLtWhite		%1000000000000000)

;
; Global stuff

(define	MAIN			0)
(define GAME_WINDOW		1)
(define	DODISP			2)
(define	GAME_ROOM		3)
(define SPEED_TEST		4)
(define GAME_CONTROLS	5)
(define GAME_INV		6)
(define GAME_EGO		7)
(define DEBUG			8)
(define GAME_ABOUT		9)
(define DEATH			10)
(define GAME_ICONBAR	11)
(define GAME_INIT		12)
(define WHERE_TO		13)
(define DISPOSE_CODE	14)

;
; Actual rooms

(define	TITLE			100)
(define ROOM101			101)
(define	TESTROOM		110)

; Indices for the icons in the icon bar
(enum
	ICON_WALK
	ICON_LOOK
	ICON_DO
	ICON_TALK
	ICON_CUSTOM
	ICON_CURITEM
	ICON_INVENTORY
	ICON_CONTROL
	ICON_HELP
)

;Inventory items
(enum
	iMoney
	iLastInvItem	;this MUST be last
)

;Sound defines
(define sDeath 10)
(define sPoints 950)

;Death reasons
(enum 1
	deathGENERIC
)

;Event flags
(enum
	fInMainGame
	fAutoSaveOn
	fWonGame
)