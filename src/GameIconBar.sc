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
(use Intrface)
(use Procs)
(use IconBar)
(use System)

(public
	iconCode 0
)

(instance iconCode of Code
	(method (doit)
		((= theIconBar IconBar)
			add:
			;These correspond to ICON_*** in game.sh
				(iconWalk cursor: walkCursor yourself:)
				(iconLook cursor: lookCursor yourself:)
				(iconDo cursor: doCursor yourself:)
				(iconTalk cursor: talkCursor yourself:)
				iconUseIt iconInventory iconControlPanel
				(iconHelp cursor: helpCursor yourself:)
				iconScore
			eachElementDo: #init
			eachElementDo: #highlightColor colBlack
			eachElementDo: #lowlightColor colGray4
			eachElementDo: #modNum GAME_ICONBAR
			curIcon: iconWalk
			useIconItem: iconUseIt
			helpIconItem: iconHelp
			walkIconItem: iconWalk
			state: (| OPENIFONME NOCLICKHELP)
		)
		(iconInventory message: (if (HaveMouse) TAB else SHIFTTAB))
	)
)

(instance iconWalk of IconItem
	(properties
		view vIcons
		loop lIconWalk
		cel 0
		type (| userEvent walkEvent)
		message V_WALK
		signal (| HIDEBAR RELVERIFY)
		maskView vIcons
		maskLoop lIconDisabled
		noun N_WALK
		helpVerb V_HELP
	)
)

(instance iconLook of IconItem
	(properties
		view vIcons
		loop lIconLook
		cel 0
		message V_LOOK
		signal (| HIDEBAR RELVERIFY)
		maskView vIcons
		maskLoop lIconDisabled
		noun N_LOOK
		helpVerb V_HELP
	)
)

(instance iconDo of IconItem
	(properties
		view vIcons
		loop lIconHand
		cel 0
		message V_DO
		signal (| HIDEBAR RELVERIFY)
		maskView vIcons
		maskLoop lIconDisabled
		noun N_DO
		helpVerb V_HELP
	)
)

(instance iconTalk of IconItem
	(properties
		view vIcons
		loop lIconTalk
		cel 0
		message V_TALK
		signal (| HIDEBAR RELVERIFY)
		maskView vIcons
		maskLoop lIconDisabled
		noun N_TALK
		helpVerb V_HELP
	)
)

(instance iconUseIt of IconItem
	(properties
		view vIcons
		loop lIconInvItem
		cel 0
		cursor ARROW_CURSOR
		message 0
		signal (| HIDEBAR RELVERIFY)
		maskView vIcons
		maskLoop lIconDisabled
		maskCel 4
		noun N_CURITEM
		helpVerb V_HELP
	)
)

(instance iconInventory of IconItem
	(properties
		view vIcons
		loop lIconInventory
		cel 0
		cursor ARROW_CURSOR
		type NULL
		message NULL
		signal (| HIDEBAR RELVERIFY IMMEDIATE)
		maskView vIcons
		maskLoop lIconDisabled
		maskCel 3
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

(instance iconScore of IconItem
	(properties
		view vIcons
		loop lIconScore
		cel 0
		cursor ARROW_CURSOR
		signal (| HIDEBAR RELVERIFY IMMEDIATE)
		maskView vIcons
		maskLoop lIconScore
		maskCel 1
		noun N_SCORE
		helpVerb V_HELP
	)
	
	(method (show &tmp [str 7] [rectPt 4] theFont)
		(super show: &rest)
		(= theFont 30)
		(Format @str "%d" score)
		(TextSize @rectPt @str theFont 0)
		(Display @str
			p_color colLRed
			p_font theFont
			p_at
				(+ nsLeft 5 (/ (- 25 [rectPt 3]) 2))
				(+ nsTop 14)
		)
	)
)

(instance iconControlPanel of IconItem
	(properties
		view vIcons
		loop lIconControls
		cel 0
		cursor ARROW_CURSOR
		message V_CONTROL
		signal (| HIDEBAR RELVERIFY IMMEDIATE)
		maskView vIcons
		maskLoop lIconDisabled
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
		view vIcons
		loop lIconHelp
		cel 0
		type helpEvent
		message V_HELP
		signal (| RELVERIFY IMMEDIATE)
		maskView vIcons
		maskLoop lIconDisabled
		maskCel 1
		noun N_HELP
		helpVerb V_HELP
	)
)

(instance walkCursor of Cursor
	(properties
		view vIcons
		loop lIconWalk
		cel 2
	)
)

(instance lookCursor of Cursor
	(properties
		view vIcons
		loop lIconLook
		cel 2
	)
)

(instance doCursor of Cursor
	(properties
		view vIcons
		loop lIconHand
		cel 2
	)
)

(instance talkCursor of Cursor
	(properties
		view vIcons
		loop lIconTalk
		cel 2
	)
)

(instance helpCursor of Cursor
	(properties
		view vIcons
		loop lIconHelp
		cel 2
	)
)