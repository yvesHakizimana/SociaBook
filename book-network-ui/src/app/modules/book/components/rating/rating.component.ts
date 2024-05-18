import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-rating',
  templateUrl: './rating.component.html',
  styleUrl: './rating.component.scss'
})
export class RatingComponent {

  @Input()
  rating = 0;
  maxRating  = 5;

  get fullStars(){
    return Math.floor(this.rating);
  }

  get halfStar(){
    return this.rating % 1 !== 0 ;
  }

  get emptyStars(){
    return this.maxRating - Math.ceil(this.rating);
  }

}
