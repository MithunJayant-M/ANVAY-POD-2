import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
export interface ChatMessage {
  role: 'user' | 'assistant';
  content: string;
  timestamp: Date;
}

interface ChatApiRequest {
  message: string;
  userId?: number;
  institutionId?: number;
}

interface ChatApiResponse {
  reply: string;
}

@Injectable({ providedIn: 'root' })
export class ChatService {
  constructor(private http: HttpClient) {}

  sendMessage(message: string, userId?: number, institutionId?: number): Observable<ChatApiResponse> {
    const body: ChatApiRequest = { message, userId, institutionId };
    return this.http.post<ChatApiResponse>(`${environment.apiBaseUrl}/api/chat`, body);
  }
}
