;;; Sierra Script 1.0 - (do not remove this comment)
;
;	GAMECONTROLS.SC
;
;	This script contains the game's control panel and its controls.
;
;
(script# GAME_CONTROLS)
(include game.sh) (include "5.shm")
(use Main)
(use BordWind)
(use Window)
(use IconBar)
(use Procs)
(use SlideIcon)
(use GControl)
(use System)

(public
	gcCode 0
	gcWin 1
)

(instance gcCode of Code
	(method (doit)
		(gcWin
			color: colBlack
			back: colGray4
			topBordColor: colWhite
			lftBordColor: colGray5
			rgtBordColor: colGray3
			botBordColor: colGray2
		)
		((= gameControls GameControls)
			window: gcWin
			add:
				iconOk
				(detailSlider
					theObj: theGame
					selector: #detailLevel
					yourself:
				)
				(volumeSlider
					theObj: theGame
					selector: #masterVolume
					yourself:
				)
				(speedSlider
					theObj: ego
					selector: #setSpeed
					yourself:
				)
				(iconSave
					theObj: theGame
					selector: #save
					yourself:
				)
				(iconRestore
					theObj: theGame
					selector: #restore
					yourself:
				)
				(iconRestart
					theObj: theGame
					selector: #restart
					yourself:
				)
				(iconQuit
					theObj: theGame
					selector: #quitGame
					yourself:
				)
				(iconAbout
					theObj: theGame
					selector: #showAbout
					yourself:
				)
				;iconTextSwitch	;comment this out if you do not intend to have speech in your game
				(iconControlHelp
					cursor: helpCursor
					yourself:
				)
			helpIconItem: iconControlHelp
			curIcon: iconRestore
			okButton: iconOk
			eachElementDo: #highlightColor colBlack
			eachElementDo: #lowlightColor colGray4
			eachElementDo: #modNum GAME_CONTROLS
			state: NOCLICKHELP
		)
	)
)

(define	SLIDER_TOP	37)
(define SLIDER_LEFT	67)
(define BUTTON_TOP 6)
(define BUTTON_LEFT 8)

(instance gcWin of BorderWindow
	(method (open &tmp
			theBevelWid t l r b theColor theMaps bottomColor topColor leftColor rightColor
			thePri i [str 20] [msgBuf 20] [rectPt 34] fixtureCel
			)
		(self
			top: (/ (- SCRNHIGH (+ (CelHigh vGameControls lControlFixtures 1) 6)) 2)
			left: (/ (- SCRNWIDE (+ 151 (CelWide vGameControls lSliderText 1))) 2)
			bottom:
				(+
					(CelHigh vGameControls lControlFixtures 1)
					6
					(/ (- SCRNHIGH (+ (CelHigh vGameControls lControlFixtures 1) 6)) 2)
				)
			right:
				(+
					151
					(CelWide vGameControls lSliderText 1)
					(/ (- SCRNWIDE (+ 151 (CelWide vGameControls lSliderText 1))) 2)
				)
			priority: 15
		)
		(super open:)
		
		; Game Paused text
		(DrawCel vGameControls lSliderText 5
			(+
				(/
					(-
						(- (+ 151 (CelWide vGameControls lSliderText 1)) (+ 4 (CelWide vGameControls lControlFixtures 1)))
						(CelWide vGameControls lSliderText 5)
					)
					2
				)
				4
				(CelWide vGameControls lControlFixtures 1)
			)
			3
			15
		)

		; Box for buttons.
		; if iconTextSwitch is not commented out, assume it's a talkie game,
		; so use the correct cel for the button box.
		(if (gameControls contains: iconTextSwitch)
			(= fixtureCel 2)
		else
			(= fixtureCel 1)
		)
		(DrawCel vGameControls lControlFixtures fixtureCel 4 3 15)
		; 1st arrow between sliders
		(DrawCel vGameControls lControlFixtures 0 94 38 15)
		; 2nd arrow between sliders
		(DrawCel vGameControls lControlFixtures 0 135 38 15)
		; Detail text
		(DrawCel vGameControls lSliderText 4 63 (- SLIDER_TOP (+ (CelHigh vGameControls lSliderText 4) 3)) 15)
		; Volume text
		(DrawCel vGameControls lSliderText 3 101 (- SLIDER_TOP (+ (CelHigh vGameControls lSliderText 4) 3)) 15)
		; Speed text
		(DrawCel vGameControls lSliderText 2 146 (- SLIDER_TOP (+ (CelHigh vGameControls lSliderText 4) 3)) 15)
		(= b (+ (= t (+ 49 (CelHigh vGameControls lSliderText 1))) 26))
		(= r
			(+
				(= l (+ 10 (CelWide vGameControls lControlFixtures 1)))
				(-
					(+ 151 (CelWide vGameControls lSliderText 1))
					(+ 10 (CelWide vGameControls lControlFixtures 1) 6)
				)
			)
		)
		(= thePri 15)
		(= theColor 0)
		(= bottomColor colGray2)
		(= rightColor colGray3)
		(= leftColor colGray5)
		(= topColor colWhite)
		(= theBevelWid 3)
		(= theMaps 3)
		(Graph GFillRect t l (+ b 1) (+ r 1) theMaps theColor thePri)
		(-= t theBevelWid)
		(-= l theBevelWid)
		(+= r theBevelWid)
		(+= b theBevelWid)
		(Graph GFillRect t l (+ t theBevelWid) r theMaps bottomColor thePri)
		(Graph GFillRect (- b theBevelWid) l b r theMaps topColor thePri)
		(for ((= i 0)) (< i theBevelWid) ((++ i))
			(Graph GDrawLine (+ t i) (+ l i) (- b (+ i 1)) (+ l i) rightColor thePri -1)
			(Graph GDrawLine (+ t i) (- r (+ i 1)) (- b (+ i 1)) (- r (+ i 1)) leftColor thePri -1)
		)
		(Graph GShowBits t l (+ b 1) (+ r 1) 1)
		(Message MsgGet GAME_CONTROLS N_SCORE NULL NULL 1 @msgBuf)
		(Format @str @msgBuf score possibleScore)
		(TextSize @rectPt @str 999 0)
		(Display @str
			p_font 999
			p_color colGray5
			p_at
			(+
				10
				(CelWide vGameControls lControlFixtures fixtureCel)
				(/
					(-
						(-
							(+ 151 (CelWide vGameControls lSliderText 1))
							(+ 10 (CelWide vGameControls lControlFixtures fixtureCel) 6)
						)
						[rectPt 3]
					)
					2
				)
			)
			;keep the text centered whether it's a talkie game or not
			(if (gameControls contains: iconTextSwitch)
				(+ 49 (CelHigh vGameControls lSliderText 1) 3 10)
			else
				(+ 49 (CelHigh vGameControls lSliderText 1) 3 6)
			)
		)
	)
)

(instance detailSlider of Slider
	(properties
		view vGameControls
		loop lSliderText
		cel 1
		nsLeft SLIDER_LEFT
		nsTop SLIDER_TOP
		signal FIXED_POSN
		noun N_DETAIL
		helpVerb V_HELP
		sliderView vGameControls
		topValue 3
	)
)

(instance volumeSlider of Slider
	(properties
		view vGameControls
		loop lSliderText
		cel 1
		nsLeft (+ SLIDER_LEFT 40)
		nsTop SLIDER_TOP
		signal FIXED_POSN
		noun N_VOLUME
		helpVerb V_HELP
		sliderView vGameControls
		topValue 15
	)
)

(instance speedSlider of Slider
	(properties
		view vGameControls
		loop lSliderText
		cel 1
		nsLeft (+ SLIDER_LEFT (* 2 40))
		nsTop SLIDER_TOP
		signal FIXED_POSN
		noun N_SPEED
		helpVerb V_HELP
		sliderView vGameControls
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
		view vGameControls
		loop lSaveButton
		cel 0
		nsLeft BUTTON_LEFT
		nsTop BUTTON_TOP
		noun N_SAVE
		signal (| VICON FIXED_POSN RELVERIFY IMMEDIATE HIDEBAR)
		helpVerb V_HELP
	)
)

(instance iconRestore of ControlIcon
	(properties
		view vGameControls
		loop lRestoreButton
		cel 0
		nsLeft BUTTON_LEFT
		nsTop (+ BUTTON_TOP 20)
		signal (| VICON FIXED_POSN RELVERIFY IMMEDIATE HIDEBAR)
		noun N_RESTORE
		helpVerb V_HELP
	)
)

(instance iconRestart of ControlIcon
	(properties
		view vGameControls
		loop lRestartButton
		cel 0
		nsLeft BUTTON_LEFT
		nsTop (+ BUTTON_TOP (* 20 2))
		signal (| VICON FIXED_POSN RELVERIFY IMMEDIATE HIDEBAR)
		noun N_RESTART
		helpVerb V_HELP
	)
)

(instance iconQuit of ControlIcon
	(properties
		view vGameControls
		loop lQuitButton
		cel 0
		nsLeft BUTTON_LEFT
		nsTop (+ BUTTON_TOP (* 20 3))
		signal (| VICON FIXED_POSN RELVERIFY IMMEDIATE HIDEBAR)
		noun N_QUIT
		helpVerb V_HELP
	)
)

(instance iconAbout of ControlIcon
	(properties
		view vGameControls
		loop lAboutButton
		cel 0
		nsLeft (+ BUTTON_LEFT 26)
		nsTop (+ BUTTON_TOP (* 20 4))
		signal (| VICON FIXED_POSN RELVERIFY IMMEDIATE HIDEBAR)
		noun N_ABOUT
		helpVerb V_HELP
	)
)

(instance iconControlHelp of IconItem
	(properties
		view vGameControls
		loop lHelpButton
		cel 0
		type helpEvent
		nsLeft BUTTON_LEFT
		nsTop (+ BUTTON_TOP (* 20 4))
		message V_HELP
		signal (| VICON FIXED_POSN RELVERIFY IMMEDIATE)
		noun N_HELP
		helpVerb V_HELP
	)
)

(instance iconOk of IconItem
	(properties
		view vGameControls
		loop lOKButton
		cel 0
		nsLeft BUTTON_LEFT
		nsTop (+ BUTTON_TOP (* 20 5))
		signal (| VICON FIXED_POSN RELVERIFY IMMEDIATE HIDEBAR)
		noun N_OK
		helpVerb V_HELP
	)
)

(instance iconTextSwitch of IconItem
	(properties
		view vGameControls
		loop lTextMode
		cel 0
		nsLeft BUTTON_LEFT
		nsTop (+ BUTTON_TOP (* 20 6))
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
	
	(method (show &tmp [str 25] [rect 4] [textBuf 20] [modeBuf 10])
		(switch msgType
			(TEXT_MSG
				(self loop: lSpeechMode)
				(Message MsgGet GAME_CONTROLS N_MSGMODE NULL C_TEXT_MODE 1 @modeBuf)
			)
			(CD_MSG
				(self loop: lDualMode)
				(Message MsgGet GAME_CONTROLS N_MSGMODE NULL C_SPEECH_MODE 1 @modeBuf)
			)
			((| TEXT_MSG CD_MSG)
				(self loop: lTextMode)
				(Message MsgGet GAME_CONTROLS N_MSGMODE NULL C_DUAL_MODE 1 @modeBuf)
			)
		)
		(Graph GFillRect 114 73 121 164 colBlack)
		(Graph GShowBits 114 73 121 164 1)
		(super show: &rest)
		(Message MsgGet GAME_CONTROLS N_MSGMODE NULL C_CURRENT_MODE 1 @textBuf)
		(Format @str @textBuf @modeBuf)
		(TextSize @rect @str 999 0)
		(Display @str
			p_font 999
			p_color colGray5
			p_at
			(+
				10
				(CelWide vGameControls lControlFixtures 2)
				(/
					(-
						(-
							(+ 151 (CelWide vGameControls lSliderText 1))
							(+ 10 (CelWide vGameControls lControlFixtures 2) 6)
						)
						[rect 3]
					)
					2
				)
			)
			(+ 49 (CelHigh vGameControls lSliderText 1) 3)
		)
	)
)

(instance helpCursor of Cursor
	(properties
		view vIcons
		loop lIconHelp
		cel 2
	)
)