/* tslint:disable */
/* eslint-disable */
import { FeedbackResponse } from '../models/feedback-response';
export interface PageResponseFeedbackResponse {
  content?: Array<FeedbackResponse>;
  first?: boolean;
  last?: boolean;
  pageNumber?: number;
  pageSize?: number;
  totalElements?: number;
  totalPages?: number;
}
