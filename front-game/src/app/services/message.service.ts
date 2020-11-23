import { EndTurnModel } from '../models/endTurnModel';
import { Injectable } from '@angular/core';
import * as SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';
import { of } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Observable } from 'ol';
import { MatDialog } from '@angular/material/dialog';


@Injectable({
  providedIn: 'root',
})
export class MessageService {
  socket: any;
  stompClient: any;
  actualUserIdTurn: any;
  defeatedPlayerNotification: string
  winnerPlayerNotification: string

  // endTurns: EndTurnModel[] = [];

  connect(matchId: any, user: any) {
    this.socket = new SockJS(environment.BASE_URL + 'socket');
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
    //Suscripción a sistema de turnos
    this.stompClient.subscribe(`/topic/${matchId}/turn_end`, (turnResponse) => {
      
      this.actualUserIdTurn = (JSON.parse(turnResponse.body)).username
      console.log("El usuario que continúa es: " + this.actualUserIdTurn)

      if (this.actualUserIdTurn == user) {
        console.log("Es tu turno!");
      }else {
        console.log("No podés jugar. No es tu turno!");
      }

      console.log("La respuesta a la suscripción dió...")
      console.log(turnResponse);
    });

    //Suscripción a derrotas de jugadores
    this.stompClient.subscribe(`/topic/${matchId}/defeated_player`, (defeatedResponse) => {
      console.log("COMPARACIÓN CON NADA");
      
      console.log(this.defeatedPlayerNotification == "");
      this.defeatedPlayerNotification = (JSON.parse(defeatedResponse.body)).username;
      console.log(this.defeatedPlayerNotification)
    });

    //Suscripción a ganador del partido
    this.stompClient.subscribe(`/topic/${matchId}/winner_player`, (winnerResponse) => {
      this.winnerPlayerNotification = (JSON.parse(winnerResponse.body)).username;
      this.defeatedPlayerNotification = ""
      console.log(this.winnerPlayerNotification)
    });
  }

  private onDisconnectCallback() {
    console.log('Websocket connection failed');
  }

  public getActualUserIdTurn(){
    return of(this.actualUserIdTurn);
  }
}
