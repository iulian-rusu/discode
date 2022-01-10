import { DatePipe } from '@angular/common';
import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Message } from '../models/message.model';

@Component({
  selector: 'app-message',
  templateUrl: './message.component.html',
  styleUrls: ['./message.component.scss'],
  providers:[DatePipe]
})
export class MessageComponent implements OnInit, OnDestroy {

  @Input() public message: Message | undefined;
  
  constructor( private readonly datePipe : DatePipe) { }

  ngOnDestroy(): void {}

  ngOnInit(): void {
  }

  transform(date: Date): string | null{
    let pipe = new DatePipe('en-US'); 
    let formattedDate = pipe.transform(date, 'short');
    return formattedDate;
  }

}
