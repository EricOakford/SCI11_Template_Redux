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
;	 Then in the invCode init, add the inventory item to the add: call.
;
;

(script# GAME_INV)
(include game.sh) (include "6.shm")
(use Main)
(use BordWind)
(use Print)
(use IconBar)
(use Invent)
(use System)

(public
	invCode 0
	invWin 1
)

(instance invCode of Code
	;This code initializes the game's inventory
	
	(method (init)
		(= inventory Inventory)
		(inventory
			window: invWin
			helpIconItem: invHelp
			selectIcon: invSelect
			okButton: ok
			add:
			;add inventory items here
				(Money setCursor: vInvMoney 1 0 yourself:)
			add: invLook invHand invSelect invMore invHelp ok
			eachElementDo: #modNum GAME_INV
			eachElementDo: #init
			state: NOCLICKHELP
		)
	)	
)

(instance invWin of InsetWindow
	(properties
		topBordHgt 28
		botBordHgt 5
	)
	
	(method (open)
		(invLook
			nsLeft: (- (/ (- (self right?) (self left?)) 2) 86)
		)
		(super open:)
	)
)

(class GameInvItem of InvItem
	(properties
		lowlightColor 2
		cursorView 0
		cursorLoop 0
		cursorCel 0
	)
	(method (select)
		;you can set the specific cursor for an item
		(genericCursor
			view: cursorView
			loop: cursorLoop
			cel: cursorCel
		)
		(theGame setCursor: genericCursor)
		(super select: &rest)
	)
	
	(method (setCursor cursorNumber loop cel)
		(= cursorView cursorNumber)
		(= cursorLoop loop)
		(= cursorCel cel)
		(= cursor genericCursor)
	)
	
	(method (doVerb theVerb &tmp port icon)
		(if (not modNum) (= modNum curRoomNum))
		(switch theVerb
			(V_LOOK
				(if (Message MsgSize modNum noun V_LOOK NULL 1)
					(= icon (CelWide view loop cel))
					(= port (GetPort))
					(Print
						addIcon: view loop cel 0 0
						addText: noun V_LOOK NULL 1 (+ icon 4) 0 modNum
						init:
					)
					(SetPort port)
				)
			)
			(V_DO
				(if (Message MsgSize modNum noun V_DO 0 1)
					(= port (GetPort))
					(Print addText: noun V_DO 0 0 0 0 modNum init:)
					(SetPort port)
				else
					(= port (GetPort))
					(Print addText: 0 V_DO 0 0 0 0 modNum init:)
					(SetPort port)
				)
			)
			(else 
				(if (Message MsgSize modNum noun theVerb 0 1)
					(= port (GetPort))
					(Print addText: noun theVerb 0 0 0 0 modNum init:)
					(SetPort port)
				else
					(= port (GetPort))
					(Print addText: 0 V_COMBINE 0 0 0 0 modNum init:)
					(SetPort port)
				)
			)
		)
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
		cursor ARROW_CURSOR
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
		cursor vLookCursor
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
		cursor vDoCursor
		message V_DO
		noun N_HAND
		helpVerb V_HELP
	)
)

(instance invHelp of GameIconItem
	(properties
		view vInvIcons
		loop lInvHelp
		cel 0
		cursor vHelpCursor
		message V_HELP
		signal (| RELVERIFY IMMEDIATE)
		noun N_HELP
		helpVerb V_HELP
	)
)

(instance invSelect of GameIconItem
	(properties
		view vInvIcons
		loop lInvSelect
		cel 0
		cursor ARROW_CURSOR
		noun N_SELECT
		helpVerb V_HELP
	)
)

(instance invMore of GameIconItem
	(properties
		view vInvIcons
		loop lInvMore
		cel 0
		cursor ARROW_CURSOR
		maskView vIconBar
		maskLoop lDisabledIcon
		signal DISABLED	;disabled until it is completed
		noun N_MORE
		helpVerb V_HELP
	)
	
	(method (select)
		;this doesn't work yet, but I'm putting the button here for completeness
		(return FALSE)
	)
)

;Declare your inventory items below

(instance Money of GameInvItem
	(properties
		view vInvMoney
		cursor vInvMoney
		message V_MONEY
		signal IMMEDIATE
		noun N_MONEY
		name "Money"
	)
)

(instance genericCursor of Cursor)
