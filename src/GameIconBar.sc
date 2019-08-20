;;; Sierra Script 1.0 - (do not remove this comment)
;
;	GAMEICONBAR.SC
;
;	The game's icon bar is initialized here. It could have been in the game's Main script, but
;	doing it this way makes everything better organized. In fact, this was done more often for SCI32 games.
;
;

(script# GAME_ICONBAR)
(include game.sh) (include "11.shm")
(use Main)
(use IconBar)
(use System)

(public
	iconCode 0
)

(instance iconCode of Code
	(properties)
	
	(method (init)
		(= theIconBar IconBar)
		(theIconBar
			add:
			;These correspond to ICON_*** in game.sh
				iconWalk iconLook iconDo iconTalk iconCustom
				iconUseIt iconInventory iconControlPanel iconHelp
			eachElementDo: #init
			eachElementDo: #lowlightColor 5
			eachElementDo: #modNum GAME_ICONBAR
			useIconItem: iconUseIt
			helpIconItem: iconHelp
			walkIconItem: iconWalk
			state: 3072			
		)
	)
)

(instance iconWalk of IconItem
	(properties
		view vIconBar
		loop lWalkIcon
		cel 0
		type (| userEvent walkEvent)
		message V_WALK
		signal (| HIDEBAR RELVERIFY)
		maskView vIconBar
		maskLoop lDisabledIcon
		noun N_WALK
		helpVerb V_HELP
	)
	
	(method (init)
		(= cursor walkCursor)
		(super init:)
	)
	
	(method (select)
		(return
			(if (super select: &rest)
				(theIconBar hide:)
				(return TRUE)
			else
				(return FALSE)
			)
		)
	)
)

(instance iconLook of IconItem
	(properties
		view vIconBar
		loop lLookIcon
		cel 0
		message V_LOOK
		signal (| HIDEBAR RELVERIFY)
		maskView vIconBar
		maskLoop lDisabledIcon
		noun N_LOOK
		helpVerb V_HELP
	)
	
	(method (init)
		(= cursor lookCursor)
		(super init:)
	)
)

(instance iconDo of IconItem
	(properties
		view vIconBar
		loop lDoIcon
		cel 0
		message V_DO
		signal (| HIDEBAR RELVERIFY)
		maskView vIconBar
		maskLoop lDisabledIcon
		noun N_DO
		helpVerb V_HELP
	)
	
	(method (init)
		(= cursor doCursor)
		(super init:)
	)
)

(instance iconTalk of IconItem
	(properties
		view vIconBar
		loop lTalkIcon
		cel 0
		message V_TALK
		signal (| HIDEBAR RELVERIFY)
		maskView vIconBar
		maskLoop lDisabledIcon
		noun N_TALK
		helpVerb V_HELP
	)
	
	(method (init)
		(= cursor talkCursor)
		(super init:)
	)
)

(instance iconCustom of IconItem
	(properties
		view vIconBar
		loop lCustomIcon
		cel 0
		cursor ARROW_CURSOR
		message 0
		signal (| HIDEBAR RELVERIFY DISABLED)
		maskView vIconBar
		maskLoop lDisabledIcon
		noun N_CUSTOM
		helpVerb V_HELP
	)
	
	(method (select)
		(return FALSE)
	)
)

;;;(instance iconScore of IconItem
;;;	(properties
;;;		view vIconBar
;;;		loop lScoreIcon
;;;		cel 0
;;;		cursor ARROW_CURSOR
;;;		signal (| HIDEBAR RELVERIFY IMMEDIATE)
;;;		maskView vIconBar
;;;		maskLoop lScoreIcon
;;;		noun N_SCORE
;;;		helpVerb V_HELP
;;;	)
;;;	
;;;	(method (show &tmp [str 7] [temp7 4])
;;;		(super show: &rest)
;;;		(Format @str "%d/%d" score possibleScore)
;;;		(TextSize @temp7 @str scoreFont 0)
;;;		(Display @str p_color 255
;;;			p_font scoreFont
;;;			p_at (+ (- nsLeft 8) (/ (- 50 [temp7 3]) 2)) (+ nsTop 13)
;;;		)
;;;	)
;;;)

(instance iconUseIt of IconItem
	(properties
		view vIconBar
		loop lItemIcon
		cel 0
		cursor ARROW_CURSOR
		message 0
		signal (| HIDEBAR RELVERIFY)
		maskView vIconBar
		maskLoop lDisabledIcon
		maskCel 4
		noun N_CURITEM
		helpVerb V_HELP
	)
	
	(method (select param1 &tmp newEvent temp1 theIconBarCurInvIcon temp3 temp4)
		(return
			(cond 
				((& signal DISABLED) 0)
				((and argc param1 (& signal RELVERIFY))
					(if
					(= theIconBarCurInvIcon (theIconBar curInvIcon?))
						(= temp3
							(+
								(/
									(-
										(- nsRight nsLeft)
										(CelWide
											(theIconBarCurInvIcon view?)
											(- (theIconBarCurInvIcon loop?) 1)
											(theIconBarCurInvIcon cel?)
										)
									)
									2
								)
								nsLeft
							)
						)
						(= temp4
							(+
								(theIconBar y?)
								(/
									(-
										(- nsBottom nsTop)
										(CelHigh
											(theIconBarCurInvIcon view?)
											(- (theIconBarCurInvIcon loop?) 1)
											(theIconBarCurInvIcon cel?)
										)
									)
									2
								)
								nsTop
							)
						)
					)
					(DrawCel view loop (= temp1 1) nsLeft nsTop -1)
					(if
					(= theIconBarCurInvIcon (theIconBar curInvIcon?))
						(DrawCel
							(theIconBarCurInvIcon view?)
							(- (theIconBarCurInvIcon loop?) 1)
							(theIconBarCurInvIcon cel?)
							temp3
							temp4
							-1
						)
					)
					(Graph GShowBits nsTop nsLeft nsBottom nsRight 1)
					(while (!= ((= newEvent (Event new:)) type?) 2)
						(newEvent localize:)
						(cond 
							((self onMe: newEvent)
								(if (not temp1)
									(DrawCel view loop (= temp1 1) nsLeft nsTop -1)
									(if
									(= theIconBarCurInvIcon (theIconBar curInvIcon?))
										(DrawCel
											(theIconBarCurInvIcon view?)
											(- (theIconBarCurInvIcon loop?) 1)
											(theIconBarCurInvIcon cel?)
											temp3
											temp4
											-1
										)
									)
									(Graph GShowBits nsTop nsLeft nsBottom nsRight 1)
								)
							)
							(temp1
								(DrawCel view loop (= temp1 0) nsLeft nsTop -1)
								(if
								(= theIconBarCurInvIcon (theIconBar curInvIcon?))
									(DrawCel
										(theIconBarCurInvIcon view?)
										(- (theIconBarCurInvIcon loop?) 1)
										(theIconBarCurInvIcon cel?)
										temp3
										temp4
										-1
									)
								)
								(Graph GShowBits nsTop nsLeft nsBottom nsRight 1)
							)
						)
						(newEvent dispose:)
					)
					(newEvent dispose:)
					(if (== temp1 1)
						(DrawCel view loop 0 nsLeft nsTop -1)
						(if
						(= theIconBarCurInvIcon (theIconBar curInvIcon?))
							(DrawCel
								(theIconBarCurInvIcon view?)
								(- (theIconBarCurInvIcon loop?) 1)
								(theIconBarCurInvIcon cel?)
								temp3
								temp4
								-1
							)
						)
						(Graph GShowBits nsTop nsLeft nsBottom nsRight 1)
					)
					temp1
				)
				(else TRUE)
			)
		)
	)
)

(instance iconInventory of IconItem
	(properties
		view vIconBar
		loop lInvIcon
		cel 0
		cursor ARROW_CURSOR
		type $0000
		message 0
		signal (| HIDEBAR RELVERIFY IMMEDIATE)
		maskView vIconBar
		maskLoop lDisabledIcon
		noun N_INVENTORY
		helpVerb V_HELP
	)
	
	(method (select)
		(return
			(if (super select: &rest)
				(ego showInv:)
				(return TRUE)
			else
				(return FALSE)
			)
		)
	)
)

(instance iconControlPanel of IconItem
	(properties
		view vIconBar
		loop lControlIcon
		cel 0
		cursor ARROW_CURSOR
		message V_CONTROL
		signal (| HIDEBAR RELVERIFY IMMEDIATE)
		maskView vIconBar
		maskLoop lDisabledIcon
		noun N_CONTROL
		helpVerb V_HELP
	)
	
	(method (select)
		(return
			(if (super select: &rest)
				(theGame showControls:)
				(return TRUE)
			else
				(return FALSE)
			)
		)
	)
)

(instance iconHelp of IconItem
	(properties
		view vIconBar
		loop lHelpIcon
		cel 0
		cursor vHelpCursor
		type helpEvent
		message V_HELP
		signal (| RELVERIFY IMMEDIATE)
		maskView vIconBar
		maskLoop lDisabledIcon
		noun N_HELP
		helpVerb V_HELP
	)
)

(instance walkCursor of Cursor
	(properties
		view vWalkCursor
	)
)

(instance lookCursor of Cursor
	(properties
		view vLookCursor
	)
)

(instance doCursor of Cursor
	(properties
		view vDoCursor
	)
)

(instance talkCursor of Cursor
	(properties
		view vTalkCursor
	)
)