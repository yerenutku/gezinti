//
//  Event.swift
//  getirhackathon
//
//  Created by Mert Aközcan on 24/03/2017.
//  Copyright © 2017 Mert Aközcan. All rights reserved.
//

import Foundation
import GoogleMaps

class Event {
    
    var position: CLLocationCoordinate2D
    
    init(latitude: Double, longitude: Double) {
        position = CLLocationCoordinate2D(latitude: latitude, longitude: longitude)
    }
    
}
