# EspressoBug
Demonstrates an android espresso bug related to TextInputLayout.setError

When an app calls `TextInputLayout.setError` after a button click in an espresso test the test code will hang indefinitely. I've tracked this down to `Tap.java` line 170 - `if (!MotionEvents.sendUp(uiController, res.down))`. The `MotionEvents.sendDown` prior to this line works fine, but `MotionEvents.sendUp` never returns.

Commenting out line 84 of MainActivity will cause the test to complete (but fail which would be expected). As written though the test hangs.
