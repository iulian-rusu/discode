import { Component, Input, OnInit } from '@angular/core';
import { Member } from '../models/member.model';

@Component({
  selector: 'app-chat-room',
  templateUrl: './chat-room.component.html',
  styleUrls: ['./chat-room.component.scss']
})
export class ChatRoomComponent implements OnInit {

  @Input() public chatName: string | undefined;
  @Input() public chatMembers: Member[] | undefined;

  constructor() { 
    console.log("ctor");
  }

  ngOnInit(): void {
    console.log("init");
  }

}
