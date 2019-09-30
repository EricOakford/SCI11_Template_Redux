;;; Sierra Script 1.0 - (do not remove this comment)
;
;	 MAIN.SC
;
;	 This is the main game script. It contains the main game class and all the global variables.
;	
;	 In addition to the above, it contains the crucial default Messager
;	 and its findTalker method (used for mapping talker numbers to a Talker or Narrator instance).
;
;

(script# MAIN)
(include game.sh) (include "0.shm")
(use BordWind)
(use Dialog)
(use Intrface)
(use StopWalk)
(use Polygon)
(use PolyPath)
(use Timer)
(use Ego)
(use Osc)
(use Grooper)
(use Print)
(use Talker)
(use IconBar)
(use Messager)
(use GControl)
(use Invent)
(use Sound)
(use Game)
(use User)
(use System)

(public
	SCI11 0
	Bset 1
	Bclr 2
	Btst 3
	AddPolygonsToRoom 4
	CreateNewPolygon 5
)

(local
	ego
	theGame
	curRoom
	unused_1
	quit
	cast
	regions
	timers
	sounds
	inventory
	addToPics
	curRoomNum
	prevRoomNum
	newRoomNum
	debugOn
	score
	possibleScore
	textCode
	cuees
	theCursor
	normalCursor =  ARROW_CURSOR
	waitCursor =  HAND_CURSOR
	userFont =  USERFONT
	smallFont =  4
	lastEvent
	modelessDialog
	bigFont =  USERFONT
	version
	unused_3
	curSaveDir
	unused_4
	perspective
	features
	unused_5
	useSortedFeatures
	unused_6
	overlays =  -1
	doMotionCue
	systemWindow
	unused_7
	unused_8
	modelessPort
	[sysLogPath 20]
	endSysLogPath
	gameControls
	ftrInitializer
	doVerbCode
	approachCode
	useObstacles =  TRUE
	unused_9
	theIconBar
	mouseX
	mouseY
	keyDownHandler
	mouseDownHandler
	directionHandler
	speechHandler
	lastVolume
	pMouse
	theDoits
	eatMice =  60
	user
	syncBias
	theSync
	unused_10
	fastCast
	inputFont
	tickOffset
	howFast
	gameTime
	narrator
	msgType =  TEXT_MSG
	messager
	prints
	walkHandler
	textSpeed =  2
	altPolyList
	global96
	global97
	global98
	lastSysGlobal
	myTextColor
	myBackColor
	myHighlightColor
	myLowlightColor
	isVGA
	debugging
	statusLine
	soundFx
	theMusic
	globalSound
	checkedIcons
	scoreFont
	colorCount
	musicChannels
	[gameFlags 10]
	theStopGroop
)

(procedure (Bset flagEnum)
	(= [gameFlags (/ flagEnum 16)]
		(|
			[gameFlags (/ flagEnum 16)]
			(>> $8000 (mod flagEnum 16))
		)
	)
)

(procedure (Bclr flagEnum)
	(= [gameFlags (/ flagEnum 16)]
		(&
			[gameFlags (/ flagEnum 16)]
			(~ (>> $8000 (mod flagEnum 16)))
		)
	)
)

(procedure (Btst flagEnum)
	(return
		(&
			[gameFlags (/ flagEnum 16)]
			(>> $8000 (mod flagEnum 16))
		)
	)
)

(procedure (CreateNewPolygonHelper polyBuffer nextPoly &tmp newPoly pointCount)
	(= newPoly (Polygon new:))
	(= pointCount (Memory MReadWord (+ polyBuffer 2)))
	(newPoly
		dynamic: FALSE
		type: (Memory MReadWord polyBuffer)
		size: pointCount
		; Use the points directly from the buffer:
		points: (+ polyBuffer 4)
	)
	; Tell the caller the position of the next poly, if they care:
	(if (> argc 1)
		(Memory MWriteWord
			nextPoly
			(+ polyBuffer 4 (* 4 pointCount))
		)
	)
	(return newPoly)
)

;	
;	 Creates Polygon objects based on the point lists in polyBuffer and adds
;	 them to the room's obstacles.
;	
;	 :param heapPtr polyBuffer: An array with polygon points.
;	
;	 Example usage::
;	
;	 	(AddPolygonsToRoom @P_ThePolygons)
;	
;	 The array begins with a number indicating how many polygons there are. This is followed
;	 by the following information for each polygon:
;	
;	 	- A number expressing the type of the polygon (e.g. PBarredAccess).
;	 	- A number indicating how many points are in the polygon.
;	 	- (x y) pairs of numbers for each point.
;	
;	 Example::
;	
;	 	[P_ThePolygons 19] = [2 PContainedAccess 4 319 189 319 50 0 50 0 189 PBarredAccess 3 319 189 319 50 0 50]
;	
;	 See also: :doc:`/polygons`.		
(procedure (AddPolygonsToRoom polyBuffer &tmp polyCount)
	(if (u< polyBuffer 100)
		(Prints {polyBuffer is not a pointer. Polygon ignored.})
	else
		(= polyCount (Memory MReadWord polyBuffer))
		(+= polyBuffer 2)
		(while polyCount
			(curRoom
				addObstacle:
					(if (== polyCount 1)
						(CreateNewPolygonHelper polyBuffer)
					else
						(CreateNewPolygonHelper polyBuffer @polyBuffer)
					)
			)
			(-- polyCount)
		)
	)
)

;
; .. function:: CreateNewPolygon(polyBuffer [nextPolyOptional])
;
; 	Creates a new polygon object.
; 	
; 	:param heapPtr polyBuffer: An array with polygon points.
; 	:param heapPtr nextPolyOptional: An optional pointer that receives the position of the next polygon in the buffer.
; 	
; 	Example usage::
; 	
; 		(aRock setOnMeCheck: omcPOLYGON (CreateNewPolygon @P_Rock))
;
; 	The array consists of the following:	
; 	
; 		- A number expressing the type of the polygon (e.g. PBarredAccess).
; 		- A number indicating how many points are in the polygon.
; 		- (x y) pairs of numbers for each point.
; 		
; 	Example::
; 	
; 		[P_Rock 10] = [PContainedAccess 4 319 189 319 50 0 50 0 189]
; 		
; 	See also: :doc:`/polygons`.
(procedure (CreateNewPolygon polyBuffer &tmp polyCount)
	(if (u< polyBuffer 100)
		(Prints {polyBuffer is not a pointer. Polygon ignored.})
		(return NULL)
	else
		(= polyCount (Memory MReadWord polyBuffer))
		(+= polyBuffer 2)
		(return (CreateNewPolygonHelper polyBuffer &rest))
	)
)

(class SCI11 kindof Game
	; The main game class. It's a subclass of Game and adds game-specific functionality.
	
	(properties
		printLang ENGLISH	;set your game's language here. Supported languages can be found in SYSTEM.SH.
	)

	(method (init &tmp [mD 22])
		;load some important modules
		Print
		BorderWindow
		DText
		DButton
		StopWalk
		Polygon
		PolyPath
		(ScriptID GAME_EGO)
		Timer
		IconBar
		Inventory
		(ScriptID SIGHT)
		Narrator
		Oscillate
		(super init:)

		;Assign globals to this script's objects
		(= theMusic musicSound)
		(= globalSound theGlobalSound)
		(= soundFx soundEffects)
		(= messager gameMessager)
		(= doVerbCode gameDoVerbCode)
		(= theStopGroop stopGroop)	
		(theMusic owner: self flags: mNOPAUSE init:)
		(globalSound owner: self flags: mNOPAUSE init:)
		(soundFx owner: self flags:	mNOPAUSE init:)
		(keyDownHandler addToFront:	self)
		(mouseDownHandler addToFront: self)		
		(= waitCursor theWaitCursor)

		;anything not requiring objects in this script is loaded in GAME_INIT.SC
		((ScriptID GAME_INIT 0) init:)
	)
	
	(method (startRoom roomNum)
		(if debugging
			((ScriptID DEBUG 0) init:)
		)
		
		(statusCode doit: roomNum)
		((ScriptID DISPOSE_CODE 0) doit: roomNum)
		;EO: despite what the message will say, the memory is NOT fragmented.
		;Disabling the message until I can find a way to prevent it from
		;appearing when changing rooms.
;;;		(if
;;;			(and
;;;				(!= (- (MemoryInfo FreeHeap) 2) (MemoryInfo LargestPtr))
;;;				(Print
;;;					addText: N_MEM_FRAGMENTED 0 0 0 1 0 MAIN
;;;					addButton: FALSE N_MEM_FRAGMENTED 0 0 2 0 12 MAIN
;;;					addButton: TRUE N_MEM_FRAGMENTED 0 0 3 70 12 MAIN
;;;					init:
;;;				)
;;;			)
;;;			(SetDebug)
;;;		)
		(super startRoom: roomNum)
		(if
			(and
				(ego cycler?)
				(not (ego looper?))
				((ego cycler?) isKindOf: StopWalk)
			)
			(ego setLoop: stopGroop)
		)
		(if (== (theIconBar curIcon?) (theIconBar at: ICON_CURITEM))
			(theIconBar curIcon: (theIconBar at: ICON_WALK))
		)	
	)	

	(method (handleEvent event &tmp oldCur)
		(super handleEvent: event)
		(if (event claimed?) (return TRUE))
		(return
			(switch (event type?)
				(keyDown
					(switch (event message?)
						(TAB
							(if (not (& ((theIconBar at: ICON_INVENTORY) signal?) DISABLED))
								(if fastCast (return fastCast))
								(ego showInv:)
								(event claimed: TRUE)
							)
						)
						(`^q	;KEY_CONTROL
							(theGame quitGame:)
							(event claimed: TRUE)
						)
						(`#2	;KEY_F2
							(cond 
								((theGame masterVolume:) (theGame masterVolume: 0))
								((> musicChannels 1) (theGame masterVolume: 15))
								(else (theGame masterVolume: 1))
							)
							(event claimed: TRUE)
						)
						(`#5	;KEY_F5
							(if (not (& ((theIconBar at: ICON_CONTROL) signal?) DISABLED))
								(if fastCast (return fastCast))
								(theGame save:)
								(event claimed: TRUE)
							)
						)
						(`#6	;KEY_F7
							(if (not (& ((theIconBar at: ICON_CONTROL) signal?) DISABLED))
								(if fastCast (return fastCast))
								(theGame restore:)
								(event claimed: TRUE)
							)
						)
					)
				)
			)
		)
	)
	
	(method (setCursor cursorObj tOrF theX theY &tmp oldCurObj moveToX moveToY)
		;this is the same as the original setCursor method
		;but, the cursors are objects now.
		;For reference, see cursor.sc

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
		
	(method (handsOff)
		(user canInput: TRUE, canControl: FALSE)
		(theIconBar eachElementDo: #perform checkIcon)
		(theIconBar curIcon: (theIconBar at: ICON_CONTROL))
		(theIconBar disable: ICON_WALK ICON_LOOK ICON_DO ICON_TALK ICON_CURITEM ICON_INVENTORY)
		(self setCursor: waitCursor TRUE)
	)
	
	(method (handsOn)
		(user canInput: TRUE, canControl: TRUE)
		(theIconBar enable: ICON_WALK ICON_LOOK ICON_DO ICON_TALK ICON_CURITEM ICON_INVENTORY)
		(if (not (curRoom inset:)) (theIconBar enable: ICON_CONTROL))
		(if (not (theIconBar curInvIcon?))
			(theIconBar disable: ICON_CURITEM)	
		)	
		(self setCursor: normalCursor TRUE)
	)
	
	(method (showControls)
		(theIconBar hide:)
		(gameControls
			window: (ScriptID GAME_CONTROLS 1)
			show:
		)
	)
	
	(method (solvePuzzle flagEnum points)
		;Adds an amount to the player's current score.
		;It checks if a certain flag is set so that the points are awarded only once.
		(if (not (Btst flagEnum))
			(= score (+ score points))
			(Bset flagEnum)
			(soundFx number: sPoints loop: 1 flags: mNOPAUSE play:)
		)
	)		
	
	(method (showAbout)
		((ScriptID GAME_ABOUT 0) doit:)
		(DisposeScript GAME_ABOUT)
	)
	;this will allow for digital sound volume to be adjusted	
	(method (masterAudioVolume newVol)
		(if argc (DoAudio Volume newVol) else (DoAudio Volume))
	)
	
	(method (restart)
		;the game's restart dialog
		(if
			(Print
				font:		userFont
				width:		100
				mode:		teJustCenter
				addText:	N_RESTART NULL NULL 1 0 0 MAIN
				addButton:	TRUE N_YESORNO NULL NULL 1 0 25 MAIN
				addButton:	FALSE N_YESORNO NULL NULL 2 75 25 MAIN
				init:
			)
			(super restart:)
		)
	)

	(method (quitGame)
		;the game's quit dialog
		(= quit
			(Print
				font:		userFont
				width:		100
				mode:		teJustCenter
				addText:	N_QUITGAME NULL NULL 1 0 0 MAIN
				addButton:	TRUE N_YESORNO NULL NULL 1 0 25 MAIN
				addButton:	FALSE N_YESORNO NULL NULL 2 75 25 MAIN
				init:
			)
		)
	)
	
	(method (pragmaFail)
		;nobody responds to user input
		(if modelessDialog (modelessDialog dispose:))
		(if (User canControl:)
			(switch ((user curEvent?) message?)
				(V_DO
					(messager say: N_PRAGFAIL V_DO 0 1 0 MAIN)
				)
				(V_LOOK
					(messager say: N_PRAGFAIL V_LOOK 0 1 0 MAIN)
				)
				(V_TALK
					(messager say: N_PRAGFAIL V_TALK 0 1 0 MAIN)
				)
				(else 
					(messager say: N_PRAGFAIL V_COMBINE 0 1 0 MAIN)
				)
			)
		)
	)
)

(instance statusCode of Code
	(properties)
	
	(method (doit roomNum &tmp [strg 50] [scoreStr 50])
		(if
			;add rooms where the status line is not shown
			(not (OneOf roomNum 
					TITLE SPEED_TEST WHERE_TO
				 )
			)
		(Message MsgGet MAIN N_STATUSLINE 0 0 1 @strg)
		(Format @scoreStr @strg score possibleScore)
		(DrawStatus @scoreStr 23 0)
		)
	)
)

(instance gameMessager of Messager
	(properties)
	
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

(instance gameDoVerbCode of Code
	(properties)
	
	(method (doit theVerb)
		(cond 
			((== theVerb V_LOOK) (messager say: N_VERB_GENERIC V_LOOK NULL 1 0 MAIN))
			((== theVerb V_DO) (messager say: N_VERB_GENERIC V_DO NULL 1 0 MAIN))
			((== theVerb V_TALK) (messager say: N_VERB_GENERIC V_TALK NULL 1 0 MAIN))
			(else (theGame pragmaFail:))
		)
	)
)

(instance stopGroop of GradualLooper)

(instance theWaitCursor of Cursor
	(properties
		view	HAND_CURSOR
	)
)

(instance theGlobalSound of Sound)
(instance musicSound of Sound)
(instance soundEffects of Sound)


(instance checkIcon of Code
	(properties)
	
	(method (doit param1)
		(if
			(and
				(param1 isKindOf: IconItem)
				(& (param1 signal?) RELVERIFY)
			)
			(= checkedIcons
				(| checkedIcons (>> $8000 (theIconBar indexOf: param1)))
			)
		)
	)
)
