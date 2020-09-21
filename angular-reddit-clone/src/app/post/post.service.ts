import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PostModel } from './post-model';
import { CreatePostPayload } from './create-post/create-post-payload';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  constructor(private httpClient: HttpClient) {}

  getAllPosts(): Observable<Array<PostModel>> {
    return this.httpClient.get<Array<PostModel>>('api/posts');
  }

  createPost(
    createPostPayload: CreatePostPayload
  ): Observable<CreatePostPayload> {
    return this.httpClient.post<CreatePostPayload>(
      'api/posts',
      createPostPayload
    );
  }

  getPost(postId: number): Observable<PostModel> {
    return this.httpClient.get<PostModel>('api/posts/' + postId);
  }

  getAllPostsByUser(username: string): Observable<PostModel[]> {
    return this.httpClient.get<PostModel[]>(
      'api/posts/by-username/' + username
    );
  }
}
