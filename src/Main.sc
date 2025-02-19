;;; Sierra Script 1.0 - (do not remove this comment)
;
;	MAIN.SC
;
;	This is the main game script. It contains the main game instance and all the global variables.
;
;	In addition to the above, it contains the crucial default Messager
;	and its findTalker method (used for mapping talker numbers to a Talker or Narrator instance).
;

(script# MAIN)
(include game.sh) (include "0.shm")
(use GameEgo)
(use Procs)
(use Intrface)
(use Dialog)
(use Print)
(use Talker)
(use Messager)
(use PMouse)
(use Polygon)
(use PolyPath)
(use IconBar)
(use Feature)
(use Flags)
(use GameWindow)
(use BordWind)
(use Window)
(use Sound)
(use Game)
(use User)
(use System)

(public
	SCI11 0 ;Replace "SCI11" with the game's internal name (up to 6 characters)
)

(local
	ego								  	;pointer to ego
	theGame							  	;ID of the Game instance
	curRoom							  	;ID of current room
	unused_1		
	quit							  	;when TRUE, quit game
	cast							  	;collection of actors
	regions							  	;set of current regions
	timers							  	;list of timers in the game
	sounds							  	;set of sounds being played
	inventory						  	;set of inventory items in game
	addToPics						  	;list of views added to the picture
	curRoomNum						  	;current room number
	prevRoomNum						  	;previous room number
	newRoomNum						  	;number of room to change to
	debugOn							  	;generic debug flag -- set from debug menu
	score							  	;the player's current score
	possibleScore					  	;highest possible score
	textCode							;code that handles interactive text
	cuees							  	;list of who-to-cues for next cycle
	theCursor						  	;the number of the current cursor
	normalCursor	=	ARROW_CURSOR	;number of normal cursor form
	waitCursor		=	HAND_CURSOR 	;cursor number of "wait" cursor
	userFont		=	USERFONT	  	;font to use for Print
	smallFont		=	4 			  	;small font for save/restore, etc.
	lastEvent						  	;the last event (used by save/restore game)
	modelessDialog					  	;the modeless Dialog known to User and Intrface
	bigFont			=	USERFONT	  	;large font
	version			=	0			  	;pointer to 'incver' version string
										;	WARNING!  Must be set in room 0
										;	(usually to {x.yyy    } or {x.yyy.zzz})
	unused_3
	curSaveDir							;address of current save drive/directory string
	unused_4
	perspective							;player's viewing angle: degrees away
										;	from vertical along y axis
	features							;locations that may respond to events
	unused_5
	useSortedFeatures	=	FALSE		;enable cast & feature sorting?
	unused_6
	overlays			=	-1
	doMotionCue							;a motion cue has occurred - process it
	systemWindow						;ID of standard system window
	unused_7
	unused_8
	modelessPort
	[sysLogPath	20]						;-used for system standard logfile path	
	endSysLogPath						;/		(uses 20 globals)
	gameControls						;pointer to instance of game controls
	ftrInitializer						;pointer to code that gets called from
										;	a feature's init
	doVerbCode							;pointer to code that gets invoked if
										;	no feature claims a user event
	approachCode						;pointer to code that translates verbs
										;	into bits
	useObstacles	=	TRUE			;will Ego use PolyPath or not?
	unused_9
	theIconBar							;points to TheIconBar or Null	
	mouseX								;-last known mouse position
	mouseY								;/
	keyDownHandler						;-our EventHandlers, get called by game
	mouseDownHandler					;/
	directionHandler					;/
	speechHandler						;a special handler for speech events
	lastVolume
	pMouse			=	NULL			;pointer to a Pseudo-Mouse, or NULL
	theDoits		=	NULL			;list of objects to get doits each cycle
	eatMice			=	60				;how many ticks before we can mouse
	user			=	NULL			;pointer to specific applications User
	syncBias							;-globals used by sync.sc
	theSync								;/		(will be removed shortly)
	unused_10
	fastCast							;list of talkers on screen
	inputFont		=	SYSFONT			;font used for user type-in
	tickOffset							;used to adjust gameTime after restore
	howFast								;measurment of how fast a machine is
	gameTime							;ticks since game start
	narrator							;pointer to narrator (normally Narrator)
	msgType			=	TEXT_MSG		;type of messages used
	messager							;pointer to messager (normally Messager)
	prints								;list of Print's on screen
	walkHandler							;list of objects to get walkEvents
	textSpeed		=	2				;time text remains on screen
	altPolyList							;list of alternate obstacles
	;globals 96-99 are unused
		global96
		global97
		global98
	lastSysGlobal
	;globals 100 and above are for game use	
	theMusic	;music object, current playing music
	gameCode = 1234		;remnant from SCI1.0
	theMusic2	;second sound object, can be used for sound effects
	
	;standard globals for colors
	colBlack
	colGray1
	colGray2
	colGray3
	colGray4
	colGray5
	colWhite
	colDRed
	colLRed
	colVLRed
	colDYellow
	colYellow
	colLYellow
	colVDGreen
	colDGreen
	colLGreen
	colVLGreen
	colDBlue
	colBlue
	colLBlue
	colVLBlue
	colMagenta
	colLMagenta
	colCyan
	colLCyan
	;end standard color globals

	myTextColor				;color of text in message boxes
	myBackColor				;color of message boxes
	gameFlags				;pointer for Flags object, which only requires one global
	saveCursorX				; position of cursor when HandsOff is used
	saveCursorY				;
	numColors				;Number of colors supported by graphics driver
	numVoices				;Number of voices supported by sound driver
	debugging				;debug mode enabled
	isHandsOff				;ego can't be controlled
	egoLooper				;pointer for ego's stopGroop
	deathReason				;message to display when calling EgoDead
	theCurIcon
	iconSettings
)

;
; Global sound objects
(instance longSong of Sound
	(properties
		flags mNOPAUSE
	)
)

(instance longSong2 of Sound
	(properties
		flags mNOPAUSE
	)
)

;
;  Sound used only by theGame:solvePuzzle
(instance pointsSound of Sound
	(properties
		number sScore
		flags mNOPAUSE
	)
)

;
; The main game instance. It adds game-specific functionality.	
; Replace "SCI11" with the game's internal name (up to 6 characters)
(instance SCI11 of Game
	(properties
		printLang ENGLISH	;set your game's language here. Supported languages can be found in SYSTEM.SH.
	)

	(method (init)
		;these MUST be pre-loaded to prevent fragmentation
		Print
		DButton
		Narrator
		Polygon
		PolyPath
		(ScriptID SIGHT)
		
		;load up the standard game system
		(= systemWindow SysWindow)
		(= version {x.yyy})
		(super init: &rest)
		
		;set up the global sounds
		((= theMusic longSong)
			owner: self
			init:
		)
		((= theMusic2 longSong2)
			owner: self
			init:
		)
		
		(pointsSound
			owner: self
			init:
			setPri: 15
			setLoop: 1
		)
		
		;set up doVerb and feature initializer code
		(= doVerbCode gameDoVerbCode)
		(= ftrInitializer gameFtrInit)
		
		;assign code instances to variables
		(= pMouse PseudoMouse)
		(= messager gameMessager)
		(= approachCode gameApproachCode)
		(= handsOffCode gameHandsOff)
		(= handsOnCode gameHandsOn)
		((= gameFlags gameEventFlags)
			init:
		)
		((= altPolyList (List new:))
			name: {altPolys}
			add:
		)
		
		;set up the ego
		(= ego GameEgo)
		(= egoLooper (ScriptID GAME_EGO 1))
		(user alterEgo:  ego)

		;set the custom window here, not in GameInit!	
		(= systemWindow BorderWindow)
		
		;initialize the colors, icon bar, control panel, and inventory
		((ScriptID COLOR_INIT 0) doit:)
		((ScriptID GAME_ICONBAR 0) init:)
		((ScriptID GAME_CONTROLS 0) init:)
		((ScriptID GAME_INV 0) init:)
		
		;initialize everything else
		((ScriptID GAME_INIT 0) doit:)
		
		;now go to the speed tester
		(self newRoom: SPEED_TEST)
	)

	(method (startRoom n)
		(if modelessDialog (modelessDialog dispose:))
		((ScriptID DISPOSE 0) doit: n)
		; Check for frags
		(if (and	(!= (- (MemoryInfo FreeHeap) 2)
					(MemoryInfo LargestPtr))
				(Print
					addText: N_MEM_FRAGMENTED NULL NULL 1 0 0 MAIN
					addButton: FALSE N_MEM_FRAGMENTED NULL NULL 2 0 12 MAIN
					addButton: TRUE N_MEM_FRAGMENTED NULL NULL 3 70 12 MAIN
					init:
				)
			)
			(SetDebug)
		)
		(if debugging
			((ScriptID DEBUG 0) init:)
		)
		(super startRoom: n)
	)

	(method (pragmaFail &tmp theVerb)
		;nobody responds to user input
		(if modelessDialog
			(modelessDialog dispose:)
		)
		(if (user canInput:)
			(= theVerb ((user curEvent?) message?))
			(if (OneOf theVerb V_DO V_LOOK V_TALK)
				(messager say: N_PRAGFAIL theVerb NULL 1 0 MAIN)
			else ;non-handled verb
				(messager say: N_PRAGFAIL V_COMBINE NULL 1 0 MAIN)
			)
		)
	)

	(method (handleEvent event)
		(super handleEvent: event)
		(if (event claimed?) (return TRUE))
		(return
			(switch (event type?)
				(keyDown
					(switch (event message?)
						(TAB
							(if (not (& ((theIconBar at: ICON_INVENTORY) signal?) DISABLED))
								(ego showInv:)
							)
						)
						(SHIFTTAB
							(if (not (& ((theIconBar at: ICON_INVENTORY) signal?) DISABLED))
								(ego showInv:)
							)
						)
						(`^c
							(if (not (& ((theIconBar at: ICON_CONTROL) signal?) DISABLED))
								(theGame showControls:)
								(event claimed: TRUE)
							)
						)
						(`#2
							(cond
								((theGame masterVolume:)
									(theGame masterVolume: 0)
								)
								((> numVoices 1)
									(theGame masterVolume: 15)
								)
								(else
									(theGame masterVolume: 1)
								)
							)
							(event claimed: TRUE)
						)
						(`#5
							(if (not (& ((theIconBar at: ICON_CONTROL) signal?) DISABLED))
								(if fastCast (return))
								(theGame save:)
								(event claimed: TRUE)
							)
						)
						(`#7
							(if (not (& ((theIconBar at: ICON_CONTROL) signal?) DISABLED))
								(if fastCast (return))
								(theGame restore:)
								(event claimed: TRUE)
							)
						)
						(`#9
							(theGame restart:)
							(event claimed: TRUE)
						)
						(`^q
							(theGame quitGame:)
							(event claimed: TRUE)
						)
					)
				)
			)
		)
	)
	
	(method (setCursor cursorObj tOrF theX theY &tmp oldCurObj moveToX moveToY)
		;this is the same as the original setCursor method,
		;but the cursors are objects now.

		(= oldCurObj theCursor)
		(= theCursor cursorObj)
		(if (> argc 2)
			(= moveToX (if (< theX 0) 0 else theX))  ;this will fix off-screen cursor problem
			(= moveToY (if (< theY 0) 0 else theY))
			(SetCursor moveToX moveToY)
		)
		(if (IsObject cursorObj)
			(if argc
	  			((= theCursor cursorObj) init:)
			)
			(cursorObj init:)
		else
  			(SetCursor cursorObj 0 0)
		)
		(return oldCurObj)
	)
	
	(method (solvePuzzle pValue pFlag)
		;Adds an amount to the player's current score.
		;It checks if a certain flag is set so that the points are awarded only once.
		(if (and (> argc 1) (gameFlags test: pFlag))
			(return)
		)
		(if pValue
			(theGame changeScore: pValue)
			(if (and (> argc 1) pFlag)
				(gameFlags set: pFlag)
				(pointsSound play:)
			)
		)
	)

	(method (showAbout)
		((ScriptID GAME_ABOUT 0) doit:)
		(DisposeScript GAME_ABOUT)
	)
	
	(method (restart &tmp oldCur)
		;if a parameter is given, skip the dialog and restart immediately
		(if argc
			(super restart:)
		else
			;the game's restart dialog (can't use YesNoDialog proc here)
			(if modelessDialog
				(modelessDialog dispose:)
			)
			(= oldCur (self setCursor: normalCursor TRUE))
			(if
				(Print
					font:		userFont
					width:		100
					mode:		teJustCenter
					addText:	N_RESTART NULL NULL 1 0 0 MAIN
					addButton:	TRUE N_YESORNO NULL NULL 1 0 25 MAIN
					addButton:	FALSE N_YESORNO NULL NULL 2 70 25 MAIN
					init:
				)
				(super restart:)
			else
				(self setCursor: oldCur TRUE)
			)
		)
	)

	(method (quitGame &tmp oldCur)
		;if a parameter is given, skip the dialog and quit immediately
		(if argc
			(super quitGame:)
		else
			;the game's quit dialog (can't use YesNoDialog proc here)
			(if modelessDialog
				(modelessDialog dispose:)
			)
			(= oldCur (self setCursor: normalCursor TRUE))
			(if
				(Print
					font:		userFont
					width:		100
					mode:		teJustCenter
					addText:	N_QUITGAME NULL NULL 1 0 0 MAIN
					addButton:	TRUE N_YESORNO NULL NULL 1 0 25 MAIN
					addButton:	FALSE N_YESORNO NULL NULL 2 70 25 MAIN
					init:
				)
				(super quitGame:)
			else
				(self setCursor: oldCur TRUE)
			)
		)
	)

	;this must be used to bring up the control panel
	; to ensure that it always displays correctly.
	(method (showControls &tmp oldCur)
		(theIconBar hide:)
		(= oldCur ((theIconBar curIcon?) cursor?))
		(theGame setCursor: normalCursor TRUE)
		(gameControls
			window: (ScriptID GAME_CONTROLS 1)
			show:
		)
		(theGame setCursor: oldCur TRUE)		
	)
)

