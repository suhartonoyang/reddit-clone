import { Component, OnInit, Input } from '@angular/core';
import { PostModel } from '../../post/post-model';
import { faArrowUp, faArrowDown } from '@fortawesome/free-solid-svg-icons';
import { VotePayload } from '../vote-payload';
import { VoteService } from '../vote.service';
import { AuthService } from 'src/app/auth/shared/auth.service';
import { PostService } from 'src/app/post/post.service';
import { ToastrService } from 'ngx-toastr';
import { VoteType } from '../vote-enum';
import { error } from 'protractor';
import { throwError } from 'rxjs';

@Component({
  selector: 'app-vote-button',
  templateUrl: './vote-button.component.html',
  styleUrls: ['./vote-button.component.css'],
})
export class VoteButtonComponent implements OnInit {
  @Input() post: PostModel;
  votePayload: VotePayload;
  faArrowUp = faArrowUp;
  faArrowDown = faArrowDown;
  downvoteColor: string;
  upvoteColor: string;

  constructor(
    private voteService: VoteService,
    private authService: AuthService,
    private postService: PostService,
    private toastr: ToastrService
  ) {
    this.votePayload = {
      voteType: undefined,
      postId: undefined,
    };
  }

  ngOnInit(): void {}

  upvotePost() {
    this.votePayload.voteType = VoteType.UPVOTE;
    this.vote();
  }

  downvotePost() {
    this.votePayload.voteType = VoteType.DOWNVOTE;
    this.vote();
  }

  private vote() {
    this.votePayload.postId = this.post.id;
    this.voteService.vote(this.votePayload).subscribe(
      () => {
        this.updatePostAfterVote();
      },
      (error) => {
        this.toastr.error(error.error.message);
        throwError(error);
      }
    );
  }

  private updatePostAfterVote() {
    this.postService.getPost(this.post.id).subscribe((post) => {
      this.post = post;
    });
  }
}
