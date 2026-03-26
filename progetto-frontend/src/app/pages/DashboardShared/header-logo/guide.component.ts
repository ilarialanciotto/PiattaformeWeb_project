import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-guide',
  templateUrl: './guide.component.html',
  standalone: false,
  styleUrl: './guide.component.css'
})

export class GuideComponent implements OnInit {

  @Input() guideStep: number = 0;
  @Input() role: String = "";
  @Output() guideStepChange = new EventEmitter<number>();

  messagesGUIDE = [
    'Welcome! Here you can search for available university classrooms',
    'Select a classroom to see its details',
    'Click here to book a classroom',
    'Enter feedback for the application',
    'Here you can view or cancel your bookings',
    'Here you can view and edit your information'
  ];

  messagesSTUDENT = [
    'Here you keep your bookings updated by viewing their codes ,which will be given to you when the manager accepts',
    'Here you can enter/exit the classroom and possibly enter the given code',
    'Here you can view the bookings made by the teachers for the selected classroom'
  ];

  messagesTEACHER_ADMIN = [
    'Here you can view the bookings made by students that need to be accepted',
    'By clicking on the ID you can see the details of a booking'
  ]

  messagesADMIN = [
    'Here you can give a teacher the responsibility of a classroom'
  ]

  go() {
    if (this.guideStep < this.messagesGUIDE.length - 1) {
      this.guideStep++;
    } else {
      this.guideStep = -1; // Fine guida
    }
    this.guideStepChange.emit(this.guideStep);
  }

  skipGuide() {
    this.guideStep = -1;
    this.guideStepChange.emit(this.guideStep);
  }

  ngOnInit(): void {
    if(this.role === "ROLE_STUDENT") this.messagesGUIDE= this.messagesGUIDE.concat(this.messagesSTUDENT)
    else if(this.role === "ROLE_TEACHER") this.messagesGUIDE=this.messagesGUIDE.concat(this.messagesTEACHER_ADMIN)
    else this.messagesGUIDE=this.messagesGUIDE.concat(this.messagesTEACHER_ADMIN).concat(this.messagesADMIN)
  }

}