(instance gameDoVerbCode of Code
	;if there is no corresponding message for an object and verb, bring up a default message.
	(method (doit theVerb)
		(if (OneOf theVerb V_LOOK V_DO V_TALK)
			(messager say: N_VERB_GENERIC theVerb NULL 1 0 MAIN)
		else ;non-handled verb
			(messager say: N_VERB_GENERIC V_COMBINE NULL 1 0 MAIN)
		)
	)
)

(instance gameFtrInit of Code		; sets up defaults
	(method (doit theObj)
		; angle used by facingMe
		(if (== (theObj sightAngle?) ftrDefault)
			(theObj sightAngle: 90)
		)
		; maximum distance to get an object (for example.)
		; instance of Action or EventHandler with Actions
		(if (== (theObj actions?) ftrDefault)
			(theObj actions: 0)
		)
	)
)

(instance gameMessager of Messager
	(method (findTalker who &tmp theTalker)
		(if
			(= theTalker
				(switch who
					;Add the talkers here, using the defines you set in the message editor
					;from TALKERS.SH
					(else  narrator)
				)
			)
			(return)
		else
			(super findTalker: who)
		)
	)
)

(instance gameApproachCode of Code
	(method (doit theVerb)
		(switch theVerb
			(V_LOOK $0001)
			(V_TALK $0002)
			(V_WALK $0004)
			(V_DO $0008)
			(else  $8000)
		)
	)
)

