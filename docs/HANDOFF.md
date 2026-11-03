# Handoff

Current state only. **Rewrite this file at the end of every session**: carry forward what is
open, delete what is resolved, never append history (git and `docs/decisions/` hold it).

**State (2026-11-03, ~1:30 am):** M1 *First light* built in one session (T1.1–T1.6, T1.9) on
the owner's Pixel 11 Pro XL, with the owner trying it live and asking for changes as it went:
erase by word with a scratch, fade and closing gap (replacing the whole-note wipe), a *Swipe to
erase* switch, Normal and Handwriting fonts (Patrick Hand, after Caveat was too hard to read),
the widget's ink drawn by the app (a launcher ignores app fonts) and its lines given in each
update (it wasn't refreshing), and blank lines at the end clearing themselves. Public on GitHub
under MIT (T1.7); 0.1.0 on a GitHub release with a signed APK. Debug builds are signed with the
release key too, so test builds and releases replace each other on the Pixel. Lint clean;
release APK 2.2 MB; the signed release ran on the Pixel (opens in 0.13 s).

## Next
1. The owner tries it (`VERIFY.md` *V-M1*, T1.8), mostly the scratch's feel and the widget.
   The owner's widget went with the switch to the release key (one uninstall; the note and
   settings were put back): it needs adding again, then check it refreshes on leaving the app.
2. README screenshots, when the Pixel is free (each paper, the sheet, the widget).

## Waiting on the owner
- Back up the release key with its passwords (`docs/local.md` says where); lose it and no
  update can install over 0.1.0.
- As built, to confirm: the name *Daily Note* (D1.1); "dot matrix" read as dot grid, and the
  two yellows told apart (D1.2); the *Undo* pill I added (D1.5); Patrick Hand (D1.8).

## Uncommitted
Nothing.

## Watch out (this week only)
- The Pixel is the owner's own phone and they use it while the session works: check
  `dumpsys activity activities | grep topResumedActivity` before launching or installing, and
  say so in chat when it is locked (the owner asked to be pinged).
