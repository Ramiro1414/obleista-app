package com.example.obleista_app.backend.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class CoordsService {

    private final FusedLocationProviderClient fusedLocationClient;

    // Constructor modificado
    public CoordsService(Context context, FusedLocationProviderClient fusedLocationClient) {
        this.fusedLocationClient = fusedLocationClient != null ? fusedLocationClient : LocationServices.getFusedLocationProviderClient(context);
    }

    @SuppressLint("MissingPermission")
    public void getLastLocation(OnSuccessListener<Location> listener) {
        fusedLocationClient.getLastLocation().addOnSuccessListener(listener);
    }
}