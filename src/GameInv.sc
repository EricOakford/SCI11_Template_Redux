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
(use BordWind)
(use Print)
(use IconBar)
(use Invent)
(use System)

(public
	GameInv 0
	invWin 1
	resetInv 2
)

(local
	theItem
	theOwner
)

(define ITEMS_PER_PAGE 10)

(instance GameInv of Inventory
	;This is the game-specific inventory	
	(method (init)
		((= inventory self)
			window: invWin
			helpIconItem: invHelp
			selectIcon: invSelect
			okButton: ok
			add:
			;add inventory items here
				(Money setCursor: vInvItems lInvCursors iMoney yourself:)
			add:
			;add icons here
				invLook
				invHand
				invSelect
				invMore
				invHelp
				ok
			eachElementDo: #modNum GAME_INV
			eachElementDo: #init
			state: NOCLICKHELP
		)
		(ego get: iMoney)
	)
)

(instance invWin of InsetWindow
	(properties
		topBordHgt 28
		botBordHgt 5
	)
	
	(method (open &tmp theX node obj)
		(= theX 0)
		(= node (inventory first:))
		(while node
			(if
				(not
					((= obj (NodeValue node)) isKindOf: InvItem)
				)
				(= theX
					(+
						theX
						(CelWide (obj view?) (obj loop?) (obj cel?))
					)
				)
			)
			(= node (inventory next: node))
		)
		(super open:)
		(invLook nsLeft: (/ (- (- right left) theX) 2))
	)
)

(class GameInvItem of InvItem
	(properties
		signal IMMEDIATE
		lowlightColor 2
		cursorView 0
		cursorLoop 0
		cursorCel 0
		realOwner 0
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

	(method (ownedBy who)
		(return
			(if (== owner who)
			else
				(== realOwner who)
			)
		)
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
				(if (Message MsgSize modNum noun V_DO NULL 1)
					(= port (GetPort))
					(Print addText: noun V_DO NULL NULL 0 0 modNum init:)
					(SetPort port)
				else
					(= port (GetPort))
					(Print addText: NULL V_DO NULL 0 0 0 modNum init:)
					(SetPort port)
				)
			)
			(else 
				(if (Message MsgSize modNum noun theVerb NULL 1)
					(= port (GetPort))
					(Print addText: noun theVerb NULL 0 0 0 modNum init:)
					(SetPort port)
				else
					(= port (GetPort))
					(Print addText: NULL V_COMBINE NULL 0 0 0 modNum init:)
					(SetPort port)
				)
			)
		)
	)	
)

(instance resetInv of Code	
	(method (doit who &tmp i what pageNum numItems)
		(= numItems 0)
		(= theOwner who)
		(= pageNum 0)
		(for ((= i 0)) (< i iLastInvItem) ((++ i))
			(if
				(or
					(== ((= what (inventory at: i)) owner?) theOwner)
					(== (what realOwner?) theOwner)
				)
				(++ numItems)
				(what realOwner: theOwner owner: 0)
				(if (<= (++ pageNum) ITEMS_PER_PAGE)
					(what owner: theOwner)
					(= theItem i)
				)
			)
		)
		(if (<= numItems ITEMS_PER_PAGE)
			(invMore loop: lInvMoreDisabled)
		else
			(invMore loop: lInvMore)
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
	
	(method (init)
		(= cursor lookCursor)
		(super init:)
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
	
	(method (init)
		(= cursor doCursor)
		(super init:)
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

(instance invMore of GameIconItem
	(properties
		view vInvIcons
		loop lInvMore
		cel 0
		cursor ARROW_CURSOR
		signal (| RELVERIFY IMMEDIATE)
		noun N_MORE
		helpVerb V_HELP
	)
	
	(method (select &tmp i what obj pageNum)
		(return
			(if (and (super select: &rest) (== loop lInvMore))
				(for ((= i 0)) (<= i theItem) ((++ i))
					(if (== ((= obj (inventory at: i)) owner?) theOwner)
						(obj realOwner: theOwner owner: 0)
					)
				)
				(= pageNum 0)
				(= what theItem)
				(while (< i iLastInvItem)
					(if
						(and
							(==
								((= obj (inventory at: i)) realOwner?)
								theOwner
							)
							(<= (++ pageNum) ITEMS_PER_PAGE)
						)
						(obj owner: theOwner)
						(= theItem i)
					)
					(++ i)
				)
				(if (== what theItem)
					(resetInv doit: theOwner)
				)
				(inventory hide: highlightedIcon: 0 show: theOwner)
				(return FALSE)
			else
				FALSE
			)
		)
	)
)

(instance lookCursor of Cursor
	(properties
		view vIconBar
		loop lLookIcon
		cel 2
	)
)

(instance doCursor of Cursor
	(properties
		view vIconBar
		loop lDoIcon
		cel 2
	)
)

(instance helpCursor of Cursor
	(properties
		view vIconBar
		loop lHelpIcon
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