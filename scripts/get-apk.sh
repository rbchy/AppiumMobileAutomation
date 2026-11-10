#!/usr/bin/env bash
# Downloads the app under test — Sauce Labs "My Demo App" (Android) — next to pom.xml.
# Uses the latest GitHub release of saucelabs/my-demo-app-android (needs gh + GH_TOKEN in CI).
set -euo pipefail
cd "$(dirname "$0")/.."
if ! ls *.apk >/dev/null 2>&1; then
  gh release download --repo saucelabs/my-demo-app-android --pattern '*.apk' --clobber
fi
ls -la *.apk
APK=$(ls -1 *.apk | head -1)
echo "APP_PATH=$PWD/$APK" >> "${GITHUB_ENV:-/dev/null}"
echo "App under test: $APK"
