import {Component, OnInit} from '@angular/core';
import {BookRequestDto} from "../../../../services/models/book-request-dto";
import {saveBook} from "../../../../services/fn/book/save-book";
import {BookService} from "../../../../services/services/book.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-manage-book',
  templateUrl: './manage-book.component.html',
  styleUrl: './manage-book.component.scss'
})
export class ManageBookComponent implements  OnInit {
  errorMsg: string[] = [];
  selectedBookCover: any;
  selectedPicture: string | undefined;
  bookRequest : BookRequestDto = {title: "", authorName: "", isbn: "", synopsis: "", shareable: false};

  constructor(private bookService: BookService,
              private router: Router,
              private activatedRoute: ActivatedRoute){

  }

  //Displaying the details of the book with the corresponding id from the activated route.
  ngOnInit() {
    const bookId = this.activatedRoute.snapshot.params['bookId'];
    if(bookId){
      this.bookService.findBookById({
        "book-id": bookId
      }).subscribe({
        next: (book) => {
          this.bookRequest = {
            id: book.id,
            title: book.title as string,
            authorName: book.authorName as string,
            isbn: book.isbn as string,
            synopsis: book.synopsis as string,
            shareable: book.shareable
          };
          if(book.coverImage){
            this.selectedPicture = 'data:image/jpeg;base64,' + book.coverImage;
          }
        }
      })
    }
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
