//
//  Request.swift
//  getirhackathon
//
//  Created by Mert Aközcan on 25/03/2017.
//  Copyright © 2017 Mert Aközcan. All rights reserved.
//

import Foundation

class Request {
    
    func processRequest() {
        var request = URLRequest(url: URL(string: "http://192.168.88.63:9999/api/event/register")!)
        request.httpMethod = "POST"
        let postKey = "title=deneme4&owner=58d553e3ef54401bec32899d&desc=Cayicelim&time=2017-05-18T16:00:00.000Z&[41,078592,29,022357]"
        request.httpBody = postKey.data(using: .utf8)
        let task = URLSession.shared.dataTask(with: request) { data, response, error in
            guard let data = data, error == nil else {
                print("error=\(error)")
                return
            }
            if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode != 200 {
                print("response = \(response)")
            }
            
            let jsonWithObjectRoot = try? JSONSerialization.jsonObject(with: data, options: [])
            
            if let dictionary = jsonWithObjectRoot as? [String: Any] {
                if let nestedDictionary = dictionary["savedEvent"] as? [String: Any] {
                    
                }
            }
        }
    }
    
    
    
}
