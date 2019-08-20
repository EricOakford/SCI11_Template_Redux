;;; Sierra Script 1.0 - (do not remove this comment)
;
;	DEBUG.SC
;	
;	This script contains the in-game debug functionality, triggered by setting the "debugging" global
;	variable to TRUE. You can press SHIFT-? to get a list of debug options.
;
;

(script# DEBUG)
(include game.sh)
(use Main)
(use Intrface)
(use Print)
(use PolyEdit)
(use DlgEdit)
(use Dialog)
(use WriteFtr)
(use Feature)
(use Window)
(use Invent)
(use User)
(use Actor)
(use System)

(public
	debugHandler 0
)

(local
	[local0 27]
	invDButton
)
(procedure (localproc_0052)
	(if (OneOf (curRoom style?) 11 12 13 14)
		(curRoom drawPic: (curRoom picture?) 100 style: 100)
	)
)

(instance debugHandler of Feature
	(properties)
	
	(method (init)
		(super init:)
		(mouseDownHandler addToFront: self)
		(keyDownHandler addToFront: self)
	)
	
	(method (dispose)
		(mouseDownHandler delete: self)
		(keyDownHandler delete: self)
		(super dispose:)
		(DisposeScript DEBUG)
	)
	
	(method (handleEvent event &tmp [str 160] temp160 newEvent i temp163 temp164 temp165 temp166 temp167 temp168 temp169 temp170 temp171 temp172 temp173 [temp174 4] temp178)
		(switch (event type?)
			(keyDown
				(event claimed: TRUE)
				(switch (event message?)
					(`@a
						(= i (cast first:))
						(while i
							(= temp164 (NodeValue i))
							(Format @str
								{class: %s\nview: %d\nloop: %d\ncel: %d\nposn: %d %d %d\nheading: %d\npri: %d\nsignal: $%x\nillBits: $%x\n}
								((temp164 -super-?) name?)
								(temp164 view?)
								(temp164 loop?)
								(temp164 cel?)
								(temp164 x?)
								(temp164 y?)
								(temp164 z?)
								(temp164 heading?)
								(temp164 priority?)
								(temp164 signal?)
								(if (temp164 isKindOf: Actor)
									(temp164 illegalBits?)
								else
									-1
								)
							)
							(if
								(not
									(Print
										addText: @str
										window: SysWindow
										addTitle: (temp164 name?)
										addIcon:
											(temp164 view?)
											(temp164 loop?)
											(temp164 cel?)
											(+ (Print x?) 80)
											(Print y?)
										init:
									)
								)
								(break)
							)
							(= i (cast next: i))
						)
					)
					(`@b
						(PolygonEditor doit:)
					)
					(`@c
						(localproc_0052)
						(Show CMAP)
					)
					(`@d
						(DialogEditor doit:)
					)
					(`@e
					)
					(`@f
					)
					(`@g
						(= str 0)
						(GetInput @str 6 {Variable No.})
						(= i (ReadNumber @str))
						(= str 0)
						(GetInput @str 6 {Value})
						(= [ego i] (ReadNumber @str))
						(= str 0)
					)
					(`@h
						(= str 0)
						(Print
							addText: {Global number:}
							addEdit: @str 6 0 12
							init:
						)
						(= i (ReadNumber @str))
						(if (IsObject [ego i])
							(Format @str
								{ Global %d: %s_}
								i
								([ego i] name?)
							)
						else
							(Format
								@str
								{ Global %d: %d_}
								i
								[ego i]
							)
						)
						(Prints @str)
					)
					(`@i
						(dInvD doit:)
					)
					(`@j						
					)
					(`@k
						(= temp160 (GetPort))
						(SetPort 0)
						(= temp171 5)
						(= temp172 16)
						(= temp167 15)
						(= temp168 80)
						(= temp170 (+ temp167 (* 34 temp171)))
						(= temp169 (+ temp168 (* 10 temp172)))
						(= temp165
							(Graph GSaveBits temp167 temp168 temp170 temp169 1)
						)
						(Graph GFillRect temp167 temp168 temp170 temp169 1 255)
						(= temp166 0)
						(while (< temp166 256)
							(Graph
								GFillRect
								(+ temp167 temp171 (* temp171 (/ temp166 8)))
								(+ temp168 temp172 (* 16 (mod temp166 8)))
								(+ temp167 temp171 temp171 (* temp171 (/ temp166 8)))
								(+ temp168 temp172 temp172 (* temp172 (mod temp166 8)))
								1
								temp166
							)
							(++ temp166)
						)
						(Graph GShowBits temp167 temp168 temp170 temp169 1)
						(repeat
							(if
								(or
									(== ((= newEvent (Event new:)) type?) mouseDown)
									(== (newEvent type?) keyDown)
								)
								(break)
							)
							(newEvent dispose:)
						)
						(newEvent dispose:)
						(Graph GRestoreBits temp165)
						(Graph GShowBits temp167 temp168 temp170 temp169 1)
						(SetPort temp160)
					)
					(`@l
						(= str 0)
						(= i (GetNumber {Flag No.}))
						(Bset i)
					)
					(`@m
						(= str 0)
						(= i (GetNumber {Flag No.}))
						(Bclr i)
					)
					(`@n
						(= str 0)
						(= i (GetNumber {Flag No.}))
						(Format @str {%d} (Btst i))
						(Prints @str)
					)
					(`@o
						((ScriptID LOGGER) doit: @sysLogPath 0)
					)
					(`@p
						(localproc_0052)
						(Show PMAP)
					)
					(`@q
						(theGame detailLevel: 1)
					)
					(`@r
						(Format @str
							DEBUG 1
							(curRoom name?)
							curRoomNum
							(curRoom curPic?)
							(curRoom style?)
							(curRoom horizon?)
							(curRoom north?)
							(curRoom south?)
							(curRoom east?)
							(curRoom west?)
							(if (IsObject (curRoom script?))
								((curRoom script?) name?)
							else
								{..none..}
							)
						)
						(Print width: 120 addText: @str init:)
						(theGame showMem:)
					)
					(`@s
					)
					(`@t
						(if modelessDialog (modelessDialog dispose:))
						(if (> (= i (GetNumber {Teleport to})) 0)
							(curRoom newRoom: i)
						)
					)
					(`@u
						(User canInput: TRUE canControl: TRUE)
						(theIconBar enable: ICON_WALK ICON_LOOK ICON_DO ICON_TALK ICON_CURITEM ICON_INVENTORY)
					)
					(`@v
						(Show VMAP)
						(addToPics doit:)
					)
					(`@w
						(WriteCode doit:)
					)
					(`@x
						(= quit TRUE)
					)
					(`@y
					)
					(`@z
						(= quit TRUE)
					)
					(`?
						(Prints
							{Debug options:______(Page 1 of 5)\n\n___A - Show cast\n___B - Polygon editor\n___C - Show control map\n___D - Dialog editor\n___E - (vacant) \n___F - (vacant)\n___G - Set global\n}
						)
						(Prints
							{Debug options:______(Page 2 of 5)\n\n___H - Show global\n___I - Get inventory item\n___J - (vacant)\n___K - Show palette\n___L - Set flag\n___M - Clear flag\n___N - Show flag\n}
						)
						(Prints
							{Debug options:______(Page 3 of 5)\n\n___O - QA Note Logger\n___P - Show priority map\n___Q - Set Detail to 1\n___R - Show room info/free memory\n___S - (vacant)\n___T - Teleport\n___U - Give HandsOn\n}
						)
						(Prints
							{Debug options:______(Page 4 of 5)\n\n___V - Show visual map\n___W - Feature writer\n___Y - (vacant)\n___X,Z - Quick quit\n}
						)
						(Prints
							{Debug options:______(Page 5 of 5)\n\n__A=Alt, C=Ctrl, L=Left shift, R=Right shift\n\n__Left click:\n____A_______Move ego\n____CL______Show ego\n____CR______Show room\n____CA______Show position\n}
						)
					)
					(else
						(event claimed: FALSE)
					)
				)
			)
			(mouseDown
				(switch (event modifiers?)
					(13 0)
					(14 0)
					(12
						(event claimed: TRUE)
						(Format @str DEBUG 2 (event x?) (event y?))
						(= temp160
							(Print
								posn: 160 10
								font: 999
								modeless: 1
								addText: @str
								init:
							)
						)
						(while (!= 2 ((= newEvent (Event new:)) type?))
							(newEvent dispose:)
						)
						(newEvent dispose:)
						(temp160 dispose:)
					)
					(5
						(event type: keyDown message: 4864)
						(self handleEvent: event)
					)
					(9 0)
					(10 0)
					(shiftRight 0)
					(shiftLeft 0)
					(ctrlDown 0)
					(altDown
						(event claimed: TRUE)
						(= temp178 (theGame setCursor: INVIS_CURSOR))
;;;						(= i
;;;							((= temp173
;;;								(if gTheNewDButtonValue else (User alterEgo?))
;;;							)
;;;								signal?
;;;							)
;;;						)
						(temp173 startUpd:)
						(while (!= 2 ((= newEvent (Event new:)) type?))
							(temp173 x: (newEvent x?) y: (- (newEvent y?) 10))
							(Animate (cast elements?) 0)
							(newEvent dispose:)
						)
						(newEvent dispose:)
						(theGame setCursor: temp178)
						(temp173 signal: i)
					)
				)
			)
		)
	)
)

(instance dInvD of Dialog
	(properties)
	
	(method (init &tmp theX theY temp2 ret newDText inventoryFirst temp6)
		(= temp2 (= theX (= theY 4)))
		(= ret 0)
		(= inventoryFirst (inventory first:))
		(while inventoryFirst
			(= temp6 (NodeValue inventoryFirst))
			(++ ret)
			(if (temp6 isKindOf: InvItem)
				(self
					add:
						((= newDText (DText new:))
							value: temp6
							text: (temp6 name?)
							nsLeft: theX
							nsTop: theY
							state: 3
							font: 999
							setSize:
							yourself:
						)
				)
			)
			(if
			(< temp2 (- (newDText nsRight?) (newDText nsLeft?)))
				(= temp2 (- (newDText nsRight?) (newDText nsLeft?)))
			)
			(if
				(>
					(= theY
						(+ theY (- (newDText nsBottom?) (newDText nsTop?)) 1)
					)
					130
				)
				(= theY 4)
				(= theX (+ theX temp2 10))
				(= temp2 0)
			)
			(= inventoryFirst (inventory next: inventoryFirst))
		)
		(= window systemWindow)
		(self setSize:)
		(= invDButton (DButton new:))
		(invDButton
			text: {Outta here!}
			font: 999
			setSize:
			moveTo: (- nsRight (+ 4 (invDButton nsRight?))) nsBottom
		)
		(self add: invDButton setSize: center:)
		(return ret)
	)
	
	(method (doit &tmp ret item)
		(self init:)
		(self open: 4 15)
		(= ret invDButton)
		(repeat
			(= ret (super doit: ret))
			(if
			(OneOf ret 0 -1 invDButton invDButton)
				(break)
			)
			(ego get: (inventory indexOf: (ret value?)))
		)
		(if (== ret invDButton)
			(= item 0)
			(while (< item (inventory size?))
				(if ((inventory at: item) isKindOf: InvItem)
					(ego get: item)
				)
				(++ item)
			)
		)
		(self eachElementDo: #dispose 1 dispose:)
	)
	
	(method (handleEvent event &tmp eventMessage eventType)
		(= eventMessage (event message?))
		(switch (= eventType (event type?))
			(keyDown
				(switch eventMessage
					(UPARROW (= eventMessage 3840))
					(DOWNARROW
						(= eventMessage 9)
					)
				)
			)
			(direction
				(switch eventMessage
					(dirN
						(= eventMessage 3840)
						(= eventType keyDown)
					)
					(dirS
						(= eventMessage 9)
						(= eventType keyDown)
					)
				)
			)
		)
		(event type: eventType message: eventMessage)
		(super handleEvent: event)
	)
)