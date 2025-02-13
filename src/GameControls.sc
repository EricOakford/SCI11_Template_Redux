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
(use GControl)
(use SlideIcon)
(use System)

(public
	theGameControls 0
	gcWin 1
)

(instance theGameControls of GameControls
	(method (init)
		(gcWin
			color: colBlack
			back: colGray4
			topBordColor: colWhite
			lftBordColor: colGray5
			rgtBordColor: colGray3
			botBordColor: colGray2
		)
		((= gameControls self)
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
				iconTextSwitch	;comment this out if you do not intend to have speech in your game
				(iconControlHelp
					cursor: helpCursor
					yourself:
				)
			eachElementDo: #highlightColor colBlack
			eachElementDo: #lowlightColor colGray4
			eachElementDo: #modNum GAME_CONTROLS
			helpIconItem: iconControlHelp
			curIcon: iconRestore
			okButton: iconOk
			eachElementDo: #highlightColor 0
			eachElementDo: #lowlightColor colGray4
			state: NOCLICKHELP
		)
	)
)

(define SLIDER_TITLE_TOP	37)
(define SLIDER_TOP 73)
(define SLIDER_LEFT 139)
(define SLIDDIST 40)
(define BUTTON_TOP 42)
(define BUTTON_LEFT 80)
(define BUTTON_DIST	20)
(define SCORE_TOP	13)

;for talkie games, these are for the display mode rectangle
(define MSGMODE_TOP 155)
(define MSGMODE_LEFT 140)
(define MSGMODE_BOTTOM 170)
(define MSGMODE_RIGHT 240)

