import { Component, OnInit } from '@angular/core';
import { PostModel } from '../post/post-model';
import { PostService } from '../post/post.service';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  posts: Array<PostModel> = [];

  constructor(private postService: PostService, private router: Router) {
    this.postService.getAllPosts().subscribe((post) => {
      this.posts = post;
    });
  }

  ngOnInit(): void {}
}
