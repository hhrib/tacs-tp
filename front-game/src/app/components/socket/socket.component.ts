import { EndTurnModel } from '../../models/endTurnModel';
import { MessageService } from './../../services/message.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-socket',
  templateUrl: './socket.component.html',
  styleUrls: ['./socket.component.css']
})
export class SocketComponent implements OnInit, OnDestroy {
  title = 'websocket-client';

  form: FormGroup;
  endTurns: EndTurnModel[];

  constructor(fb: FormBuilder, private service: MessageService) {
    this.form = fb.group({
      sender: ['', Validators.required],
      message: ['', Validators.required]
    });

    // this.endTurns = service.endTurns;
  }

  ngOnInit() {
  }

  ngOnDestroy() {
    this.service.disconnect();
  }

  send() {
    this.service.sendMessage(this.form.value)
    this.form.reset();
  }
}
