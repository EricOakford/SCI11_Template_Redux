;;; Sierra Script 1.0 - (do not remove this comment)
;
;	GAMEINV.SC
;
;	GameInvItem is a game-specific subclass of InvItem.
;	Here, you can add inventory item instances, and can create custom properties.
;	
;	 An example might be::
;	
;	 	(instance Hammer of GameInvItem
;	 		(properties
;	 			view 900
;	 			loop 1
;	 			cursor 900			; Optional cursor when using this item.
;	 			message V_HAMMER	; Optional verb associated with the item.
;	 			signal IMMEDIATE
;	 			noun N_HAMMER		; noun from message resource 0
;	 		)
;	 	)
;	
;	 Then in the GameInv init, add the inventory item to the add: call.
;
;

(script# GAME_INV)
(include game.sh) (include "6.shm")
(use Main)
(use Procs)
(use IconBar)
(use Print)
(use ScrollableInventory)
(use ScrollInset)
(use BordWind)
(use Window)
(use Invent)
(use System)

(public
	GameInv 0
	invWin 1
)

(instance GameInv of ScrollableInventory
	;This is the game-specific inventory	
	(method (init)
		((= inventory self)
			add:
				;Add your inventory items here.
				;Make sure they are in the same order as the item list in GAME.SH.
				(Money setCursor: vInvItems lInvCursors iMoney yourself:)
			;add the interface buttons afterwards
			add:
				(invLook cursor: lookCursor yourself:)
				(invHand cursor: doCursor yourself:)
				(invSelect cursor: normalCursor yourself:)
				invUp
				invDown
				invHelp
				ok
		)
		(self
			state: NOCLICKHELP
			upIcon: invUp
			downIcon: invDown
			window: invWin
			helpIconItem: invHelp
			selectIcon: invSelect
			okButton: ok
			numCols: 5
			scrollAmount: 5
			dispAmount: 10
			empty: N_EMPTY
			normalHeading: GAME_INV
			eachElementDo: #highlightColor 0
			eachElementDo: #modNum GAME_INV
			eachElementDo: #init
		)
	)
)

(instance invWin of ScrollInsetWindow
	(properties
		priority -1
		topBordHgt 28
		botBordHgt 5
	)
	
	(method (open)
		(invLook
			nsLeft: (- (/ (- (self right?) (self left?)) 2) 100)
		)
		(invLook nsTop: 2)
		(super open: &rest)
	)
)

(class GameInvItem of InvItem
	(properties
		signal IMMEDIATE
		lowlightColor 2
		cursorView 0
		cursorLoop 0
		cursorCel 0
	)
	
	(method (select)
		(invCursor
			view: cursorView
			loop: cursorLoop
			cel: cursorCel
		)
		(theGame setCursor: invCursor)
		(super select: &rest)
	)
	
	(method (setCursor theView theLoop theCel)
		(= cursorView theView)
		(= cursorLoop theLoop)
		(= cursorCel theCel)
		(= cursor invCursor)
	)

	(method (doVerb theVerb &tmp port icon)
		(= port (GetPort))
		(if (not modNum) (= modNum curRoomNum))
		(switch theVerb
			(V_LOOK
				(if (Message MsgSize modNum noun V_LOOK NULL 1)
					(= icon (CelWide view loop cel))
					(Print
						addIcon: view loop cel 0 0
						addText: noun V_LOOK NULL 1 (+ icon 4) 0 modNum
						init:
					)
				)
			)
			(else 
				(if (Message MsgSize modNum noun theVerb NULL 1)
					(Print
						addText: noun theVerb NULL 0 0 0 modNum
						init:
					)
				else
					(Print
						addText: NULL V_COMBINE NULL 0 0 0 modNum
						init:
					)
				)
			)
		)
		(SetPort port)
	)	
)

(class GameIconItem of IconItem
	(properties
		lowlightColor 5
	)
	;this is to ensure that the showInv method doesn't mistake the icons
	;for inventory items
	(method (ownedBy)
		(return FALSE)
	)
)


(instance ok of GameIconItem
	(properties
		view vInvIcons
		loop lInvOK
		cel 0
		signal (| HIDEBAR RELVERIFY IMMEDIATE)
		noun N_OK
		helpVerb V_HELP
	)
)

(instance invLook of GameIconItem
	(properties
		view vInvIcons
		loop lInvLook
		cel 0
		message V_LOOK
		signal (| FIXED_POSN RELVERIFY)
		noun N_LOOK
		helpVerb V_HELP
	)
)

(instance invHand of GameIconItem
	(properties
		view vInvIcons
		loop lInvHand
		cel 0
		message V_DO
		noun N_HAND
		helpVerb V_HELP
	)
)

(instance invUp of GameIconItem
	(properties
		view vInvIcons
		loop lInvUp
		cel 0
		cursor ARROW_CURSOR
		maskView vInvIcons
		maskLoop lInvUp
		maskCel 2
		lowlightColor 5
		noun N_SCROLL_UP
		helpVerb V_HELP
	)
	
	(method (select)
		(if (super select: &rest)
			(inventory scroll: -1)
		)
		(return FALSE)
	)
)

(instance invDown of GameIconItem
	(properties
		view vInvIcons
		loop lInvDown
		cel 0
		cursor ARROW_CURSOR
		maskView vInvIcons
		maskLoop lInvDown
		maskCel 2
		lowlightColor 5
		noun N_SCROLL_DOWN
		helpVerb V_HELP
	)
	
	(method (select)
		(if (super select: &rest)
			(inventory scroll: 1)
		)
		(return FALSE)
	)
)


(instance invHelp of GameIconItem
	(properties
		view vInvIcons
		loop lInvHelp
		cel 0
		message V_HELP
		signal (| RELVERIFY IMMEDIATE)
		noun N_HELP
		helpVerb V_HELP
	)
	
	(method (init)
		(= cursor helpCursor)
		(super init:)
	)
)

(instance invSelect of GameIconItem
	(properties
		view vInvIcons
		loop lInvSelect
		cel 0
		noun N_SELECT
		helpVerb V_HELP
	)
	
	(method (init)
		(= cursor normalCursor)
		(super init:)
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

(instance helpCursor of Cursor
	(properties
		view vIcons
		loop lIconHelp
		cel 2
	)
)

(instance invCursor of Cursor
	(properties
		view vInvItems
		loop lInvCursors
	)	
)

;Declare your inventory items below
(instance Money of GameInvItem
	(properties
		view vInvItems
		loop 0
		cel iMoney	;cel and item number are the same by default
		message V_MONEY
		noun N_MONEY
		name "Money"
	)
)