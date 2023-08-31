import { Injectable } from '@angular/core';
import {Stomp} from "@stomp/stompjs";
// @ts-ignore
import SockJS from 'sockjs-client';

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {
  ws!: any;
  name: string = '';
  greetings: string[] = [];
  connected: boolean = false;

  constructor() { }

  connect() {
    // const socket = new WebSocket('ws://localhost:8080/greeting');
    const socket = new SockJS('http://localhost:8080/greeting');
    this.ws = Stomp.over(socket);

    this.ws.connect({}, (frame: any) => {
      this.ws.subscribe('/user/queue/errors', (message: any) => {
        alert("Error: " + message.body);
      });
      this.ws.subscribe('/user/queue/reply', (message: any) => {
        this.showGreeting(message.body);
      });
      this.ws.subscribe('/topic/reply', (message: any) => {
        this.showGreeting(message.body);
      });
      this.ws.subscribe('/topic/messages', (message: any) => {
        this.showGreeting(message.body);
      })
      this.connected = true;
    }, (err: any) => {
      alert("STOMP ERROR: " + err)
    });
  }

  disconnect() {
    if (this.ws !== null) this.ws.disconnect();
    console.log("Disconnected");
    this.connected = false;
  }

  sendName() {
    console.log('name', this.name);
    this.ws.send("/app/message", {}, JSON.stringify({ name: this.name}));
  }

  sendToAll() {
    this.ws.send("/app/messages", {}, JSON.stringify({ name: this.name}));
  }

  showGreeting(message: string) {
    this.greetings.push(message);
  }
}
