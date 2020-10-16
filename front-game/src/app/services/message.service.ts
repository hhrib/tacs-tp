import { EndTurnModel } from '../models/endTurnModel';
import { Injectable } from '@angular/core';
import * as SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';


@Injectable({
  providedIn: 'root',
})
export class MessageService {
  socket: any;
  stompClient: any;

  endTurns: EndTurnModel[] = [];

  connect(matchId: any, user: any) {
    this.socket = new SockJS('http://localhost:8080/socket');
    this.stompClient = Stomp.over(this.socket);
    this.stompClient.connect(
      {},
      () => this.onConnectCallback(matchId, user),
      () => this.onDisconnectCallback()
    );
  }

  disconnect() {
    this.stompClient.disconnect();
  }

  sendMessage(message: any) {
    console.log("Message crudo: " + message);
    console.log("Message Json: " + JSON.stringify(message));
    this.stompClient.send('/app/send', {}, JSON.stringify(message));
  }
  private onConnectCallback(matchId: any, user: any) {
    this.stompClient.subscribe(`/topic/${matchId}/turn_end`, (turnResponse) => {
      // if (frame.body) {
      //   let chat = JSON.parse(frame.body);
      //   this.chats.push(new EndTurnModel(chat.sender, chat.message));
      //   console.log(this.chats);
      // }

      let actualUserIdTurn = (JSON.parse(turnResponse.body)).userId
      console.log("El usuario que continúa es: " + actualUserIdTurn)

      if (actualUserIdTurn == user) {
        console.log("Es tu turno fiera!");
      }else {
        console.log("No podés jugar. No es tu turno!");
      }

      console.log("La respuesta a la suscripción dió...")
      console.log(turnResponse);
      /* if (frame.body) {
        let endTurn = JSON.parse(frame.body);
        this.endTurns.push(new EndTurnModel(endTurn.userId));
        console.log(this.endTurns);
      } */
    });
  }

  private onDisconnectCallback() {
    console.log('Websocket connection failed');
  }
}
