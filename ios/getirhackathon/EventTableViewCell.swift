//
//  EventTableViewCell.swift
//  getirhackathon
//
//  Created by Mert Aközcan on 25/03/2017.
//  Copyright © 2017 Mert Aközcan. All rights reserved.
//

import UIKit

class EventTableViewCell: UITableViewCell {

    @IBOutlet weak var eventTitle: UILabel!
    @IBOutlet weak var eventDescription: UILabel!
    @IBOutlet weak var eventDateAndTime: UILabel!
    
    var event: Event? {
        didSet {
            updateUI()
        }
    }
    
    func updateUI() {
        eventTitle = nil
        eventDateAndTime = nil
        eventDescription = nil
        
        if let event = self.event {
            eventTitle.text = event.title
            eventDescription.text = event.description
            eventDateAndTime.text = event.dateAndTime
        }
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
