;;; Sierra Script 1.0 - (do not remove this comment)
;
;	CONTROLS.SC
;
;	This script contains the game's control panel and its controls.
;
;

(script# GAME_CONTROLS)
(include game.sh) (include "5.shm")
(use Main)
(use BordWind)
(use SlideIcon)
(use IconBar)
(use GControl)
(use Window)
(use System)

(public
	gcCode 0
	gcWin 1
)

(define BUTTON_X 56)
(define BUTTON_Y 42)
(define HEADER_X 80)
(define INDICATOR_X 70)
(define INDICATOR_Y 40)
(define CONTROL_WIDTH 200)
(define SLIDER_X 130)
(define SLIDER_Y 73)
(define MSG_BUTTON_X 190)
(define MSG_BUTTON_Y 140)

(enum	;header cels
	celDial
	celSlider
	celSpeed
	celVolume
	celDetail
	celPaused
)

(instance gcCode of Code
	(method (init)
		((= gameControls GameControls)
			window: gcWin
			add:
				iconOk
				(detailSlider theObj: theGame selector: #detailLevel yourself:)
				(volumeSlider theObj: theGame selector: #masterVolume yourself:)
				(speedSlider theObj: ego selector: #setSpeed yourself:)
				(iconSave theObj: theGame selector: #save yourself:)
				(iconRestore theObj: theGame selector: #restore yourself:)
				(iconRestart theObj: theGame selector: #restart yourself:)
				(iconQuit theObj: theGame selector: #quitGame yourself:)
				(iconAbout theObj: theGame selector: #showAbout yourself:)
				iconTextSwitch	;comment this out if you do not intend to have speech in your game
				iconHelp
			eachElementDo: #highlightColor 40
			eachElementDo: #lowlightColor 5
			eachElementDo: #modNum GAME_CONTROLS
			helpIconItem: iconHelp
			curIcon: iconSave
			okButton: iconOk
			state: NOCLICKHELP
		)
	)
)

(instance gcWin of BorderWindow
	(method (open &tmp [ofStr 25] priority)
		(= priority -1)
		(self
			top: (/ (- SCRNHIGH (+ (CelHigh vControlIcons lControlFixtures 1) 6)) 2)
			left: (/ (- SCRNWIDE (+ CONTROL_WIDTH (CelWide vControlIcons lSliderText 1))) 2)
			bottom:
				(+
					(CelHigh vControlIcons lControlFixtures 1)
					6
					(/ (- SCRNHIGH (+ (CelHigh vControlIcons lControlFixtures 1) 6)) 2)
				)
			right:
				(+
					CONTROL_WIDTH
					(CelWide vControlIcons lSliderText 1)
					(/ (- SCRNWIDE (+ CONTROL_WIDTH (CelWide vControlIcons lSliderText 1))) 2)
				)
			priority: priority
		)
		(super open:)
		(DrawCel vControlIcons lSliderText celPaused
			(+
				(/
					(-
						(- (+ CONTROL_WIDTH (CelWide vControlIcons lSliderText celSlider)) (+ 4 (CelWide vControlIcons lControlFixtures celSlider)))
						(CelWide vControlIcons lSliderText 5)
					)
					2
				)
				4
				(CelWide vControlIcons lControlFixtures 1)
			)
			3
			priority
		)
		(DrawCel vControlIcons lControlFixtures 1 4 3 priority)	;button holes
		(DrawCel vControlIcons lControlFixtures 0 INDICATOR_X INDICATOR_Y priority)	;detail indicator
		(DrawCel vControlIcons lControlFixtures 0 (+ INDICATOR_X 50) INDICATOR_Y priority)	;volume indicator
		(DrawCel vControlIcons lControlFixtures 0 (+ INDICATOR_X 100) INDICATOR_Y priority)	;speed indicator
		
		(DrawCel	;detail header
			vControlIcons lSliderText celDetail HEADER_X
			(- (- 37 (+ (CelHigh vControlIcons lSliderText 4) 3)) 9)
			priority
		)
		(DrawCel	;volume header
			vControlIcons lSliderText celVolume (+ HEADER_X 50)
			(- (- 37 (+ (CelHigh vControlIcons lSliderText 4) 3)) 9)
			priority
		)
		(DrawCel	;speed header
			vControlIcons lSliderText celSpeed (+ HEADER_X 100)
			(- (- 37 (+ (CelHigh vControlIcons lSliderText 4) 3)) 9)
			priority
		)
		(Graph GShowBits 12 1 15 (+ CONTROL_WIDTH (CelWide vControlIcons lSliderText 1)) 1)
		(SetPort 0)
	)
)


(instance detailSlider of Slider
	(properties
		view vControlIcons
		loop lSliderText
		cel 1
		nsLeft SLIDER_X
		nsTop SLIDER_Y
		signal FIXED_POSN
		noun N_DETAIL
		helpVerb V_HELP
		sliderView vControlIcons
		bottomValue 1
		topValue 3
	)
)

(instance volumeSlider of Slider
	(properties
		view vControlIcons
		loop lSliderText
		cel 1
		nsLeft (+ SLIDER_X 50)
		nsTop SLIDER_Y
		signal FIXED_POSN
		noun N_VOLUME
		helpVerb V_HELP
		sliderView vControlIcons
		topValue 15
	)
)
	

(instance speedSlider of Slider
	(properties
		view vControlIcons
		loop lSliderText
		cel 1
		nsLeft (+ SLIDER_X 100)
		nsTop SLIDER_Y
		signal FIXED_POSN
		noun N_SPEED
		helpVerb V_HELP
		sliderView vControlIcons
		bottomValue 15
	)
	
	(method (show)
		(if (not (user controls?))
			(= signal (| FIXED_POSN DISABLED))
		else
			(= signal FIXED_POSN)
		)
		(super show: &rest)
	)
	
	(method (mask)
	)
	
	(method (move)
		(if (user controls?)
			(super move: &rest)
		)
	)
)


(instance iconSave of ControlIcon
	(properties
		view vControlIcons
		loop lSaveButton
		cel 0
		nsLeft BUTTON_X
		nsTop BUTTON_Y
		noun N_SAVE
		signal (| VICON FIXED_POSN RELVERIFY IMMEDIATE HIDEBAR)
		helpVerb V_HELP
	)
)

(instance iconRestore of ControlIcon
	(properties
		view vControlIcons
		loop lRestoreButton
		cel 0
		nsLeft BUTTON_X
		nsTop (+ BUTTON_Y 20)
		signal (| VICON FIXED_POSN RELVERIFY IMMEDIATE HIDEBAR)
		noun N_RESTORE
		helpVerb V_HELP
	)
)

(instance iconRestart of ControlIcon
	(properties
		view vControlIcons
		loop lRestartButton
		cel 0
		nsLeft BUTTON_X
		nsTop (+ BUTTON_Y 40)
		signal (| VICON FIXED_POSN RELVERIFY IMMEDIATE HIDEBAR)
		noun N_RESTART
		helpVerb V_HELP
	)
)

(instance iconQuit of ControlIcon
	(properties
		view vControlIcons
		loop lQuitButton
		cel 0
		nsLeft BUTTON_X
		nsTop (+ BUTTON_Y 60)
		signal (| VICON FIXED_POSN RELVERIFY IMMEDIATE HIDEBAR)
		noun N_QUIT
		helpVerb V_HELP
	)
)

(instance iconAbout of ControlIcon
	(properties
		view vControlIcons
		loop lAboutButton
		cel 0
		nsLeft BUTTON_X
		nsTop (+ BUTTON_Y 80)
		signal (| VICON FIXED_POSN RELVERIFY IMMEDIATE HIDEBAR)
		noun N_ABOUT
		helpVerb V_HELP
	)
)

(instance iconHelp of ControlIcon
	(properties
		view vControlIcons
		loop lHelpButton
		cel 0
		nsLeft (+ BUTTON_X 26)
		nsTop (+ BUTTON_Y 80)
		cursor vHelpCursor
		message V_HELP
		signal (| VICON FIXED_POSN RELVERIFY IMMEDIATE)
		noun N_HELP
		helpVerb V_HELP
	)
)

(instance iconOk of ControlIcon
	(properties
		view vControlIcons
		loop lOKButton
		cel 0
		nsLeft BUTTON_X
		nsTop (+ BUTTON_Y 100)
		signal (| VICON FIXED_POSN RELVERIFY IMMEDIATE HIDEBAR)
		noun N_OK
		helpVerb V_HELP
	)
)

(instance iconTextSwitch of IconItem
	(properties
		view vControlIcons
		loop lModeButton
		cel 0
		nsLeft 137
		nsTop 143
		cursor ARROW_CURSOR
		signal (| VICON FIXED_POSN RELVERIFY IMMEDIATE)
		noun N_MSGMODE
		helpVerb V_HELP
	)
	
	(method (doit)
		(switch msgType
			(TEXT_MSG
				(= msgType CD_MSG)
			)
			(CD_MSG
				(= msgType (| TEXT_MSG CD_MSG))
			)
			;The stock system scripts do not properly support both speech and text.
			;To implement this, I made slight modifications to Narrator:display.
			((| TEXT_MSG CD_MSG)
				(= msgType TEXT_MSG)
			)
		)
		(self show:)
	)
	
	(method (show)
		(switch msgType
			(TEXT_MSG
				(DrawCel vControlIcons lCurrentMode 0 MSG_BUTTON_X MSG_BUTTON_Y -1)
			)
			(CD_MSG
				(DrawCel vControlIcons lCurrentMode 1 MSG_BUTTON_X MSG_BUTTON_Y -1)
			)
			((| TEXT_MSG CD_MSG)
				(DrawCel vControlIcons lCurrentMode 2 MSG_BUTTON_X MSG_BUTTON_Y -1)
			)
		)
		(Graph GShowBits MSG_BUTTON_Y MSG_BUTTON_X
			(+ MSG_BUTTON_Y (CelHigh vControlIcons lCurrentMode))
			(+ MSG_BUTTON_X (CelWide vControlIcons lCurrentMode))
			1
		)
		(super show: &rest)
	)
)