(instance gameHandsOff of Code
	;Disable ego control
	(method (doit)
		(if (not theCurIcon)	; don't want to save it twice!
			(= theCurIcon (theIconBar curIcon?))
		)
		
		(= isHandsOff TRUE)
		(user
			canControl: FALSE
			canInput: FALSE
		)
		(ego setMotion: 0)
		
		; save the state of each icon so we can put the icon bar back the way it was
		(= iconSettings 0)
		(theIconBar eachElementDo: #perform checkIcon)
	
		; disable some icons so user doesn't screw us up
		(theIconBar disable:
			ICON_WALK
			ICON_LOOK
			ICON_DO
			ICON_TALK
			ICON_ITEM
			ICON_INVENTORY
		)
		
		; if no mouse, move the cursor out of the way, but save the initial
		; posn so HandsOn can restore it	
		(if (not (HaveMouse))
			(= saveCursorX ((user curEvent?) x?))
			(= saveCursorY ((user curEvent?) y?))
			(theGame setCursor: waitCursor TRUE 310 185)
		else
			(theGame setCursor: waitCursor TRUE)
		)
	)
)

(instance gameHandsOn of Code
	;Ensable ego control
	(method (doit)
		(= isHandsOff FALSE)
		(User
			canControl: TRUE
			canInput: TRUE
		)
		
		; re-enable iconbar
		(theIconBar enable:
			ICON_WALK
			ICON_LOOK
			ICON_DO
			ICON_TALK
			ICON_ITEM
			ICON_INVENTORY
			ICON_CONTROL
			ICON_HELP
		)
		(if (not (theIconBar curInvIcon?))
			(theIconBar disable: ICON_ITEM)
		)
	
		(if theCurIcon
			(theIconBar curIcon: theCurIcon)
			(theGame setCursor: ((theIconBar curIcon?) cursor?))
			(= theCurIcon 0)
			(if (and	(== (theIconBar curIcon?) (theIconBar at: ICON_ITEM))
						(not (theIconBar curInvIcon?))
					)
				(theIconBar advanceCurIcon:)
			)
		)	
		
		; restore cursor xy posn if no mouse
		(if (not (HaveMouse))
			(theGame setCursor:
				((theIconBar curIcon?) cursor?) TRUE saveCursorX saveCursorY
			)
		else
			(theGame setCursor:
				((theIconBar curIcon?) cursor?) TRUE
			)
		)
	)
)

(instance checkIcon of Code
	(method (doit theIcon)
		(if (theIcon isKindOf: IconItem)		; It's an icon
			(if (& (theIcon signal?) DISABLED)
				(|= iconSettings (>> $8000 (theIconBar indexOf: theIcon)))
			)
		)
	)
)

(instance gameEventFlags of Flags
	(properties
		size NUMFLAGS
	)
)