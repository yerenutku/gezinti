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
    
    var title: String
    
    var description: String
    
    var dateAndTime: String
    
    init(latitude: Double, longitude: Double, eventTitle: String, description: String, dateAndTime: String) {
        position = CLLocationCoordinate2D(latitude: latitude, longitude: longitude)
        self.title = eventTitle
        self.description = description
        self.dateAndTime = dateAndTime
    }
    
}
