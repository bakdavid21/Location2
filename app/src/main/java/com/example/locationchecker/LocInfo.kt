package com.example.locationchecker

import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
data class LocInfo(
    var latitude: Double? = 0.0,
    var longitude: Double? = 0.0
)


