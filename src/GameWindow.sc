;;; Sierra Script 1.0 - (do not remove this comment)
;
;	GAMEWINDOW.SC
;
;	This is a custom window that Print points to.
;	Customize it any way you like.
;	If you decide not to use it, it will consume no heap.
;

(script#	GAME_WINDOW)
(include game.sh)
(use Main)
(use BordWind)
(use Window)


(class GameWindow kindof BorderWindow	
	(method (open)
		;Customize the window here
		(super open:	&rest)
	)
)
