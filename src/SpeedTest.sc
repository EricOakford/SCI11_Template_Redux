;;; Sierra Script 1.0 - (do not remove this comment)
;
;	SPEEDTEST.SC
;
;	This is the script that checks the machine speed, then starts the game proper.
;
;

(script# SPEED_TEST)
(include game.sh)
(use Main)
(use LoadMany)
(use Motion)
(use Game)
(use Actor)
(use System)

(public
	speedRoom 0
)

(local
	distance
	endTime
)

(instance speedRoom of Room
	(properties
		picture pSpeedTest
	)
	
	(method (init)
		(LoadMany RES_VIEW vTestBlock)
		(super init:)
		(theGame handsOff:)
		(ego setSpeed: 0)
		(self setScript: speedTest)
	)
	
	(method (doit &tmp temp0)
		(super doit:)
		(= temp0 0)
		(while (< temp0 500)
			(++ temp0)
		)
	)
)

(instance fred of Actor
	(properties
		view vTestBlock
	)
)

(instance speedTest of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(fred
					setLoop: 0
					illegalBits: 0
					posn: 0 0
					setStep: 1 1
					setCycle: Forward
					init:
				)
				(= cycles 1)
			)
			(1
				(= endTime (GetTime))
				(fred setMotion: MoveTo 320 190)
				(= cycles 50)
			)
			(2
				(= distance (- (GetTime) endTime))
				(startGame doit:)
			)
		)
	)
)

(instance startGame of Code
	(properties)
	
	(method (doit &tmp nextRoom)
		(if debugging
			(= nextRoom WHERE_TO)
			else (= nextRoom TITLE)
		)		
		(cond 
			((> distance 160) (= howFast 0))
			((> distance 150) (= howFast 1))
			((> distance 140) (= howFast 2))
			((> distance 130) (= howFast 3))
			((> distance 120) (= howFast 4))
			((> distance 110) (= howFast 5))
			((> distance 100) (= howFast 6))
			((> distance 90) (= howFast 7))
			((> distance 80) (= howFast 8))
			((> distance 70) (= howFast 9))
			((> distance 60) (= howFast 10))
			((> distance 50) (= howFast 11))
			((> distance 40) (= howFast 12))
			((> distance 30) (= howFast 13))
			((> distance 20) (= howFast 14))
			(else (= howFast 15))
		)
		(theGame
			detailLevel: (cond 
				((<= howFast 3) 1)
				((<= howFast 10) 2)
				(else 3)
			)
		)
		(ego setSpeed: 7)
		;If debugging is enabled, go to the "Where to?" dialog.
		;Otherwise, go to the title screen..
		(curRoom newRoom: nextRoom)
	)
)