//
//  ViewController.swift
//  getirhackathon
//
//  Created by Mert Aközcan on 24/03/2017.
//  Copyright © 2017 Mert Aközcan. All rights reserved.
//

import UIKit
import GoogleMaps

class MapViewController: UIViewController {
    
    var locationManager = CLLocationManager()
    var currentLocation: CLLocation?
    var mapView: GMSMapView!
    var zoomLevel: Float = 10.0
    
    var events = [Event]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        locationManager = CLLocationManager()
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        locationManager.requestAlwaysAuthorization()
        locationManager.distanceFilter = 50
        locationManager.startUpdatingLocation()
        locationManager.delegate = self
        
        let camera = GMSCameraPosition.camera(withLatitude: 0, longitude: 0, zoom: zoomLevel)
        mapView = GMSMapView.map(withFrame: view.bounds, camera: camera)
        mapView.delegate = self
        mapView.settings.myLocationButton = true
        mapView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        mapView.isMyLocationEnabled = true
        
        view.addSubview(mapView)
        mapView.isHidden = true
        
        
        let position = CLLocationCoordinate2D(latitude: 51.5, longitude: -0.127)
        let marker = GMSMarker(position: position)
        marker.title = "London"
        
        marker.tracksViewChanges = true
        marker.map = mapView
        
//        events.append(Event(latitude: 41.00, longitude: 29.00))
//        events.append(Event(latitude: 41.10, longitude: 29.10))
        
//        pinCurrentEvents()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    func pinCurrentEvents() {
        for event in events {
            let marker = GMSMarker()
            marker.position = CLLocationCoordinate2D(latitude: event.position.latitude, longitude: event.position.longitude)
            marker.map = mapView
        }
    }

}

extension MapViewController: CLLocationManagerDelegate {
    
    // Handle incoming location events.
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        let location: CLLocation = locations.last!
        
        let camera = GMSCameraPosition.camera(withLatitude: location.coordinate.latitude,
                                              longitude: location.coordinate.longitude,
                                              zoom: zoomLevel)
        if mapView.isHidden {
            mapView.isHidden = false
            mapView.camera = camera
        } else {
            mapView.animate(to: camera)
        }
    }
    
    // Handle authorization for the location manager.
    func locationManager(_ manager: CLLocationManager, didChangeAuthorization status: CLAuthorizationStatus) {
        switch status {
        case .restricted:
            print("Location access was restricted.")
        case .denied:
            print("User denied access to location.")
            // Display the map using the default location.
            mapView.isHidden = false
        case .notDetermined:
            print("Location status not determined.")
        case .authorizedAlways: fallthrough
        case .authorizedWhenInUse:
            print("Location status is OK.")
        }
    }
    
    // Handle location manager errors.
    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        locationManager.stopUpdatingLocation()
        print("Error: \(error)")
    }
}

extension MapViewController: GMSMapViewDelegate {
    func mapView(_ mapView: GMSMapView, didTap marker: GMSMarker) -> Bool {
        print("touched \(marker.title)")
        
        let infoWindow = UIView(frame: CGRect(x: 0, y: view.bounds.height - 100, width: view.bounds.width, height: 100))
        
        infoWindow.backgroundColor = .white
        view.addSubview(infoWindow)
        return true
    }
    
    func mapView(_ mapView: GMSMapView, didTap overlay: GMSOverlay) {
        print("deneme")
    }
}

