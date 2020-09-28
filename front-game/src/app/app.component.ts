import { Component } from '@angular/core';
import {WebSocketService} from "./services/web-socket.service";


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'front-game';
  public notifications = 0;

  constructor(private webSocketService: WebSocketService) {

  }
}
