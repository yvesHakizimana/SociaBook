import {Component, OnInit, signal} from '@angular/core';
import {BorrowedBookResponse} from "../../../../services/models/borrowed-book-response";
import {PageResponseBorrowedBookResponse} from "../../../../services/models/page-response-borrowed-book-response";
import {FeedbackRequest} from "../../../../services/models/feedback-request";
import {BookService} from "../../../../services/services/book.service";
import {FeedbackService} from "../../../../services/services/feedback.service";

@Component({
  selector: 'app-returned-books',
  templateUrl: './returned-books.component.html',
  styleUrl: './returned-books.component.scss'
})
export class ReturnedBooksComponent implements OnInit {
  page = 0;
  size = 5;
  returnedBooks:  PageResponseBorrowedBookResponse = {};
  level = "success";
  message = "";


  constructor(private bookService: BookService,
              private feedbackService: FeedbackService) {
  }


  ngOnInit() {
    this.findAllReturnedBooks();
  }

  private findAllReturnedBooks(){
    this.bookService.findAllReturnedBooks({
      page: this.page,
      size: this.size
    }).subscribe({
      next: (resp) => {
        this.returnedBooks = resp;
      }
    })
  }

  goToFirstPage() {
    this.page = 0;
    this.findAllReturnedBooks();
  }

  goToPreviousPage() {
    this.page--;
    this.findAllReturnedBooks();
  }

  goToPage(pageIndex: number) {
    this.page = pageIndex;
    this.findAllReturnedBooks();
  }

  goToNextPage() {
    this.page++;
    this.findAllReturnedBooks();
  }

  goToLastPage() {
    this.page = this.returnedBooks.totalPages as number -1;
    this.findAllReturnedBooks();
  }

  approveBookReturn(book: BorrowedBookResponse) {
    if(!book.returned){
      this.message = "The Book is not yet returned."
      this.level = "success"
      return;
    }
    this.bookService.approveReturnedBorrowedBook({
      'book-id': book.id as number
    }).subscribe({
      next: () =>{
        this.message = "Book Return Approved"
        this.level = "success"
        this.findAllReturnedBooks();
      },
      error: (err) => {
        console.log(err)
      }
    })
  }
}
