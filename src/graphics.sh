;;; Sierra Script 1.0 - (do not remove this comment)\
;********************************************************************
;***
;***	GRAPHICS.SH
;***	  Defines for views and pictures
;***
;********************************************************************

;
; Ego views
(define	vEgo		0)
(define vEgoStand	1)
(define vEgoTalk	10)

;
; Speed tester
(define vSpeedTest 99)

;
; Inventory items
(define vInvItems	700)
	(define lInvCursors	1)

;
; Death icons
(define vDeathSkull 800)

;
; Interface views
(define vIcons	900)
	(enum
		lIconWalk
		lIconLook
		lIconHand
		lIconTalk
		lIconInvItem
		lIconInventory
		lIconExit
		lIconControls
		lIconScore
		lIconHelp
		lIconDisabled
	)	
(define vInvIcons 991)
	(enum
		lInvHand
		lInvHelp
		lInvLook
		lInvOK
		lInvSelect
		lInvMore
		lInvMoreDisabled
	)

(define vGameControls			947)
(define vGameControlsGerman		1026)
(define vGameControlsSpanish	1040)
(define vGameControlsFrench		1051) 
	(enum
		lSliderText
		lControlFixtures
		lSaveButton
		lRestoreButton
		lRestartButton
		lQuitButton
		lAboutButton
		lHelpButton
		lOKButton
		lTextMode
		lSpeechMode
		lDualMode
	)
	
	;
; Picture defines
(define pTestRoom 110)
