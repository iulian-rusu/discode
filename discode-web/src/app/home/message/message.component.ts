import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Member } from '../models/member.model';

@Component({
  selector: 'app-message',
  templateUrl: './message.component.html',
  styleUrls: ['./message.component.scss']
})
export class MessageComponent implements OnInit, OnDestroy {

  @Input() public member: Member | undefined;
  @Input() public message: string | undefined;
  @Input() public time: string | undefined;
  
  constructor() { }

  ngOnDestroy(): void {
    throw new Error('Method not implemented.');
  }

  ngOnInit(): void {
  }

}
