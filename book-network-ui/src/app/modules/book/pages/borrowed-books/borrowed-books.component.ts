import {Component, OnInit} from '@angular/core';
import {BorrowedBookResponse} from "../../../../services/models/borrowed-book-response";
import {PageResponseBorrowedBookResponse} from "../../../../services/models/page-response-borrowed-book-response";
import {BookService} from "../../../../services/services/book.service";

@Component({
  selector: 'app-borrowed-books',
  templateUrl: './borrowed-books.component.html',
  styleUrl: './borrowed-books.component.scss'
})
export class BorrowedBooksComponent implements OnInit{

  page = 0;
  size = 5;

  constructor(private bookService: BookService) {
  }

  myBorrowedBooks : PageResponseBorrowedBookResponse = {};

  returnBorrowBook(book: BorrowedBookResponse) {

  }

  ngOnInit() {
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
}
