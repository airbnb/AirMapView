# AirMapView

[![Build Status](https://travis-ci.org/airbnb/AirMapView.svg)](https://travis-ci.org/airbnb/AirMapView)

AirMapView is a view abstraction that enables interactive maps
for devices with and without Google Play Services. AirMapView is built
to support multiple native map providers including Google Maps V2 and soon Amazon Maps V2.
If a device does not have any supported native map provider, AirMapView
will fallback to a web based map provider (currently Google Maps). AirMapView's original author is [Nick Adams](https://github.com/nwadams).

* [Features](#features)
* [Download](#download)
* [How to Use](#how-to-use)

## Features

* Google Maps V2
* Swap map providers at runtime
* Web based maps for devices without Google Play Services

![](screenshots/google_maps_v2.png)
![](screenshots/google_web_maps.png)



## Download

Grab via Gradle:

```groovy
compile 'com.airbnb.android:airmapview:1.1.1'
```


## How to Use

1. Define `AirMapView` in your layout file
    ```xml
    <com.airbnb.android.airmapview.AirMapView
        android:id="@+id/map_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
    ```

1. Initialize in code
    ```java
    mapView = (AirMapView) findViewById(R.id.map_view);
    mapView.initialize(getSupportFragmentManager());
    ```

1. Add markers/polylines
    ```java
    map.addMarker(new AirMapMarker(latLng, markerId)
            .setTitle("Airbnb HQ")
            .setIconId(R.drawable.icon_location_pin));
    ```


License
--------

    Copyright 2015 Airbnb, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


 [1]: http://airbnb.github.io/airbnb/AirMapView/
