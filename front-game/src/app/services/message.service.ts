import { EndTurnModel } from '../models/endTurnModel';
import { Injectable } from '@angular/core';
import * as SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';
import { of } from 'rxjs';
import { environment } from 'src/environments/environment';


@Injectable({
  providedIn: 'root',
})
export class MessageService {
  socket: any;
  stompClient: any;
  actualUserIdTurn: any;

  // endTurns: EndTurnModel[] = [];

  connect(matchId: any, user: any) {
    // this.socket = new SockJS(environment.BASE_URL + 'socket');
    this.socket = new SockJS('https://localhost:8081/socket');
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

      this.actualUserIdTurn = (JSON.parse(turnResponse.body)).username //TODO: CON LO DE ALE, CAMBIAR A USERNAME
      console.log("El usuario que continúa es: " + this.actualUserIdTurn)

      if (this.actualUserIdTurn == user) {
        console.log("Es tu turno!");
      }else {
        console.log("No podés jugar. No es tu turno!");
      }

      console.log("La respuesta a la suscripción dió...")
      console.log(turnResponse);
    });
  }

  private onDisconnectCallback() {
    console.log('Websocket connection failed');
  }

  public getActualUserIdTurn(){
    return of(this.actualUserIdTurn);
  }
}
