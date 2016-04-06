# 1.3.4 April 6, 2016

* Add GeoJSON support
* Add bitmap snapshotting support

# 1.3.3 March 28, 2016

* Web map bug fixes (PR #76)
* Use `AirMapMarker` in marker click listeners and info window creator. (PR #67)
* Added some `toBuilder()` to `AirMapMarker` and `bitmapDescriptor(BitmapDescriptor)` to `AirMapMarker.Builder` (PR #84)

# 1.3.2 February 19, 2016

* Added support for drawing GeoJson layers (PR #80)
* Disable file access on Webview and prevent content success (PR #72)

# 1.3.1 January 6, 2016

* Added support for drawing polygons (PR #64)
* Fixed runtime permission for Android M (PR #65)
* Fixed drawing markers for China map type (PR #68)

# 1.3.0 September 28, 2015

* Android M support
* Mapbox support (PR #49)
* Fix google map info window size (PR #53)

# 1.2.0

* Updates project dependencies (compile sdk version, build tools, appcompat and play services) to latest
* Adds snippet field to AirMapMarker (PR #39)
* Add geolocation support for tracking user location (PR #45)

# 1.1.5

* Add the ability to enable/disable the map toolbar (#37)
* Updates appcompat GMS dependencies. Removes ic_launcher icon from library code (#38)

# 1.1.4

* Fix getScreenLocation for web maps

# 1.1.3

* Remove Amazon AAR temporarily until we make it an optional dependency

# 1.1.2

* Adds the ability to retrieve the screen location from a Lat/Lng
* You can now set a Bitmap icon in the `setIcon()`, currently only supported by Native Google Maps.

# 1.1.1

* Initial open source release
