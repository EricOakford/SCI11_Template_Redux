IF EXIST "C:\Program Files\DOSBox\dosbox.exe" GOTO StartGame64Bit
IF NOT EXIST "C:\Program Files\DOSBox\dosbox.exe" goto 32Bit

:32Bit
IF EXIST "C:\Program Files (x86)\DOSBox\dosbox.exe" GOTO StartGame32Bit
IF NOT EXIST "C:\Program Files (x86)\DOSBox\dosbox.exe" GOTO Message

:Message
echo msgbox"DOSBox not found. Please rerun the DOSBoxRun Utility.">%TEMP%\DBmsg.vbs&%TEMP%\DBmsg.vbs&del %TEMP%\DBmsg.vbs
EXIT

:StartGame32Bit
"C:\Program Files (x86)\DOSBox\dosbox.exe" -conf dosbox.conf -noconsole
EXIT

:StartGame64Bit
"C:\Program Files\DOSBox\dosbox.exe" -conf dosbox.conf -noconsole
EXIT

