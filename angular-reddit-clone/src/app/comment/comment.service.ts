import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommentPayload } from './comment-payload';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CommentService {
  constructor(private httpClient: HttpClient) {}

  getAllCommentsForPost(postId: number): Observable<CommentPayload[]> {
    return this.httpClient.get<CommentPayload[]>(
      'api/comments/by-postId/' + postId
    );
  }

  getAllCommentsByUser(username: string): Observable<CommentPayload[]> {
    return this.httpClient.get<CommentPayload[]>(
      'api/comments/by-username/' + username
    );
  }

  postComment(commentPayload: CommentPayload): Observable<any> {
    return this.httpClient.post<any>('api/comments', commentPayload);
  }
}
