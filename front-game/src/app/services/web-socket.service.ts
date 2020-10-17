import {Injectable} from "@angular/core";
import { environment } from 'src/environments/environment';

var SockJS = require("sockjs-client");
var Stomp = require("stompjs");
const SOCKET_URL = environment.BASE_URL + 'socket';

@Injectable()
export class WebSocketService {
    // Open connection with the back-end socket
    constructor() {
        this.initializeWebSocketConnection();
      }
      public stompClient;
      public msg = [];
      initializeWebSocketConnection() {
        const ws = new SockJS(SOCKET_URL);
        this.stompClient = Stomp.over(ws);
        const that = this;
        // tslint:disable-next-line:only-arrow-functions
        this.stompClient.connect({}, function(frame) {
          that.stompClient.subscribe('/statistics', (message) => {
            if (message.body) { 
              that.msg.push(message.body);
            }
          });
        });
      }
      
      sendMessage(message) {
        this.stompClient.send('/app/send/message' , {}, message);
      }
}