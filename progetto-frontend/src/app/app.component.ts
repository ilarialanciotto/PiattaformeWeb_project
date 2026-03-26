import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: false
})

export class AppComponent implements OnInit{
  title = 'My frontend project';

  ngOnInit(): void {
  }
}
