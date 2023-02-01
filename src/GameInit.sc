;;; Sierra Script 1.0 - (do not remove this comment)
;
;	GAMEINIT.SC
;
;	Add things to initialize at game start here.
;	Make sure they don't require any objects or methods in MAIN.SC.
;
;
(script# GAME_INIT)
(include game.sh) (include "0.shm")
(use Main)
(use Talker)
(use BordWind)
(use System)

(local
	[quitStr 120]
)

(public
	gameInitCode 0
)

(instance gameInitCode of Code
	(method (doit)
		(= numVoices (DoSound NumVoices))
		(= numColors (Graph GDetect))
		(= useSortedFeatures TRUE)
		
		;When you quit the game, a random message will appear at the DOS prompt.
		;Customize these messages in the message editor as you see fit.
		(Message MsgGet MAIN N_QUIT_STR NULL NULL (Random 1 4) @quitStr)
		(SetQuitStr @quitStr)
		
		; These correspond to font codes used in messages.
		; By default, render messages in font 0 (system font).
		; Render messages with the |f1| tag in userFont (default 1).
		; Render messages with the |f2| tag in smallFont (default 4).
		; Render messages with the |f3| tag in font 1307.		
		(TextFonts SYSFONT userFont smallFont 1307)
		
		(= systemWindow BorderWindow)
		(theGame
			setCursor: theCursor TRUE 304 172
		)
		;at this point, the color globals have already been initialized,
		;so set the interface colors using them.	
		(= myTextColor colBlack)
		(= myBackColor colGray4)
		(systemWindow
			color: myTextColor
			back: myBackColor
			;comment these lines out if not using a BorderWindow!
			topBordColor: colWhite
			lftBordColor: colGray5
			rgtBordColor: colGray3
			botBordColor: colGray2
		)
		
		; These correspond to color codes used in messages (values into global palette).
		; By default, render messages as color 0.
		; Render messages with the |c1| tag as very light-red
		; Render messages with the |c2| tag as light-brown (dark yellow).
		; Render messages with the |c3| tag as dark gray.
		(TextColors colBlack colVLRed colDYellow colGray3)
		
		((= narrator Narrator)
			font: userFont
			color: myTextColor
			back: myBackColor
			keepWindow: TRUE
		)
		(= msgType TEXT_MSG)
		(= eatMice 30)
		(= possibleScore 999)
		(= score 0)
		(= debugging TRUE)	;Set this to FALSE to disable the debug features
		(theGame handsOff:)
		(DisposeScript GAME_INIT)	;don't need this in memory anymore
	)
)