(instance gcWin of BorderWindow
	(method (open &tmp
			savePort theBevelWid t l r b theColor theMaps
			bottomColor topColor leftColor rightColor thePri i
			theCel scoreHeight
			[str 15] [scoreBuf 15] [rectPt 4]
		)
		(= thePri -1)
		;if iconTextSwitch isn't commented out, let's assume it's
		; a talkie game, and load the correct cel for the extra button
		(if (gameControls contains: iconTextSwitch)
			(= theCel 2)
		else
			(= theCel 1)
		)
		(self
			top: (/ (- SCRNHIGH (+ (CelHigh vGameControls lControlFixtures 1) 6)) 2)
			left: (/ (- SCRNWIDE (+ 151 (CelWide vGameControls lSliderText 1))) 2)
			bottom:
				(+
					(CelHigh vGameControls lControlFixtures theCel)
					6
					(/ (- SCRNHIGH (+ (CelHigh vGameControls lControlFixtures theCel) 6)) 2)
				)
			right:
				(+
					151
					(CelWide vGameControls lSliderText 1)
					(/ (- SCRNWIDE (+ 151 (CelWide vGameControls lSliderText 1))) 2)
				)
			priority: thePri
		)
		(super open:)
		
		; Game Paused text
		(DrawCel vGameControls lSliderText 5
			(+
				(/
					(-
						(- (+ 151 (CelWide vGameControls lSliderText 1)) (+ 4 (CelWide vGameControls lControlFixtures theCel)))
						(CelWide vGameControls lSliderText 5)
					)
					2
				)
				4
				(CelWide vGameControls lControlFixtures 1)
			)
			3
			thePri
		)
		; Box for buttons
		(DrawCel vGameControls lControlFixtures theCel 4 3 thePri)
		; 1st arrow between sliders
		(DrawCel vGameControls lControlFixtures 0 94 38 thePri)
		; 2nd arrow between sliders
		(DrawCel vGameControls lControlFixtures 0 135 38 thePri)
		; Detail text
		(DrawCel vGameControls lSliderText 4 63 (- SLIDER_TITLE_TOP (+ (CelHigh vGameControls lSliderText 4) 3)) thePri)
		; Volume text
		(DrawCel vGameControls lSliderText 3 101 (- SLIDER_TITLE_TOP (+ (CelHigh vGameControls lSliderText 4) 3)) thePri)
		; Speed text
		(DrawCel vGameControls lSliderText 2 146 (- SLIDER_TITLE_TOP (+ (CelHigh vGameControls lSliderText 4) 3)) thePri)

		;Now draw the window below the sliders for score
		(Graph GShowBits 12 1 15 (+ 151 (CelWide vGameControls lSliderText 1)) 1)
		;The window will be sized accordingly whether this is a talkie game or not
		(if (gameControls contains: iconTextSwitch)
			(= scoreHeight (* SCORE_TOP 2))
		else
			(= scoreHeight SCORE_TOP)
		)
		(= b (+ (= t (+ 46 (CelHigh vGameControls lSliderText 1))) scoreHeight))
		(= r
			(+
				(= l (+ 10 (CelWide vGameControls lControlFixtures 1)))
				(-
					(+ 151 (CelWide vGameControls lSliderText 1))
					(+ 10 (CelWide vGameControls lControlFixtures 1) 6)
				)
			)
		)
		(= theColor 0)
		(= bottomColor colGray2)
		(= rightColor colGray3)
		(= leftColor colGray5)
		(= topColor colWhite)
		(= theBevelWid 3)
		(= theMaps VMAP)
		
		;draw the bevels
		(Graph GFillRect t l (+ b 1) (+ r 1) theMaps theColor thePri)
		(-= t theBevelWid)
		(-= l theBevelWid)
		(+= r theBevelWid)
		(+= b theBevelWid)
		(Graph GFillRect t l (+ t theBevelWid) r theMaps bottomColor thePri)
		(Graph GFillRect (- b theBevelWid) l b r theMaps topColor thePri)
		(for ((= i 0)) (< i theBevelWid) ((++ i))
			(Graph GDrawLine (+ t i) (+ l i) (- b (+ i 1)) (+ l i) rightColor thePri -1)
			(Graph GDrawLine (+ t i)(- r (+ i 1)) (- b (+ i 1)) (- r (+ i 1)) leftColor thePri -1)
		)
		(Graph GShowBits t l (+ b 1) (+ r 1) 1)
		
		;print the score centered in its window
		(Message MsgGet GAME_CONTROLS N_SCORE NULL NULL 1 @scoreBuf)
		(Format @str @scoreBuf score possibleScore)
		(TextSize @rectPt @str 999 0)
		(Display @str
			p_font 999
			p_color colGray4
			p_at
			(+ 10 (CelWide vGameControls lControlFixtures 1)
				(/
					(-
						(-
							(+ 151 (CelWide vGameControls lSliderText 1))
							(+ 10 (CelWide vGameControls lControlFixtures 1) 6)
						)
						[rectPt 3]
					)
					2
				)
			)
			(+ 46 (CelHigh vGameControls lSliderText 1) 3)
		)
		(SetPort 0)
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
		nsLeft (+ SLIDER_LEFT SLIDDIST)
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
		nsLeft (+ SLIDER_LEFT (* 2 SLIDDIST))
		nsTop SLIDER_TOP
		signal FIXED_POSN
		noun N_SPEED
		helpVerb V_HELP
		sliderView vGameControls
		bottomValue 15
	)
	
	(method (show)
		(if (not (user controls?))
			(= sliderCel 6)
			(= signal (| FIXED_POSN DISABLED))
		else
			(= sliderCel 0)
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
		nsTop (+ BUTTON_TOP BUTTON_DIST)
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
		nsTop (+ BUTTON_TOP (* BUTTON_DIST 2))
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
		nsTop (+ BUTTON_TOP (* BUTTON_DIST 3))
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
		nsLeft BUTTON_LEFT
		nsTop (+ BUTTON_TOP (* BUTTON_DIST 4))
		signal (| VICON FIXED_POSN RELVERIFY IMMEDIATE HIDEBAR)
		noun N_ABOUT
		helpVerb V_HELP
	)
)

(instance iconControlHelp of ControlIcon
	(properties
		view vGameControls
		loop lHelpButton
		cel 0
		nsLeft (+ BUTTON_LEFT 26)
		nsTop (+ BUTTON_TOP (* BUTTON_DIST 4))
		message V_HELP
		signal (| VICON FIXED_POSN RELVERIFY IMMEDIATE)
		noun N_HELP
		helpVerb V_HELP
	)
)

(instance iconOk of ControlIcon
	(properties
		view vGameControls
		loop lOKButton
		cel 0
		nsLeft BUTTON_LEFT
		nsTop (+ BUTTON_TOP (* BUTTON_DIST 5))
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
		nsTop (+ BUTTON_TOP (* BUTTON_DIST 6))
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
		
		;Now draw the rectangle for the display mode below the score
		(Graph GFillRect MSGMODE_TOP MSGMODE_LEFT MSGMODE_BOTTOM MSGMODE_RIGHT colBlack)
		(Graph GShowBits MSGMODE_TOP MSGMODE_LEFT MSGMODE_BOTTOM MSGMODE_RIGHT VMAP)
		(super show: &rest)
		(Message MsgGet GAME_CONTROLS N_MSGMODE NULL C_CURRENT_MODE 1 @textBuf)
		(Format @str @textBuf @modeBuf)
		(TextSize @rect @str 999 0)
		(Display @str
			p_font 999
			p_color colGray4
			p_at
			(+
				82
				(CelWide 947 1 1)
				(/
					(-
						(-
							(+ 151 (CelWide 947 0 1))
							(+ 10 (CelWide 947 1 1) 6)
						)
						[rect 3]
					)
					2
				)
			)
			(+ 92 (CelHigh 947 0 1) 3)
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