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
    
    var summaryWindow: UIView!
    
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
        
        events.append(Event(latitude: 41.00, longitude: 29.00, eventTitle: "Deneme 1", description: "Desc 1", dateAndTime: "01.01.2017"))
        events.append(Event(latitude: 41.10, longitude: 29.10, eventTitle: "Deneme 2", description: "Desc 2", dateAndTime: "01.02.2017"))
        
        pinCurrentEvents()
    }
    
    func showEventDetail(_ sender: UITapGestureRecognizer) {
        performSegue(withIdentifier: "Show Event Detail From Summary", sender: self)
    }
    
    func pinCurrentEvents() {
        for event in events {
            let marker = GMSMarker()
            marker.position = CLLocationCoordinate2D(latitude: event.position.latitude, longitude: event.position.longitude)
            marker.title = event.title
            marker.snippet = event.description + "||" + event.dateAndTime
            marker.tracksViewChanges = true
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
        summaryWindow = UIView(frame: CGRect(x: 0, y: view.bounds.height - 100, width: view.bounds.width, height: 100))
        summaryWindow.backgroundColor = .white
        view.addSubview(summaryWindow)
        let tapGestureRec = UITapGestureRecognizer(target: self, action: #selector (self.showEventDetail(_:)))
        summaryWindow.addGestureRecognizer(tapGestureRec)
        let controller = storyboard?.instantiateViewController(withIdentifier: "Event Detail Controller")
        if let eventDetailTableViewController = controller as? EventDetailTableViewController {
            var data = marker.snippet!.components(separatedBy: "||")
            eventDetailTableViewController.event = Event(latitude: marker.position.latitude, longitude: marker.position.longitude, eventTitle: marker.title!, description: data[0],dateAndTime: data[1])
        }
        return true
    }
    
    func mapView(_ mapView: GMSMapView, didTapAt coordinate: CLLocationCoordinate2D) {
        let lastSubview = view.subviews.last
        lastSubview?.removeFromSuperview()
    }
    
    func mapViewDidFinishTileRendering(_ mapView: GMSMapView) {
        print("djfnelwkf")
    }
    
}
