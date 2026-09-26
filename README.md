# ChargeVibe

A customizable floating overlay that displays an animated icon, emoji, or custom image on screen while your Android device is charging. Runs as a foreground service, reacts live to charging state and battery level, and survives reboots.

## What it does

- Floating overlay (icon / emoji / custom image) shown while charging
- Customizable size, transparency, position, mask color, animation speed
- Reacts to battery level (e.g. blinks on low battery)
- Persists in background; auto-restarts after reboot
- Settings apply instantly, no restart needed

## Read this first: what it can't do

**ChargeVibe cannot hide or replace the native Android status bar battery icon — no Play Store app can.** The status bar is rendered by `SystemUI` at the highest compositor layer, an OS-level restriction that only root, a custom ROM, or OEM tools (Samsung Good Lock, etc.) can bypass. ChargeVibe draws its overlay *below* that layer, positioned as close as possible — the native icon will always still be visible next to it.

This is a fun, animated charging visual layered on top of stock Android UI, not a replacement for the system battery indicator.

## Permissions

| Permission | Why |
|---|---|
| `SYSTEM_ALERT_WINDOW` | Draw the overlay |
| `FOREGROUND_SERVICE` / `_SPECIAL_USE` | Keep it alive in background |
| `POST_NOTIFICATIONS` | Required for foreground service |
| `RECEIVE_BOOT_COMPLETED` | Restart after reboot |
| `REQUEST_IGNORE_BATTERY_OPTIMIZATIONS` | Recommended — avoids OEM battery killers |
| `READ_MEDIA_IMAGES` | Optional — custom image picker |

No network access. Nothing leaves the device.

## Known limitations

- Native battery icon can't be hidden (permanent, OS-level)
- Alignment with the status bar icon varies by device/OEM
- Xiaomi/Oppo/Vivo/Huawei battery managers may kill the service unless the user grants the optimization exemption

## Building

```bash
git clone https://github.com/<your-username>/ChargeVibe.git
cd ChargeVibe
./gradlew assembleDebug
```

Requires `minSdk 26`, `targetSdk 34`.

## Play Store note

`SYSTEM_ALERT_WINDOW` needs a permissions declaration form; `FOREGROUND_SERVICE_SPECIAL_USE` needs a justification `<property>` in the manifest. Both required for review approval.

## License

*(Add your chosen license here.)*
