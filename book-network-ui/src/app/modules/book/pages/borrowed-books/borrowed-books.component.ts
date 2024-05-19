import {Component, OnInit} from '@angular/core';
import {BorrowedBookResponse} from "../../../../services/models/borrowed-book-response";
import {PageResponseBorrowedBookResponse} from "../../../../services/models/page-response-borrowed-book-response";
import {BookService} from "../../../../services/services/book.service";
import {FeedbackRequest} from "../../../../services/models/feedback-request";
import {FeedbackService} from "../../../../services/services/feedback.service";

@Component({
  selector: 'app-borrowed-books',
  templateUrl: './borrowed-books.component.html',
  styleUrl: './borrowed-books.component.scss'
})
export class BorrowedBooksComponent implements OnInit{

  page = 0;
  size = 5;
  selectedBook : BorrowedBookResponse | undefined = undefined
  myBorrowedBooks : PageResponseBorrowedBookResponse = {};
  feedbackRequest : FeedbackRequest = {bookId: 0, note: 0, comments: ""}


  constructor(private bookService: BookService, private feedbackService: FeedbackService) {
  }


  returnBorrowBook(book: BorrowedBookResponse) {
    this.selectedBook = book;
    this.feedbackRequest.bookId = book.id as number;
  }

  ngOnInit() {
    this.findAllBorrowedBooks();
  }

  private findAllBorrowedBooks(){
    this.bookService.findAllBorrowedBooks({
        page: this.page,
        size: this.size
    }).subscribe({
      next: (resp) => {
        this.myBorrowedBooks = resp;
      }
    })
  }

  goToFirstPage() {
    this.page = 0;
    this.findAllBorrowedBooks()
  }

  goToPreviousPage() {
    this.page--;
    this.findAllBorrowedBooks()
  }

  goToPage(pageIndex: number) {
    this.page = pageIndex;
    this.findAllBorrowedBooks()
  }

  goToNextPage() {
    this.page++;
    this.findAllBorrowedBooks()
  }

  goToLastPage() {
    this.page = this.myBorrowedBooks.totalPages as number -1;
    this.findAllBorrowedBooks()
  }

  returnBook(withFeedback: boolean) {
    this.bookService.returnBorrowedBook({
      'book-id': this.selectedBook?.id as number,
    }).subscribe({
      next: () => {
        if(withFeedback){
          this.giveFeedback();
        }
        this.selectedBook = undefined;
        this.findAllBorrowedBooks()
      }
    })
  }

  private giveFeedback(){
    this.feedbackService.saveFeedback({
      body : this.feedbackRequest
    }).subscribe({
      next: () => {

      }
    })
  }
}


