import { Component } from '@angular/core';
import {BookRequestDto} from "../../../../services/models/book-request-dto";
import {saveBook} from "../../../../services/fn/book/save-book";
import {BookService} from "../../../../services/services/book.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-manage-book',
  templateUrl: './manage-book.component.html',
  styleUrl: './manage-book.component.scss'
})
export class ManageBookComponent  {
  errorMsg: string[] = [];
  selectedBookCover: any;
  selectedPicture: string | undefined;
  bookRequest : BookRequestDto = {title: "", authorName: "", isbn: "", synopsis: "", shareable: false};

  constructor(private bookService: BookService, private router: Router){

  }

  onFileSelected(event: any) {
    this.selectedBookCover = event.target.files[0];
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

  saveBook() {
    this.bookService.saveBook({
      body: this.bookRequest
    }).subscribe({
      next: (bookId) => {
        this.bookService.uploadBookCoverPicture({
          "book-id": bookId,
          body: {
            file: this.selectedBookCover,
          }
        }).subscribe({
          next: () => {
            this.router.navigate(['/books/my-books']);
          },
          error: (err) => {
            console.log(err.error);
          }
        })
      },
      error: (err) => {
        this.errorMsg = err.error["validationErrors"];
        console.log(err);
      }
    })
  }
}
