import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { SubredditModel } from './subreddit-model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class SubredditService {
  constructor(private httpClient: HttpClient) {}

  getAllSubreddits(): Observable<Array<SubredditModel>> {
    return this.httpClient.get<Array<SubredditModel>>('api/subreddit');
  }

  createSubreddit(subredditModel: SubredditModel): Observable<SubredditModel> {
    return this.httpClient.post<SubredditModel>(
      'api/subreddit',
      subredditModel
    );
  }
}
