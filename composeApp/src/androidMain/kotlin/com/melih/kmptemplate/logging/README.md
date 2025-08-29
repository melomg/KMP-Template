# Android Logging Configuration

This package provides a solution to the issue where `android.util.Log.isLoggable(tag, Log.DEBUG)` returns `false` by default, even in debug builds.

## Problem

In Android, the `Log.isLoggable(tag, level)` method checks if a specific tag is enabled for logging at a given level. By default, only `INFO` level and above are enabled, which means `DEBUG` and `VERBOSE` logs are not shown.

This affects not only direct Android logging but also libraries like `kotlin-logging` that use Android's logging system under the hood.

## Solution

The `AndroidLogHelper` class provides a solution by:

1. Setting system properties for specific tags to enable debug logging
2. Verifying if the configuration was successful
3. Providing utility methods to check logging status

### Usage

```kotlin
// In your Application class
override fun onCreate() {
    super.onCreate()
    
    // Configure logging
    AndroidLogHelper.configureLogging(this)
    
    // Now debug logs should work
    Log.d("YourTag", "This debug message should appear")
    
    // Check if a tag is loggable
    val isLoggable = AndroidLogHelper.isLoggable("YourTag", Log.DEBUG)
}
```

### Adding More Tags

To enable debug logging for additional tags, add them to the `DEBUG_TAGS` list in `AndroidLogHelper`:

```kotlin
private val DEBUG_TAGS = listOf(
    "YourTag",
    "AnotherTag",
    // Add more tags here
)
```

## Alternative Methods

For production builds or when system properties cannot be modified, you can enable debug logging using ADB:

```bash
adb shell setprop log.tag.YourTag DEBUG
```

This command needs to be run each time the device is restarted.

## Notes

- This solution works best for debug builds
- On production builds, changing system properties may require root access
- The configuration is applied at runtime and does not persist across app restarts
- For library developers, consider providing a way for apps to configure logging levels