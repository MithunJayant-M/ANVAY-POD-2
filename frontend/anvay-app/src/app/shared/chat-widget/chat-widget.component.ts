import { Component, Input, OnInit, ViewChild, ElementRef, AfterViewChecked } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ChatService, ChatMessage } from '../../services/chat.service';

interface QuickReply {
  label: string;
  message: string;
  icon: string;
}

@Component({
  selector: 'app-chat-widget',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './chat-widget.component.html',
  styleUrls: ['./chat-widget.component.css']
})
export class ChatWidgetComponent implements OnInit, AfterViewChecked {
  @Input() userId?: number;
  @Input() institutionId?: number;

  @ViewChild('messagesEnd') private messagesEnd!: ElementRef;

  isOpen = false;
  inputText = '';
  isTyping = false;
  messages: ChatMessage[] = [];
  private shouldScroll = false;

  readonly quickReplies: QuickReply[] = [
    { label: 'Register for Event',  message: 'How do I register for an event?',         icon: '📋' },
    { label: 'Apply for Leader',    message: 'How do I apply for club leadership?',       icon: '🏆' },
    { label: 'Event Rules',         message: 'How do I view event rules?',                icon: '📜' },
    { label: 'Join a Club',         message: 'How do I join a club?',                     icon: '🏛️' },
    { label: 'Points & Ranking',    message: 'How does the points system work?',          icon: '🏅' },
    { label: 'Create an Event',     message: 'How do I create an event as admin?',        icon: '🎯' },
  ];

  constructor(private chatService: ChatService) {}

  ngOnInit() {
    this.messages = [{
      role: 'assistant',
      content: 'Hi! I\'m Anvay Support Bot. Use the quick-reply buttons below or type your question about events, clubs, or platform features.',
      timestamp: new Date()
    }];
  }

  ngAfterViewChecked() {
    if (this.shouldScroll) {
      this.scrollToBottom();
      this.shouldScroll = false;
    }
  }

  get showQuickReplies(): boolean {
    // Show chips after the last assistant message as long as user hasn't typed
    const last = this.messages[this.messages.length - 1];
    return last?.role === 'assistant' && !this.isTyping;
  }

  toggleChat() {
    this.isOpen = !this.isOpen;
    if (this.isOpen) {
      this.shouldScroll = true;
      setTimeout(() => this.focusInput(), 150);
    }
  }

  sendQuickReply(qr: QuickReply) {
    this.dispatch(qr.message);
  }

  sendMessage() {
    const text = this.inputText.trim();
    if (!text || this.isTyping) return;
    this.inputText = '';
    this.dispatch(text);
  }

  onKeydown(event: KeyboardEvent) {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      this.sendMessage();
    }
  }

  clearChat() {
    this.messages = [{
      role: 'assistant',
      content: 'Chat cleared. Select a topic below or type your question.',
      timestamp: new Date()
    }];
  }

  private dispatch(text: string) {
    this.messages.push({ role: 'user', content: text, timestamp: new Date() });
    this.isTyping = true;
    this.shouldScroll = true;

    this.chatService.sendMessage(text, this.userId, this.institutionId).subscribe({
      next: res => {
        this.messages.push({ role: 'assistant', content: res.reply, timestamp: new Date() });
        this.isTyping = false;
        this.shouldScroll = true;
      },
      error: () => {
        this.messages.push({
          role: 'assistant',
          content: 'Something went wrong. Please try again.',
          timestamp: new Date()
        });
        this.isTyping = false;
        this.shouldScroll = true;
      }
    });
  }

  private scrollToBottom() {
    try {
      this.messagesEnd?.nativeElement?.scrollIntoView({ behavior: 'smooth' });
    } catch {}
  }

  private focusInput() {
    document.getElementById('chat-input')?.focus();
  }
}
