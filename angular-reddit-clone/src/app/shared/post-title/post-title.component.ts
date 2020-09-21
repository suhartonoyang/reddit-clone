import { Component, OnInit, Input } from '@angular/core';
import { PostModel } from '../../post/post-model';
import { faComments } from '@fortawesome/free-solid-svg-icons';
import { PostService } from '../../post/post.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-post-title',
  templateUrl: './post-title.component.html',
  styleUrls: ['./post-title.component.css'],
})
export class PostTitleComponent implements OnInit {
  faComments = faComments;
  @Input() posts: PostModel[];

  constructor(private router: Router) {}

  ngOnInit(): void {}

  goToPost(postId: number) {
    this.router.navigateByUrl('/view-post/' + postId);
  }
}
