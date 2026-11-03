# Gotchas

### Line height in sp drifts with a large system font
Compose resolves an `sp` line height, under Android 14+'s nonlinear font scaling, as the
font size × (line sp / font sp): not the pixels asked for. At 130% the lines came out ~20%
taller than the paper's rules. Give the line height in `em` of the actual font size (D1.3).

### Android 16+ inlines a widget's list
`setRemoteAdapter(Intent)` lists are fetched once by the system and packed into the widget's
own bundle, bitmaps and all, bound from the context that called `updateAppWidget`. One bitmap
per line made 12.4 MB; share one bitmap object across rows (it is stored once) and update from
the application context, or the activity leaks the service connection (D1.6).

### A widget can't use the app's fonts
RemoteViews has no typeface setter, and the launcher ignores `android:fontFamily="@font/..."`
in a widget's layout (the text came out in the system sans). Draw the ink in the app (D1.6).

### Force-stop kills the widget's taps
`am force-stop` cancels every PendingIntent the app made, so the widget's lines don't respond
until the app runs again (opening it refreshes the widget). Test cold starts with `am kill`.

### Wireless adb on the Pixel
A new PC must pair first: Wireless debugging → *Pair device with pairing code* shows a
`_adb-tls-pairing` service in `adb mdns services`; `adb pair <ip:port> <code>`, then
`adb connect` to the `_adb-tls-connect` port. The phone locks quickly; screenshots of a locked
screen come back black.

### `cmd appwidget` is not there
`adb shell cmd appwidget …` says "No shell command implementation"; the old
`adb shell appwidget grantbind --package <pkg> --user 0` works.

### Bash: backslashes and long commands
The Bash tool collapses `\\` to `\` (also inside quoted Python), and commands over ~9 kB fail
to parse: edit files with the Edit tool or one heredoc per call.
