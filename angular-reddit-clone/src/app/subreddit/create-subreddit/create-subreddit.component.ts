import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { SubredditService } from '../subreddit.service';
import { SubredditModel } from '../subreddit-model';
import { throwError } from 'rxjs';

@Component({
  selector: 'app-create-subreddit',
  templateUrl: './create-subreddit.component.html',
  styleUrls: ['./create-subreddit.component.css'],
})
export class CreateSubredditComponent implements OnInit {
  createSubredditForm: FormGroup;
  subredditModel: SubredditModel;
  title = new FormControl('');
  description = new FormControl('');

  constructor(
    private router: Router,
    private subredditService: SubredditService
  ) {
    this.subredditModel = {
      name: '',
      description: '',
    };
  }

  ngOnInit(): void {
    this.createSubredditForm = new FormGroup({
      title: new FormControl('', Validators.required),
      description: new FormControl('', Validators.required),
    });
  }

  createSubreddit() {
    this.subredditModel.name = this.createSubredditForm.get('title').value;
    this.subredditModel.description = this.createSubredditForm.get(
      'description'
    ).value;
    this.subredditService.createSubreddit(this.subredditModel).subscribe(
      (data) => {
        this.router.navigateByUrl('/list-subreddits');
        console.log('called create subreddit');
      },
      (error) => {
        throwError(error);
      }
    );
  }

  discard() {
    this.router.navigateByUrl('/');
  }
}
