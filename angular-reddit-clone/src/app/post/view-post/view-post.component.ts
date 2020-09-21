import { Component, OnInit } from '@angular/core';
import { PostModel } from '../post-model';
import { PostService } from '../post.service';
import { ActivatedRoute, Router } from '@angular/router';
import { throwError } from 'rxjs';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { CommentService } from 'src/app/comment/comment.service';
import { CommentPayload } from 'src/app/comment/comment-payload';
import { AuthService } from 'src/app/auth/shared/auth.service';

@Component({
  selector: 'app-view-post',
  templateUrl: './view-post.component.html',
  styleUrls: ['./view-post.component.css'],
})
export class ViewPostComponent implements OnInit {
  postId: number;
  post: PostModel;
  commentForm: FormGroup;
  commentPayload: CommentPayload;
  comments: CommentPayload[];
  isLoggedIn: boolean;

  constructor(
    private postService: PostService,
    private activateRoute: ActivatedRoute,
    private commentService: CommentService,
    private authService: AuthService,
    private router: Router
  ) {
    this.postId = this.activateRoute.snapshot.params.postId;
    this.commentForm = new FormGroup({
      text: new FormControl('', Validators.required),
    });
    this.commentPayload = {
      text: '',
      postId: this.postId,
    };

    this.postService.getPost(this.postId).subscribe(
      (data) => {
        this.post = data;
      },
      (error) => {
        throwError(error);
      }
    );
  }

  ngOnInit(): void {
    this.getPostBypostId();
    this.getCommentsForPost();
    this.isLoggedIn = this.authService.isLoggedIn();
  }

  postComment() {
    this.commentPayload.text = this.commentForm.get('text').value;
    this.commentService.postComment(this.commentPayload).subscribe(
      (data) => {
        this.commentForm.get('text').setValue('');
        this.getCommentsForPost();
      },
      (error) => {
        throwError(error);
      }
    );
  }

  getCommentsForPost() {
    this.commentService.getAllCommentsForPost(this.postId).subscribe(
      (data) => {
        this.comments = data;
      },
      (error) => {
        throwError(error);
      }
    );
  }

  getPostBypostId() {
    this.postService.getPost(this.postId).subscribe(
      (data) => {
        this.post = data;
      },
      (error) => {
        throwError(error);
      }
    );
  }
}
