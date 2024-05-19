import { Component } from '@angular/core';

@Component({
  selector: 'app-manage-book',
  templateUrl: './manage-book.component.html',
  styleUrl: './manage-book.component.scss'
})
export class ManageBookComponent {
  errorMsg: string[] = [];
  selectedBookCover: any;
  selectedPicture: string | undefined;

  onFileSelected(event: any) {
    this.selectedBookCover = event.target.files[0];
    console.log(this.selectedBookCover);
    //Fetching and reloading the page to display the selected image.
    if(this.selectedBookCover){
      //Reading a file
      const reader = new FileReader();
      reader.onload = () => {
        this.selectedPicture = reader.result as string;
      }
      reader.readAsDataURL(this.selectedBookCover);
    }
  }
}
