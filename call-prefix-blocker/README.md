# Call Prefix Blocker — Android MVP

A minimal Android app that blocks incoming calls when the normalized phone
number starts with a prefix configured by the user.

## Example

If you add:

    140

the app treats these as matches:

    1401234567
    +91 1401234567
    911401234567

The app removes formatting characters and strips the Indian country code `91`
from numbers longer than 10 digits before prefix matching.

## MVP features

- Add blocked prefixes
- Delete blocked prefixes
- Store prefixes locally using SharedPreferences
- Request Android's Call Screening role
- Block/reject matching incoming calls
- No account, backend, Firebase, or cloud database

## Build without Android Studio

1. Upload/push this project to a GitHub repository.
2. Open the repository's **Actions** tab.
3. Run **Build Android APK** (or push to the `main` branch).
4. Open the completed workflow run.
5. Download the `call-prefix-blocker-debug` artifact.
6. Extract it and install `app-debug.apk` on your Android phone.

You may need to allow installation from the browser/files app used to open the APK.

## First use

1. Open **Call Prefix Blocker**.
2. Tap **Enable Call Screening**.
3. Approve the Android system prompt.
4. Add a prefix, e.g. `140`.
5. Keep the app selected as your device's call screening app.

## Important behavior

Android's CallScreeningService normally receives calls from numbers that are
not in the user's contacts. Calls with hidden/restricted/unavailable caller ID
also may not be provided to the screening service.

This MVP intentionally does not request READ_CONTACTS.
