package com.bounswe2017.group10.atlas.httpbody;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by talha302 on 12/2/2017.
 */

public class LocationRequest {

    @SerializedName("Longitude")
    @Expose
    private String longitude;

    @SerializedName("Latitude")
    @Expose
    private String latitude;

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
