import { VoteType } from './vote-enum';

export class VotePayload {
  voteType: VoteType;
  postId: number;
}
